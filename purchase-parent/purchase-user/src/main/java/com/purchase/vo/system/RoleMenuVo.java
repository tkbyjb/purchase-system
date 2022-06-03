package com.purchase.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Null;
import java.util.List;

@Data
public class RoleMenuVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roleId;
    private String rolename;
    private String note;
    List<MenuManaVo> menus;
}
