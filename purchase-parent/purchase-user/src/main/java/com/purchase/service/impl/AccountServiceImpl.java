package com.purchase.service.impl;

import com.purchase.dao.po.User;
import com.purchase.global.DefaultData;
import com.purchase.service.AccountService;
import com.purchase.service.UserService;
import com.purchase.util.VerifyCodeGenerator;
import com.purchase.vo.param.LoginParam;
import com.purchase.vo.param.RegisterParam;
import com.purchase.vo.param.VerifyCodeEmailParam;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private DefaultData defaultData;

    @Override
    public Boolean isExistEmail(String email) {
        return userService.getUserByEmail(email) != null?true:false;
    }

    @Override
    public Boolean isExistUsername(String username) {
        return userService.getUserByUsername(username) != null?true:false;
    }

    @Override
    public void sendRegisterVerifyCode(String email) {
        String verifyCode = VerifyCodeGenerator.generator();
        redisTemplate.opsForValue().set("register_"+email, verifyCode, 10, TimeUnit.MINUTES);
        VerifyCodeEmailParam verifyCodeEmailParam = new VerifyCodeEmailParam();
        verifyCodeEmailParam.setEmail(email);
        verifyCodeEmailParam.setVerifyCode(verifyCode);
        verifyCodeEmailParam.setType("register");
        rabbitTemplate.convertAndSend("routing_exchange", "purchase_verifyCode_email", verifyCodeEmailParam);
    }

    @Override
    @Transactional
    public Boolean userRegister(RegisterParam registerParam) {
        User user = new User();
        BeanUtils.copyProperties(registerParam, user);
        user.setCreateTime(LocalDateTime.now());
        user.setState(1);
        user.setIsDelete(0);
        user.setAvatar(defaultData.getAVATAR_URL());
        user.setEmailNotice(0);
        Long userId = userService.addUser(user);
        return (userService.updateCreateUserId(userId, userId) > 0)?true:false;
    }

    @Override
    public User userLogin(LoginParam loginParam) {
        return userService.getUserByUsernameAndPassword(loginParam.getUserUniqueId(), loginParam.getPassword());
    }
}
