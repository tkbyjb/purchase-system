package com.purchase.service;

import com.purchase.dao.bo.MenuBo;
import com.purchase.dao.po.Menu;
import com.purchase.vo.MenuVo;
import com.purchase.vo.param.system.RoleAddParam;
import com.purchase.vo.param.system.RoleUpdateParam;
import com.purchase.vo.system.MenuManaVo;

import java.util.List;

public interface MenuService {
    List<Menu> getRoleMenuByParentId(Long roleId, Long parentId);
    List<MenuBo> getRoleMenu(Long roleId, Long parentId);
    List<MenuVo> getRoleMenuVo(Long roleId, Long parentId);
    List<MenuManaVo> getMenuManaVo(Long roleId, Long parentId);
    List<MenuManaVo> getAllMenuManaVos(Long parentId);

}
