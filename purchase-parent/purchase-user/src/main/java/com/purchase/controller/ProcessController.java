package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.global.exception.NoBalanceException;
import com.purchase.global.exception.ParamIllegalException;
import com.purchase.global.exception.UploadFileNotLegalException;
import com.purchase.service.ProcessService;
import com.purchase.vo.param.process.Process1Param;
import com.purchase.vo.param.process.Process2Param;
import com.purchase.vo.param.process.Process3Param;
import com.purchase.vo.param.process.Process4Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * 每一个流程都要检查state是否能做当前操作
 */
@RestController
@Api(tags = "采购流程控制器")
@Validated
public class ProcessController {
    @Resource
    private ProcessService processService;

    @PostMapping("/purchase/process/1")
    @ApiOperation("采购第1步:确认供应商")
    @SaCheckPermission("purchase-get")
    public RespResult process1(@RequestBody @Valid Process1Param param) throws ParamIllegalException {
        /**
         * 1.检查采购方式和供应商类型是否匹配
         * 2.检查当前是否是可重新选供应商(先前供应商未处理时不能再选供应商,只能等供应商处理或取消,可以打电话/系统管理员处理)
         * 3.修改数据库
         */
        Integer r = processService.process1(param);
        if (r == 1) {
            return new RespResult(ResultState.SUCCESS);
        } else if (r == 0) {
            return new RespResult(ResultState.NO_PERMISSION, "此采购当前不可修改供应商,请等待供应商处理", null);
        } else {
            return new RespResult(ResultState.NO_PERMISSION, "此供应商和采购要求的采购方式不匹配,请重新选择", null);
        }
    }

    @PostMapping("/purchase/process/2")
    @ApiOperation("采购第2步:供应商给出价格")
    @SaCheckPermission("purchase-todo")
    public RespResult process2(@RequestBody @Valid Process2Param param) throws ParamIllegalException {
        /**
         * 1.检查采购状态是否可进行当前操作
         * 2.修改明细列表的给价格
         * 3.修改采购的状态
         */
        Integer r = processService.process2(param);
        if (r == 1) {
            return new RespResult(ResultState.SUCCESS);
        } else {
            return new RespResult(ResultState.NO_PERMISSION, "此采购当前不在可给出价格状态", null);
        }
    }

    @PostMapping("/purchase/process/3")
    @ApiOperation("采购第3步:操作员确认供应商给出的价格")
    @SaCheckPermission("purchase-get")
    public RespResult process3(@RequestBody @Valid Process3Param param) throws ParamIllegalException {
        /**
         * 1.检查采购状态是否可进行当前操作
         * 2.修改明细列表的给价格
         * 3.修改采购的状态
         */
        Integer r = processService.process3(param);
        if (r == 1) {
            return new RespResult(ResultState.SUCCESS);
        } else {
            return new RespResult(ResultState.NO_PERMISSION, "此采购步不可编辑当前步骤", null);
        }
    }

    @PostMapping("/purchase/process/4")
    @ApiOperation("采购第4步:供应商上传合同给操作员")
    @SaCheckPermission("purchase-todo")
    public RespResult process4(@Valid Process4Param param) throws ParamIllegalException, IOException, UploadFileNotLegalException {
        /**
         * 1.检查采购状态是否可进行当前操作
         * 2.保存合同文件
         * 3.修改采购的状态
         */
        Integer r = processService.process4(param);
        if (r == 1) {
            return new RespResult(ResultState.SUCCESS);
        } else if (r == 0) {
            return new RespResult(ResultState.NO_PERMISSION, "此采购步不可编辑当前步骤", null);
        } else {
            return new RespResult(ResultState.PARMA_ERROR, "必须上传合同文件", null);
        }
    }

    @PostMapping("/purchase/process/5")
    @ApiOperation("采购第5步:操作员上传合同给供应商")
    @SaCheckPermission("purchase-get")
    public RespResult process5(@Valid Process4Param param) throws ParamIllegalException, IOException, UploadFileNotLegalException {
        /**
         * 1.检查采购状态是否可进行当前操作
         * 2.保存合同文件
         * 3.修改采购的状态
         */
        Integer r = processService.process5(param);
        if (r == 1) {
            return new RespResult(ResultState.SUCCESS);
        } else if (r == 0) {
            return new RespResult(ResultState.NO_PERMISSION, "此采购步不可编辑当前步骤", null);
        } else {
            return new RespResult(ResultState.PARMA_ERROR, "必须上传合同文件", null);
        }
    }

    @GetMapping("/purchase/process/6")
    @ApiOperation("采购第6步:供应商确认发货")
    @SaCheckPermission("purchase-todo")
    public RespResult process6(@NotNull Long purchaseId) throws ParamIllegalException, IOException, UploadFileNotLegalException {
        /**
         * 1.检查采购状态是否可进行当前操作
         * 2.保存合同文件
         * 3.修改采购的状态
         */
        Integer r = processService.process6(purchaseId);
        if (r == 1) {
            return new RespResult(ResultState.SUCCESS);
        } else if (r == 0) {
            return new RespResult(ResultState.NO_PERMISSION, "此采购步不可编辑当前步骤", null);
        } else {
            return new RespResult(ResultState.PARMA_ERROR);
        }
    }

    @GetMapping("/purchase/process/7")
    @ApiOperation("采购第7步:操作员确认收货")
    @SaCheckPermission("purchase-get")
    public RespResult process7(@NotNull Long purchaseId, @NotNull Long payUserId) throws ParamIllegalException, IOException, UploadFileNotLegalException {
        /**
         * 1.检查采购状态是否可进行当前操作
         * 2.保存合同文件
         * 3.修改采购的状态(资产部的人会收到待支付账单和合同下载链接,确认收货时需要选择资产部支付的人)
         */
        Integer r = processService.process7(purchaseId, payUserId);
        if (r == 1) {
            return new RespResult(ResultState.SUCCESS);
        } else if (r == 0) {
            return new RespResult(ResultState.NO_PERMISSION, "此采购步不可编辑当前步骤", null);
        } else {
            return new RespResult(ResultState.PARMA_ERROR);
        }
    }


    @GetMapping("/purchase/process/8")
    @ApiOperation("采购第8步:财务部付款(本来应该系统自动生成账单的,只是这里很简略,没做)")
    @SaCheckPermission("pay")
    public RespResult process8(@NotNull Long purchaseId) throws NoBalanceException {
        /**
         * 1.检查采购状态是否可进行当前操作
         * 2.保存合同文件
         * 3.修改采购的状态
         * 4.添加支付数据
         */
        Integer r = processService.process8(purchaseId, Long.parseLong(StpUtil.getLoginId() + ""));
        if (r == 1) {
            return new RespResult(ResultState.SUCCESS);
        } else if (r == 0) {
            return new RespResult(ResultState.NO_PERMISSION, "此采购步不可编辑当前步骤", null);
        } else if (r == -1) {
            return new RespResult(ResultState.NO_PERMISSION, "没有权限支付此订单", null);
        } else {
            return new RespResult(ResultState.NO_PERMISSION, "没有权限支付此订单", null);
        }
    }
}
