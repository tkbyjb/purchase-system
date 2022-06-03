package com.purchase.vo.param;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class LoginParam {
    @Pattern(regexp = "[a-zA-Z0-9_@.\\u4e00-\\u9fa5]{2,128}")
    private String userUniqueId;
    @Pattern(regexp = "[a-zA-Z0-9_]{6,128}")
    private String password;
}
