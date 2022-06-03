package com.purchase.global;

import org.springframework.expression.spel.ast.NullLiteral;

public enum ResultState {
    SUCCESS("成功", 1000),
    SUCCESS_NO_MESS(null, 1001),
    SERVER_ERROR("服务器发生错误", 1001),
    EXIST_EMAIL("邮箱已经被注册", 1501),
    EXIST_USERNAME("用户名已经存在", 1502),
    EXIST_SUPPLIERNAME("供应商名已经存在", 1503),
    VERIFYCOE_ERROR("验证码过期或错误,请重新获取", 1503),
    PARMA_ERROR("用户参数不合法", 1701),
    LOGIN_FAIL("登录失败", 1801),
    UPLOAD_FILE_NOT_LEGAL("上传恶意文件导致失败", 1901),
    NO_PERMISSION("没有权限", 1902),
    NO_BALANCE("经费不足", 1999);

    private String mess;
    private Integer state;

    private ResultState(String mess, Integer state) {
        this.mess = mess;
        this.state = state;
    }


    public String getMess(){
        return this.mess;
    }

    public Integer getState() {
        return this.state;
    }
}
