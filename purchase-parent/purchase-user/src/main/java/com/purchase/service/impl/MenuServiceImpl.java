package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purchase.dao.bo.MenuBo;
import com.purchase.dao.mapper.MenuMapper;
import com.purchase.dao.po.Menu;
import com.purchase.service.MenuService;
import com.purchase.vo.MenuVo;
import com.purchase.vo.system.MenuManaVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getRoleMenuByParentId(Long roleId, Long parentId) {
        return menuMapper.getRoleMenuByParentId(roleId, parentId);
    }

    @Override
    public List<MenuBo> getRoleMenu(Long roleId, Long parentId) {
        List<MenuBo> menuBos = new ArrayList<>();
        List<Menu> menus = getRoleMenuByParentId(roleId, parentId);
        for(Menu menu : menus) {
            MenuBo menuBo = new MenuBo();
            menuBo.setMenu(menu);
            menuBo.setChilds(getRoleMenu(roleId, menu.getMenuId()));
            menuBos.add(menuBo);
        }
        return menuBos;
    }

    @Override
    public List<MenuVo> getRoleMenuVo(Long roleId, Long parentId) {
        List<MenuVo> menuVos = new ArrayList<>();
        List<Menu> menus = menuMapper.getRoleMenuByParentId(roleId, parentId);
        for(Menu menu : menus) {
            MenuVo menuVo = new MenuVo();
            menuVo.setMenuname(menu.getMenuname());
            menuVo.setPermission(menu.getPermission());
            menuVo.setIcon(menu.getIcon());
            menuVo.setChilds(getRoleMenuVo(roleId, menu.getMenuId()));
            menuVos.add(menuVo);
        }
        return menuVos;
    }

    @Override
    public List<MenuManaVo> getMenuManaVo(Long roleId, Long parentId) {
        List<MenuManaVo> menuManaVos = new ArrayList<>();
        List<Menu> menus =  menuMapper.getRoleMenuByParentId(roleId, parentId);
        for(Menu menu : menus) {
            MenuManaVo menuManaVo = new MenuManaVo();
            menuManaVo.setMenuId(menu.getMenuId());
            menuManaVo.setMenuname(menu.getMenuname());
            menuManaVo.setChilds(getMenuManaVo(roleId, menu.getMenuId()));
            menuManaVos.add(menuManaVo);
        }
        return menuManaVos;
    }

    @Override
    public List<MenuManaVo> getAllMenuManaVos(Long parentId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(parentId != null) {
            queryWrapper.eq("parentId", parentId);
        }else{
            queryWrapper.isNull("parentId");
        }
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        List<MenuManaVo> menuManaVos = new ArrayList<>();
        for (Menu menu : menus) {
            MenuManaVo menuManaVo = new MenuManaVo();
            BeanUtils.copyProperties(menu, menuManaVo);
            menuManaVo.setChilds(getAllMenuManaVos(menu.getMenuId()));
            menuManaVos.add(menuManaVo);
        }
        return menuManaVos;
    }
}
