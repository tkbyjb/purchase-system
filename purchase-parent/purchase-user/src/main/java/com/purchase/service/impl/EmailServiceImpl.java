package com.purchase.service.impl;

import com.purchase.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    private JavaMailSender javaMailSender;

    @Override
    @Async
    public void sendRegisterVerifyCode(String email, String verifyCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("采购系统 [用户注册验证码]");
        message.setFrom("wanthkuail@163.com");
        message.setTo(email);
        message.setSentDate(new Date());
        message.setText("验证码: "+verifyCode+"  ,此验证码有效时间为10分钟,请在有效期内注册");
        javaMailSender.send(message);
    }




}
