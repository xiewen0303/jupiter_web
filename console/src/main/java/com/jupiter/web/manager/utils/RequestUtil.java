package com.jupiter.web.manager.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Slf4j
public class RequestUtil {

	public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-FORWARDED-FOR");
		}
		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if(ip != null && ip.length() > 1){
			ip = ip.split(",")[0];
		}

		return ip;
	}
	
	/**
	 * 重新组装参数
	 * @param params
	 * @return
	 */
	public static Map<String, String> buildParams(Map<String, String[]> params) {
		Set<Entry<String, String[]>> entrySet = params.entrySet();
		Map<String, String> paramMap = new HashMap<String, String>();
		for(Entry<String, String[]> entry: entrySet) {
			String[] value = entry.getValue();
			if(value == null || value.length == 0) {
				paramMap.put(entry.getKey(), "");						
			} else if(value.length == 1) {
				paramMap.put(entry.getKey(), value[0]);
			} else {
				StringBuilder newValue = new StringBuilder();
				for(String val: value) {
					newValue.append(StringUtils.defaultString(val, "")).append(",");
				}
				if(newValue.length() > 0) {
					newValue.deleteCharAt(newValue.length() - 1);
				}
				paramMap.put(entry.getKey(), newValue.toString());
			}
		}
		return paramMap;
	}
	
	public static String getParamStr(HttpServletRequest request) {
		try {
			String contentType = request.getContentType();
			if("GET".equalsIgnoreCase(request.getMethod()) || contentType != null && (StringUtils.containsIgnoreCase(contentType, "multipart/form-data") 
					|| StringUtils.containsIgnoreCase(contentType, "application/x-www-form-urlencoded"))) {
				Map<String, String[]> parameterMap = request.getParameterMap();
				if(parameterMap != null && parameterMap.size() > 0) {
					Map<String, String> map = buildParams(parameterMap);
					return JSON.toJSONString(map);
				}
			} else {
				ServletInputStream sis = request.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(sis, baos);
				byte[] body = baos.toByteArray();
				return new String(body, "UTF-8");
			}
		} catch (Exception e) {
			log.error("参数格式化异常", e);
		}
		return "";
	}

}
