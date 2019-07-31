package com.jupiter.web.manager.utils;

import com.jupiter.web.manager.constants.Constant;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;


public class Utils {
    /**
     * 获取随机数字
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "01234567890123456789012345678901234567890123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    public static String encodeMD5(String md5key) {
        try {
            java.security.MessageDigest md5;
            md5 = java.security.MessageDigest.getInstance("MD5");
            md5.update(md5key.getBytes("UTF-8"));
            StringBuilder md5Hash = new StringBuilder();
            for (byte b : md5.digest()) {
                md5Hash.append(Integer.toHexString((b & 0xf0) >>> 4));
                md5Hash.append(Integer.toHexString(b & 0x0f));
            }
            return md5Hash.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 通用唯一识别码
     *
     * @return
     */
    public static synchronized String UUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 将数字转换成字符串
     *
     * @param f
     * @return
     */
    public static String number4String(BigDecimal f) {
        DecimalFormat df = new DecimalFormat();
        String style = "0.0000";// 定义要显示的数字的格式
        df.applyPattern(style);
        return df.format(f);
    }

    /**
     * 填补位数
     *
     * @param f
     * @return
     */
    public static String number2String(BigDecimal f) {
        DecimalFormat df = new DecimalFormat();
        String style = "0.00";// 定义要显示的数字的格式
        df.applyPattern(style);
        return df.format(f);
    }


    /**
     * 填补位数
     *
     * @param f
     * @return
     */
    public static String number3String(BigDecimal f) {
        DecimalFormat df = new DecimalFormat();
        String style = "0.000";// 定义要显示的数字的格式
        df.applyPattern(style);
        return df.format(f);
    }


    /**
     * 填补位数
     *
     * @param f
     * @return
     */
    public static String number8String(BigDecimal f) {
        DecimalFormat df = new DecimalFormat();
        String style = "0.00000000";// 定义要显示的数字的格式
        df.applyPattern(style);
        return df.format(f);
    }


    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    public final static String getIpAddr(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("HTTP_X_FORWARDED_FOR");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Forwarded-For");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    /**
     * 检测参数是否为数值
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().length() == 0)
            return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 隐藏登录名
     *
     * @param loginName
     * @return
     */
    public static String formatloginName(String loginName) {
        if (PhoneValiUtil.isMobile(loginName)) {
            loginName = loginName.substring(0, 3) + "****" + loginName.substring(loginName.length() - 4, loginName.length());
        } else if (loginName.matches(Constant.EmailReg)) {
            String[] emails = loginName.split("@");
            if (emails[0].length() > 3) {
                loginName = emails[0].substring(0, 3) + "****@" + emails[1];
            } else {
                loginName = emails[0].substring(0, 1) + "****@" + emails[1];
            }
        } else {
            loginName = loginName.substring(0, 6) + "****";
        }
        return loginName;
    }

}
