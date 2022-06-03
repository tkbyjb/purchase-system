package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class RoleVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roleId;
    private String rolename;
}
