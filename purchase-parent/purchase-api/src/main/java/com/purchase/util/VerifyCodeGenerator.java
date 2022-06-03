package com.purchase.util;

/**
 * 验证码生成工具类
 */
public class VerifyCodeGenerator {

    public static String generator() {
        //生成7位数验证码
        String verifyCode = "";
        for (int i = 1; i <= 7; i++) {
            verifyCode += (int) (Math.random() * 10) + "";
        }
        return verifyCode;
    }
}
