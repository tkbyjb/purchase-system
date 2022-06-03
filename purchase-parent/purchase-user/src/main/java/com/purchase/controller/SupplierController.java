package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.service.SupplierService;
import com.purchase.vo.param.SupplierGetParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Validated
@Api(tags = "供应商控制器")
public class SupplierController {
    @Resource
    private SupplierService supplierService;

    @PostMapping("/supplier/get")
    @ApiOperation("获取供应商信息")
    @SaCheckPermission("supplier-get")
    public RespResult getSuppliers(@RequestBody @Valid SupplierGetParam param) {
        /**
         * 获取正常的供应商
         */
        param.setState(2);
        return new RespResult(ResultState.SUCCESS_NO_MESS, supplierService.getSupplierListVo(param));
    }
}
