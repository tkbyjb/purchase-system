package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.MenuBo;
import com.purchase.dao.bo.RoleBo;
import com.purchase.dao.mapper.MenuMapper;
import com.purchase.dao.mapper.RoleMapper;
import com.purchase.dao.mapper.RoleMenuMapper;
import com.purchase.dao.mapper.UserMapper;
import com.purchase.dao.po.Role;
import com.purchase.dao.po.RoleMenu;
import com.purchase.dao.po.User;
import com.purchase.service.MenuService;
import com.purchase.service.RoleService;
import com.purchase.vo.RoleVo;
import com.purchase.vo.param.system.RoleAddParam;
import com.purchase.vo.param.system.RoleGetParam;
import com.purchase.vo.param.system.RoleUpdateParam;
import com.purchase.vo.system.MenuManaVo;
import com.purchase.vo.system.RoleMenuListVo;
import com.purchase.vo.system.RoleMenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<Role> getAllRole() {
        return roleMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public List<RoleVo> getAllRoleVo() {
        List<Role> roles = getAllRole();
        List<RoleVo> roleVos = new ArrayList<>();
        roles.forEach(role -> {
            RoleVo roleVo = new RoleVo();
            BeanUtils.copyProperties(role, roleVo);
            roleVos.add(roleVo);
        });
        return roleVos;
    }

    @Override
    public List<RoleBo> getAllRoleBo() {
        return roleMapper.getAllRoleBo();
    }

    @Override
    public RoleMenuListVo getRoleinfos(RoleGetParam roleGetParam) {
        Page<Role> page = new Page<>(roleGetParam.getPageParam().getPageIndex(), roleGetParam.getPageParam().getPageSize());
        roleMapper.getRoles(page, roleGetParam);

        RoleMenuListVo roleMenuListVo = new RoleMenuListVo();
        roleMenuListVo.setTotal((int)page.getTotal());

        List<RoleMenuVo> roleMenuVos = new ArrayList<>();
        for (Role role : page.getRecords()) {
            RoleMenuVo roleMenuVo = new RoleMenuVo();
            BeanUtils.copyProperties(role, roleMenuVo);
            List<MenuManaVo> menuManaVos = menuService.getMenuManaVo(role.getRoleId(), null);
            roleMenuVo.setMenus(menuManaVos);
            roleMenuVos.add(roleMenuVo);
        }
        roleMenuListVo.setRoles(roleMenuVos);
        return roleMenuListVo;
    }

    @Override
    @Transactional
    public Boolean updateRole(RoleUpdateParam roleUpdateParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("rolename", roleUpdateParam.getRolename());
        Role r = roleMapper.selectOne(queryWrapper);
        if(r != null && r.getRoleId() != roleUpdateParam.getRoleId()) {
            return false;
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleUpdateParam, role);
        role.setState(1);
        role.setIsDelete(0);
        roleMapper.updateById(role);

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.in("roleId", roleUpdateParam.getRoleId());
        roleMenuMapper.delete(queryWrapper1);

        for (Long menuId : roleUpdateParam.getMenuIds()) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getRoleId());
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean addRole(RoleAddParam roleAddParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("rolename", roleAddParam.getRolename());
        if(roleMapper.selectOne(queryWrapper) != null) {
            return false;
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleAddParam, role);
        role.setState(1);
        role.setIsDelete(0);
        roleMapper.insert(role);

        for (Long menuId : roleAddParam.getMenuIds()) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getRoleId());
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteRoles(List<Long> roleIds) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.in("roleId", roleIds);
//        for(User user : userMapper.selectList(queryWrapper)) {
//            userMapper.upateUserState(3, user.getUserId());
//            userMapper.setRoleIdNull(user.getUserId());
//        }
        if(userMapper.selectList(queryWrapper).size() > 0){
            return false;
        }

//        QueryWrapper<RoleMenu> queryWrapper1 = new QueryWrapper();
//        queryWrapper1.in("roleId", roleIds);
//        roleMenuMapper.delete(queryWrapper1);

        roleMapper.delete(new QueryWrapper<Role>().in("roleId", roleIds));
        return true;
    }
}
