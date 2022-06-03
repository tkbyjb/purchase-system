package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api("权限菜单控制器")
@Validated
public class MenuController {
    @Resource
    private MenuService menuService;

    @GetMapping("/menu/get/all/tree")
    @ApiOperation("获取权限tree")
    @SaCheckPermission("role-manage")
    public RespResult getMenuTree() {
        return new RespResult(ResultState.SUCCESS_NO_MESS, menuService.getAllMenuManaVos(null));
    }
}
