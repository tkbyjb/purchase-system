package com.purchase.vo.param;

import lombok.Data;

@Data
public class VerifyCodeEmailParam {
    private String email;
    private String verifyCode;
    private String type;
}
