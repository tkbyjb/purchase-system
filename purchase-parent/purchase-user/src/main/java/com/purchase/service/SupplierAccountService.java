package com.purchase.service;

import com.purchase.dao.po.Supplier;
import com.purchase.vo.param.LoginParam;
import com.purchase.vo.param.SupplierRegisterParam;

public interface SupplierAccountService {
    Boolean isExistEmail(String email);
    Boolean isExistLoginname(String loginname);
    Boolean isExistSuppliername(String suppliername);
    void sendRegisterVerifyCode(String email);
    Boolean register(SupplierRegisterParam registerParam);
    Supplier login(LoginParam loginParam);
}
