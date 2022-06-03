package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.service.impl.StatisticsService;
import com.purchase.vo.statistics.param.BaseParam;
import com.purchase.vo.statistics.vo.PurchaseCountVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@Api(tags = "统计控制器")
public class StatisticsController {
    @Resource
    private StatisticsService statisticsService;

    @PostMapping("/statistics/spendingTypePercentage")
    @ApiOperation("支出类型占比")
    @SaCheckPermission("statistics-pay")
    public RespResult spendingTypePercentage(@RequestBody @Valid BaseParam param) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, statisticsService.getSpendingTypePercentage(param));
    }

    @PostMapping("/statistics/applyCount")
    @ApiOperation("申请数量信息")
    @SaCheckPermission("statistics-pay")
    public RespResult applyCount(@RequestBody @Valid BaseParam param) {
        List<PurchaseCountVo> purchaseCountVos = null;
        if(param.getStep().equals("day")) {
            purchaseCountVos = statisticsService.getApplyCountVoByDay(param);
        }else if(param.getStep().equals("month")) {
            purchaseCountVos = statisticsService.getApplyCountVoByMonth(param);
        }
        else if(param.getStep().equals("year")) {
            purchaseCountVos = statisticsService.getApplyCountVoByYear(param);
        }
        return new RespResult(ResultState.SUCCESS_NO_MESS, purchaseCountVos);
    }

    @PostMapping("/statistics/purchaseCount")
    @ApiOperation("采购成功数量信息")
    @SaCheckPermission("statistics-pay")
    public RespResult purchaseCount(@RequestBody @Valid BaseParam param) {
        List<PurchaseCountVo> purchaseCountVos = null;
        if(param.getStep().equals("day")) {
            purchaseCountVos = statisticsService.getPurchaseCountVoByDay(param);
        }else if(param.getStep().equals("month")) {
            purchaseCountVos = statisticsService.getPurchaseCountVoByMonth(param);
        }
        else if(param.getStep().equals("year")) {
            purchaseCountVos = statisticsService.getPurchaseCountVoByYear(param);
        }
        return new RespResult(ResultState.SUCCESS_NO_MESS, purchaseCountVos);
    }

    @GetMapping("/statistics/headinfo")
    @ApiOperation("获取统计头部的信息")
    @SaCheckPermission("statistics-pay")
    public RespResult statisticsHeadinfo() {
        return new RespResult(ResultState.SUCCESS_NO_MESS, statisticsService.getHeadinfoVo());
    }

}
