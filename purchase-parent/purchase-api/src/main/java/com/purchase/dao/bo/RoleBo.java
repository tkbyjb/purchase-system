package com.purchase.dao.bo;

import com.purchase.dao.po.Menu;
import com.purchase.dao.po.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleBo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long roleId;
    private Role role;
    private List<Menu> menus;
}
