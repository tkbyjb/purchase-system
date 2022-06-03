package com.purchase.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.UserBo;
import com.purchase.dao.po.User;
import com.purchase.vo.param.system.UserGetParam;
import com.purchase.vo.param.system.UserUpdateParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    Integer deleteDepartmentId(Long departmentId);
    Page<UserBo> getUserBos(Page<UserBo> page, @Param("param")UserGetParam userGetParam);
    Integer updateUser(@Param("param") UserUpdateParam userUpdateParam);
    Integer upateUserState(Integer state, Long userId);
    Integer setRoleIdNull(Long userId);
}
