package com.purchase.service;


public interface EmailService {
    void sendRegisterVerifyCode(String email, String verifyCode);
}
