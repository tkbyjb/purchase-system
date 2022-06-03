package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.service.RoleService;
import com.purchase.vo.param.system.RoleAddParam;
import com.purchase.vo.param.system.RoleGetParam;
import com.purchase.vo.param.system.RoleUpdateParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.LifecycleState;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "角色控制器")
@Validated
public class RoleController {
    @Resource
    private RoleService roleService;

    @GetMapping("/public/role/get/all")
    @ApiOperation(value = "获取所有角色id+角色名")
    public RespResult getAllRole() {
        return new RespResult(ResultState.SUCCESS_NO_MESS, roleService.getAllRoleVo());
    }

    @PostMapping("/role/get")
    @ApiOperation("获取角色信息")
    public RespResult getRoleinfo(@RequestBody @Valid RoleGetParam roleGetParam) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, roleService.getRoleinfos(roleGetParam));
    }


    @PostMapping("/role/update")
    @ApiOperation("修改角色")
    @SaCheckPermission("role-manage")
    public RespResult updateRole(@RequestBody @Valid RoleUpdateParam roleUpdateParam) {
        if (roleService.updateRole(roleUpdateParam)) {
            return new RespResult(ResultState.SUCCESS);
        } else {
            return new RespResult(ResultState.PARMA_ERROR, "角色名已经存在", null);
        }
    }

    @PostMapping("/role/add")
    @ApiOperation("添加角色")
    @SaCheckPermission("role-manage")
    public RespResult addRole(@RequestBody @Valid RoleAddParam roleAddParam) {
        if (roleService.addRole(roleAddParam)) {
            return new RespResult(ResultState.SUCCESS);
        } else {
            return new RespResult(ResultState.PARMA_ERROR, "角色名已经存在", null);
        }
    }

    @PostMapping("/role/deletes")
    @ApiOperation("删除角色")
    @SaCheckPermission("role-manage")
    public RespResult deleteRoles(@RequestBody List<Long> roleIds) {
        /**
         * 把对应角色的用户的state改为3未分配角色状态,再逻辑删除角色,role_menu直接删掉
         */
        if(roleService.deleteRoles(roleIds)) {
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.NO_PERMISSION, "角色有用户存在,不能直接删除", null);
        }
    }

}
