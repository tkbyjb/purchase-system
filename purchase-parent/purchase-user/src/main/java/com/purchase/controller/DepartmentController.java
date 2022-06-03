package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.service.DepartmentService;
import com.purchase.vo.DepartmentVo;
import com.purchase.vo.param.DepartmentAddParam;
import com.purchase.vo.param.DepartmentBalanceGetParam;
import com.purchase.vo.param.DepartmentGetParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@RestController
//@RequestMapping("/department")
@Api(tags = "部门控制器")
@Validated
public class DepartmentController {
    @Resource
    private DepartmentService departmentService;

    @GetMapping("/public/department/get/all/name")
    @ApiOperation("获取所有部门id+部门名")
    public RespResult getAllName() {
        return new RespResult(ResultState.SUCCESS_NO_MESS, departmentService.getAllName());
    }

    @PostMapping("/department/get/all")
    @ApiOperation("管理员获取部门信息")
    @SaCheckPermission("department-manage")
    public RespResult get(@RequestBody @Valid DepartmentGetParam departmentGetParam) {
        System.out.println("**********"+departmentGetParam);
        return new RespResult(ResultState.SUCCESS_NO_MESS, departmentService.getAll(departmentGetParam));
    }

    @PostMapping("/department/update")
    @ApiOperation("修改部门信息")
    @SaCheckPermission("department-manage")
    public RespResult update(@RequestBody @Valid DepartmentVo departmentVo) {
        /**
         * 1.检查部门名是否已经存在2.修改
         */
        if(departmentService.updateDepartment(departmentVo)){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.PARMA_ERROR, "部门名已存在", null);
        }
    }

    @PostMapping("/department/deletes")
    @ApiOperation("删除多个部门")
    @SaCheckPermission("department-manage")
    public RespResult deletes(@RequestBody @NotNull List<Long> departmentIds) {
        /**
         * 1.把对应部门的用户的所属部门id改为null2.修改删除标记
         */
        departmentService.deletes(departmentIds);
        return new RespResult(ResultState.SUCCESS);
    }
    
    @PostMapping("/department/add")
    @ApiOperation("添加部门")
    @SaCheckPermission("department-manage")
    public RespResult add(@RequestBody @Valid DepartmentAddParam departmentAddParam) throws Exception {
        /**
         * 1.部门名是否重复2.添加
         */
        if(departmentService.addDepartment(departmentAddParam)){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.PARMA_ERROR, "部门名已经存在", null);
        }
    }

    @PostMapping("/department/balance")
    @ApiOperation("获取学院剩余经费")
    @SaCheckPermission("department-balance")
    public RespResult getBalances(@RequestBody @Valid DepartmentBalanceGetParam departmentGetParam) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, departmentService.getDepartmentBalanceListVo(departmentGetParam));
    }

}
