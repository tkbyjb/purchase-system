package com.purchase.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.RoleBo;
import com.purchase.dao.po.Role;
import com.purchase.vo.param.system.RoleGetParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<RoleBo> getAllRoleBo();
    Page<Role> getRoles(Page<Role> page, @Param("param")RoleGetParam roleGetParam);
}
