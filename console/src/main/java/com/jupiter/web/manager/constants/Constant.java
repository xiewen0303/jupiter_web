package com.jupiter.web.manager.constants;

public class Constant {

	public static String EmailReg = "^([a-zA-Z0-9_\\-\\.\\+]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";// 邮箱正则
	public static String PhoneReg = "^[0-9]*$";// "^((1[0-9]{2})|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$";

	/**
	 * 图片验证码，redis过期时间
	 */
	public static int IMAGESEXPIRETIME = 30 * 60;

	// 商品卡片信息的redis过期时间
	public static int GOODS_CARD_INFO_RESULT_EXPIRE_TIME = 30 * 60;


}
