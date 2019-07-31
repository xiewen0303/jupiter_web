package com.jupiter.web.interceptor;

import com.jupiter.web.manager.utils.RequestUtil;
import com.jupiter.web.manager.utils.ValidationUtils;
import com.tron.common.exception.BaseException;
import com.tron.common.exception.BusinessExcepiton;
import com.tron.common.exception.ErrorCode;
import com.tron.framework.dto.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {
	
	/**
	 * 	控制器全局异常处理
	 * @param e
	 * @return
	 */
	@ExceptionHandler
	@ResponseBody
	public Object exceptionHandle(Exception e, HttpServletRequest request) {
		ResponseEntity resp = new ResponseEntity();
		resp.setStatus(ResponseEntity.STATUS_FAIL);
		String clientIP = RequestUtil.getClientIP(request);
		String uri = request.getRequestURI();
		String paramStr = RequestUtil.getParamStr(request);
		log.error("【请求异常】客户端IP：{}，请求地址：{}，参数：{}", clientIP, uri, paramStr);
		if(e instanceof BusinessExcepiton) {
			log.error("业务异常", e);
			resp.setError(((BusinessExcepiton) e).getErrorCode());
			resp.setMsg(StringUtils.defaultIfBlank(((BusinessExcepiton) e).getErrorMsg(), ErrorCode.getMsg(resp.getError())));
		} else if(e instanceof BaseException) {
			log.error("自定义异常", e);
			resp.setError(((BaseException) e).getErrorCode());
			resp.setMsg(StringUtils.defaultIfBlank(((BaseException) e).getErrorMsg(), ErrorCode.getMsg(resp.getError())));
		} else if(e instanceof MethodArgumentNotValidException) {
			resp.setError(ErrorCode.DEFAULT_FAIL_CODE);
			String msg = ValidationUtils.getFirstMessage(((MethodArgumentNotValidException) e).getBindingResult());
			resp.setMsg(msg);
			log.error("参数异常1：{}", msg);
		} else if(e instanceof BindException) {
			resp.setError(ErrorCode.DEFAULT_FAIL_CODE);
			String msg = ValidationUtils.getFirstMessage((BindException) e);
			resp.setMsg(msg);
			log.error("参数异常2：{}", msg);
		} else if(e instanceof HttpMessageNotReadableException) {
			resp.setError(ErrorCode.DEFAULT_FAIL_CODE);
			resp.setMsg("HTTP请求body为空或者数据格式错误");
			log.error("参数异常3：HTTP请求body为空或者数据格式错误");
		} else if(e instanceof HttpRequestMethodNotSupportedException) {
			log.error("HTTP请求方法不支持", e);
			resp.setError(ErrorCode.DEFAULT_FAIL_CODE);
			resp.setMsg("HTTP请求方法不支持");
		} else if(e instanceof HttpMediaTypeNotSupportedException) {
			log.error("HTTP请求Content-Type不支持", e);
			resp.setError(ErrorCode.DEFAULT_FAIL_CODE);
			resp.setMsg(e.getMessage());
		} else {
			log.error("系统异常", e);
			resp.setError("E00000001");
			resp.setMsg("系统异常");
		}
		return resp;
	}
	
}
