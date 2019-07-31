package com.jupiter.web.manager.bus.common.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.jupiter.web.manager.bus.admin.store.ConfigManager;
import com.jupiter.web.manager.bus.common.dto.OSSPolicyOutputDto;
import com.jupiter.web.manager.bus.common.dto.OSSTokenOutputDto;
import com.jupiter.web.manager.common.entity.User;
import com.tron.common.oss.configuration.OSSProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.*;
import java.net.URI;
import java.util.Date;

/**
 * @author fuyuling
 * @created 2019/7/22 12:01
 */
@Slf4j
@Service
public class OSService {

    @Resource
    private OSSClient ossClient;
    @Resource
    private OSSProperties ossProperties;

    /**
     * 获取OSS policy
     *
     * @param dir
     * @return
     * @throws UnsupportedEncodingException
     */
    public OSSPolicyOutputDto getOSSPolicy(String dir) throws UnsupportedEncodingException {
        String expire = StringUtils.defaultIfBlank(ConfigManager.getValue("oss.policy.expire"), "30000");
        long expireEndTime = System.currentTimeMillis() + Long.valueOf(expire);
        Date expiration = new Date(expireEndTime);
        // 获取policy并生成签名
        PolicyConditions policyConditions = new PolicyConditions();
        String fileMaxLength = StringUtils.defaultIfBlank(ConfigManager.getValue("oss.policy.fileMaxLength"), "1048576000");
        policyConditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, Long.valueOf(fileMaxLength));
        policyConditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
        String postPolicy = ossClient.generatePostPolicy(expiration, policyConditions);
        byte[] binaryData = postPolicy.getBytes("utf-8");
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);
        // 构建响应DTO
        OSSPolicyOutputDto outputDto = new OSSPolicyOutputDto();
        outputDto.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
        outputDto.setPolicy(encodedPolicy);
        outputDto.setSignature(postSignature);
        outputDto.setDir(dir);
        String bucket = ConfigManager.getValue("oss.common.bucketName");
        URI endpoint = ossClient.getEndpoint();
        String host = String.format("%s://%s.%s", endpoint.getScheme(), bucket, endpoint.getHost());
        if (ossClient.getEndpoint().getPort() != -1) {
            host = String.format("%s:%d", host, endpoint.getPort());
        }
        outputDto.setHost(host);
        outputDto.setExpire(expireEndTime / 1000);
        return outputDto;
    }

    /**
     * 获取OSS policy
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public OSSTokenOutputDto getOSSToken(User user) {
        // 只有 RAM用户（子账号）才能调用 AssumeRole 接口
        // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
        // 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
        // RoleArn 需要在 RAM 控制台上获取
        // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
        // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
        try {
            String roleArn = ConfigManager.getValue("oss.RoleArn");
            long durationSeconds = Long.valueOf(ConfigManager.getValue("oss.TokenExpireTime"));
            String policy = readPolicyFromFile(ConfigManager.getValue("oss.policyFilePath"));
            String roleSessionName = user.getId();
            // 此处必须为 HTTPS
            final AssumeRoleResponse stsResponse = assumeRole(ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(),
                    roleArn, roleSessionName, policy, ProtocolType.HTTPS, durationSeconds);
            OSSTokenOutputDto outputDto = new OSSTokenOutputDto();
            outputDto.setStatusCode(OSSTokenOutputDto.STATUS_CODE_SUCCESS);
            outputDto.setAccessKeyId(stsResponse.getCredentials().getAccessKeyId());
            outputDto.setAccessKeySecret(stsResponse.getCredentials().getAccessKeySecret());
            outputDto.setSecurityToken(stsResponse.getCredentials().getSecurityToken());
            outputDto.setExpiration(stsResponse.getCredentials().getExpiration());
            return outputDto;
        } catch (com.aliyuncs.exceptions.ClientException e) {
            OSSTokenOutputDto outputDto = new OSSTokenOutputDto();
            outputDto.setStatusCode(OSSTokenOutputDto.STATUS_CODE_FAIL);
            outputDto.setErrorCode(e.getErrCode());
            outputDto.setErrorMessage(e.getErrMsg());
            return outputDto;
        }
    }

    private AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret, String roleArn,
                                          String roleSessionName, String policy, ProtocolType protocolType, long durationSeconds)
            throws ClientException, com.aliyuncs.exceptions.ClientException {
        // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
        IClientProfile profile = DefaultProfile.getProfile(ConfigManager.getValue("oss.region"), accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);

        // 创建一个 AssumeRoleRequest 并设置请求参数
        final AssumeRoleRequest request = new AssumeRoleRequest();
        request.setMethod(MethodType.POST);
        request.setProtocol(protocolType);

        request.setRoleArn(roleArn);
        request.setRoleSessionName(roleSessionName);
        request.setPolicy(policy);
        request.setDurationSeconds(durationSeconds);

        // 发起请求，并得到response
        return client.getAcsResponse(request);
    }

    /**
     * 读取配置文件
     * @param path
     * @return
     */
    private static String readPolicyFromFile(String path) {
        BufferedReader reader = null;
        StringBuilder data = new StringBuilder();
        //
        try {
            File file = ResourceUtils.getFile(path);
            reader = new BufferedReader(new FileReader(file));
            String temp;
            while ((temp = reader.readLine()) != null) {
                data.append(temp);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("【读取OSS policy配置文件】关闭流异常");
                }
            }
        }
        return data.toString();
    }

}
