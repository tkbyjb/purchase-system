package com.purchase.vo.system;

import lombok.Data;

import java.util.List;

@Data
public class RoleMenuListVo {
    private List<RoleMenuVo> roles;
    private Integer total;
}
