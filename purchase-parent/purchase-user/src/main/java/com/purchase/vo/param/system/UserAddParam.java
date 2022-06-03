package com.purchase.vo.param.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class UserAddParam {
    private MultipartFile[] files = null;
    @NonNull
    @Pattern(regexp = "[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,128}")
    private String username;
    @Pattern(regexp = "[a-zA-Z0-9_]{6,128}")
    private String password;
    @Email
    private String email;
    @Pattern(regexp = "(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}")
    private String tel;
    @Pattern(regexp = "[\\u4e00-\\u9fa5]{2,6}")
    private String realname;
    @NonNull
    private Integer sex;
    @Pattern(regexp = "[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]")
    private String idNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    //formdata传的必须加前端->后端转换,不知道为什么,不是formdatea的jsonformat是双向都能转换的
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birth;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long departmentId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roleId;
    private Integer emailNotice;
    private String avatar;
}
