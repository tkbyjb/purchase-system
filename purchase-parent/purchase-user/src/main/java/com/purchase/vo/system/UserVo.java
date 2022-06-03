package com.purchase.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String tel;
    private String realname;
    private Integer sex;
    private String idNumber;
    private LocalDate birth;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createTime;
    private String createUserRealname;
    private String rolename;
    private String departmentname;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long departmentId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Null
    private Long roleId;
    private Integer state;
    private Integer emailNotice;
    private String avatar;
}
