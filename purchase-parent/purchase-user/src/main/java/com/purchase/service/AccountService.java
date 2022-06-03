package com.purchase.service;

import com.purchase.dao.po.User;
import com.purchase.vo.param.LoginParam;
import com.purchase.vo.param.RegisterParam;

public interface AccountService {
    Boolean isExistEmail(String email);
    Boolean isExistUsername(String username);
    void sendRegisterVerifyCode(String email);
    Boolean userRegister(RegisterParam registerParam);
    User userLogin(LoginParam loginParam);
}
