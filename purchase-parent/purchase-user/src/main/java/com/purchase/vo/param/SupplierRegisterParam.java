package com.purchase.vo.param;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SupplierRegisterParam {
    @Pattern(regexp = "[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,128}")
    private String suppliername;
    @Email
    private String email;
    @Pattern(regexp = "(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}")
    private String tel;
    @Pattern(regexp = "[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,128}")
    private String loginname;
    @Pattern(regexp = "[a-zA-Z0-9_]{6,128}")
    private String password;
    @Min(0)
    @Max(1)
    @NotNull
    private Integer agreement;
    @NotNull
    @NotEmpty
    private String verifyCode;
}
