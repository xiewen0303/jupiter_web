package com.jupiter.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jupiter.web.manager.utils.AESUtil;
import com.jupiter.web.manager.utils.Base64;
import com.jupiter.web.manager.utils.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class EncryFilter extends OncePerRequestFilter {

    @Autowired
    private AppCofig appCofig;

    @Value("${rsa.client.public_key}")
    private String clientPublicKey;

    @Value("${rsa.server.private_key8}")
    private String private_key8;

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            boolean needEncry = false;
            String header = request.getHeader("XENCR");
            if (header != null && header.equals("1")) {
                String add_channel = request.getParameter("add_channel");
                if (add_channel != null && add_channel.equals("microsite")) {
                    log.debug("microsite do not need decrypt");
                } else {
                    needEncry = true;
                }
            }
            log.debug("------start decrypt------ " + needEncry);
            if (needEncry) {
                String param = request.getParameter("param");
                String key = request.getParameter("key");
//            if (request.getMethod().equals("GET")) {
//                key = URLDecoder.decode(key, "UTF-8");
//                param = URLDecoder.decode(param, "UTF-8");
//            }
                String aesKey = RSAUtil.decrypt(key, private_key8);
                param = new String(AESUtil.decrypt(aesKey.getBytes(), aesKey.getBytes(), Base64.decodeToBytes(param)));

                Map<String, Object> paramter = new HashMap();
                JSONObject json = JSONObject.parseObject(param);
                for (Map.Entry<String, Object> entry : json.entrySet()) {
                    if (entry.getKey().equals("key") || entry.getKey().equals("param")) {
                        continue;
                    }
                    paramter.put(entry.getKey(), entry.getValue());
                }

                RequestWrapper mRequest = new RequestWrapper(request, paramter);
                ResponseWrapper mResponse = new ResponseWrapper(response);
                chain.doFilter(mRequest, mResponse);

                byte[] bytes = mResponse.getBytes();
                String backMsg = new String(bytes, "UTF-8");
                String encode = backMsg;

                aesKey = AESUtil.getRandomKey();
                String keyData = RSAUtil.encrypt(aesKey, clientPublicKey);
                keyData = getLineString(keyData);

                byte[] dataArr = AESUtil.encrypt(aesKey.getBytes(), aesKey.getBytes(), encode.getBytes());
                encode = new String(Base64.encode(dataArr));
                encode = getLineString(encode);
                keyData += encode;

                bytes = keyData.getBytes();
                response.setHeader("X-KEY-LINE", "3");
                //response.addHeader("Transfer-Encoding", "chunked");
                response.setHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
                response.setContentType("text/plain; charset=UTF-8");

                String origin = request.getHeader("HTTP_ORIGIN");
                if (origin == null || origin.trim().isEmpty()) {
                    response.setHeader("Access-Control-Allow-Origin", "*");
                } else if (origin.matches(appCofig.getAllow_origin())) {
                    response.setHeader("Access-Control-Allow-Origin", origin);
                }
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization");

                response.getOutputStream().write(bytes);
            } else {
                chain.doFilter(request, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("EncryFilter IOException====================", e);
        } catch (ServletException e) {
            log.error("EncryFilter ServletException===============", e);
        } catch (Exception e) {
            log.error("EncryFilter Exception======================", e);

        }
    }

    private String getLineString(String data) {
        int len = data.length();
        StringBuilder sb = new StringBuilder(data);
        final int add = 76;
        int index = add;
        int count = 0;
        boolean flag = true;
        while (flag) {
            if (index >= len) {
                index = len;
                flag = false;
            }
            sb.insert(index + count, "\n");
            count++;
            index += add;
        }
        return sb.toString();
    }

    @Override
    public void destroy() {
    }

}