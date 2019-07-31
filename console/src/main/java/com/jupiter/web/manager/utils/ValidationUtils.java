package com.jupiter.web.manager.utils;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

public class ValidationUtils {
	
	private static final String MSG_SEPARATOR = "|";
	
	/**
	 * 校验
	 * @param obj
	 * @return
	 */
	public static BindingResult invokeValidator(Object obj) {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator targetValidator = validatorFactory.getValidator();
		org.springframework.validation.Validator validator = new SpringValidatorAdapter(targetValidator);
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(obj, obj.getClass().getName());
		org.springframework.validation.ValidationUtils.invokeValidator(validator, obj, bindingResult);
		return bindingResult;
	}
	
	/**
	 * 校验</br>
	 * 未通过抛出BindException
	 * @param obj
	 * @throws BindException
	 */
	public static void valid(Object obj) throws BindException {
		BindingResult bindingResult = invokeValidator(obj);
		if(bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
	}
	
	/**
	 * 校验</br>
	 * 通过，返回null</br>
	 * 未通过，返回所有的未验证通过message</br>
	 * @param obj
	 * @return
	 */
	public static String validate(Object obj) {
		BindingResult bindingResult = invokeValidator(obj);
		if(bindingResult.hasErrors()) {
			return parseBindingResult(bindingResult);
		}
		return null;
	}
	
	/**
	 * 校验</br>
	 * 通过，返回null</br>
	 * 未通过，返回一个未验证通过message</br>
	 * @param obj
	 * @return
	 */
	public static String validateSimple(Object obj) {
		BindingResult bindingResult = invokeValidator(obj);
		if(bindingResult.hasErrors()) {
			return bindingResult.getFieldError().getDefaultMessage();
		}
		return null;
	}
	
	/**
	 * 解析参数错误message
	 * @param bindingResult
	 * @return
	 */
	public static String parseBindingResult(BindingResult bindingResult) {
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		if(fieldErrors.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for(FieldError fieldError: fieldErrors) {
				sb.append(fieldError.getDefaultMessage()).append(MSG_SEPARATOR);
			}
			int length = sb.length();
			sb.delete(length - MSG_SEPARATOR.length(), length);
			return sb.toString();
		}
		return null;
	}

	public static String getFirstMessage(BindingResult bindingResult) {
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		if(fieldErrors.size() > 0) {
			return fieldErrors.get(0).getDefaultMessage();
		}
		return null;
	}

}
