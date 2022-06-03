package com.purchase.service;

import com.purchase.dao.bo.RoleBo;
import com.purchase.dao.po.Role;
import com.purchase.vo.RoleVo;
import com.purchase.vo.param.system.RoleAddParam;
import com.purchase.vo.param.system.RoleGetParam;
import com.purchase.vo.param.system.RoleUpdateParam;
import com.purchase.vo.system.RoleMenuListVo;

import java.util.List;

public interface RoleService {
    List<Role> getAllRole();
    List<RoleVo> getAllRoleVo();
    List<RoleBo> getAllRoleBo();
    RoleMenuListVo getRoleinfos(RoleGetParam roleGetParam);
    Boolean updateRole(RoleUpdateParam roleUpdateParam);
    Boolean addRole(RoleAddParam roleAddParam);
    Boolean deleteRoles(List<Long> roleIds);
}
