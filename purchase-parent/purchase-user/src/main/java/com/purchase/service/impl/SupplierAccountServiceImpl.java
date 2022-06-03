package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purchase.dao.mapper.SupplierMapper;
import com.purchase.dao.po.Supplier;
import com.purchase.global.DefaultData;
import com.purchase.service.SupplierAccountService;
import com.purchase.util.VerifyCodeGenerator;
import com.purchase.vo.param.LoginParam;
import com.purchase.vo.param.SupplierRegisterParam;
import com.purchase.vo.param.VerifyCodeEmailParam;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class SupplierAccountServiceImpl implements SupplierAccountService {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private DefaultData defaultData;
    @Resource
    private SupplierMapper supplierMapper;


    @Override
    public Boolean isExistEmail(String email) {
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        Supplier supplier = supplierMapper.selectOne(queryWrapper);
        return supplier!=null&&supplier.getSupplierId()!=null;
    }

    @Override
    public Boolean isExistLoginname(String loginname) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("loginname", loginname);
        Supplier supplier = supplierMapper.selectOne(queryWrapper);
        return supplier!=null&&supplier.getSupplierId()!=null;
    }

    @Override
    public Boolean isExistSuppliername(String suppliername) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("suppliername", suppliername);
        Supplier supplier =  supplierMapper.selectOne(queryWrapper);
        return supplier!=null&&supplier.getSupplierId()!=null;
    }

    @Override
    public void sendRegisterVerifyCode(String email) {
        String verifyCode = VerifyCodeGenerator.generator();
        redisTemplate.opsForValue().set("supplierRegister_" + email, verifyCode, 10, TimeUnit.MINUTES);
        VerifyCodeEmailParam verifyCodeEmailParam = new VerifyCodeEmailParam();
        verifyCodeEmailParam.setEmail(email);
        verifyCodeEmailParam.setVerifyCode(verifyCode);
        verifyCodeEmailParam.setType("register");
        rabbitTemplate.convertAndSend("routing_exchange", "purchase_verifyCode_email", verifyCodeEmailParam);
    }

    @Override
    public Boolean register(SupplierRegisterParam registerParam) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(registerParam, supplier);
        supplier.setIsDelete(0);
        supplier.setState(0);
        supplier.setAvatar(defaultData.AVATAR_URL);
        supplier.setCreateTime(LocalDateTime.now());
        return supplierMapper.insert(supplier) > 0 ? true : false;
    }

    @Override
    public Supplier login(LoginParam loginParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("loginname", loginParam.getUserUniqueId());
        queryWrapper.eq("password", loginParam.getPassword());
        return supplierMapper.selectOne(queryWrapper);
    }
}
