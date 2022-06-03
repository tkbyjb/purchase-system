package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import com.purchase.dao.po.Supplier;
import com.purchase.dao.po.User;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.global.exception.ParamIllegalException;
import com.purchase.service.PurchaseService;
import com.purchase.vo.param.ApplyGetUnreviewParam;
import com.purchase.vo.param.PurchaseGetParam;
import com.purchase.vo.param.process.Process1Param;
import com.purchase.vo.param.process.SupplierPurchaseGetParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@Api(tags = "采购控制器")
public class PurchaseController {
    @Resource
    private PurchaseService purchaseService;
    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/purchase/add")
    @ApiOperation("添加一条采购")
    @SaCheckPermission("purchase-add")
    public RespResult addPurchase(@NotNull Long combId) {
        /**
         * 1.添加一天采购
         * 2.修改组合状态为4
         * 4,修改对应明细state为3
         */
        purchaseService.addPurchase(combId, Long.parseLong(StpUtil.getLoginId()+""));
        return new RespResult(ResultState.SUCCESS);
    }

    @PostMapping("/purchase/get")
    @ApiOperation("获取采购信息")
    @SaCheckPermission(value = {"purchase-get"}, mode = SaMode.OR)//这个也没控制住能访问自己能访问的吗这个问题
    public RespResult getPurchase(@RequestBody @Valid PurchaseGetParam param) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, purchaseService.getPurchaseListVo(param));
    }



    @PostMapping("/supplier/get/purchase")
    @ApiOperation("供应商获取采购单")
    @SaCheckPermission("purchase-todo")
    public RespResult getSupplierPurchase(@RequestBody @Valid SupplierPurchaseGetParam param) {
        /**
         * 采购单当前供应商是它的采购单,组合,明细信息
         */
        return new RespResult(ResultState.SUCCESS_NO_MESS, purchaseService.getSupplierPurchaseListVo(param, Long.parseLong(StpUtil.getLoginId()+"")));
    }

    @PostMapping("/purchase/get/unpay")
    @ApiOperation("获取登录用户的待支付采购")
    @SaCheckPermission("pay")
    public RespResult getUnpayPurchase(@RequestBody PurchaseGetParam param) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, purchaseService.getUnpayPurchaseListVo(param, Long.parseLong(StpUtil.getLoginId()+"")));
    }
}
