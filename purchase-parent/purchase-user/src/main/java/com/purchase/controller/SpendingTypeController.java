package com.purchase.controller;

import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.service.SpendingTypeService;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "支出类型控制器")
@Validated
public class SpendingTypeController {
    @Resource
    private SpendingTypeService spendingTypeService;

    @GetMapping("/spendingType/get/all")
    public RespResult getAll() {
        return new RespResult(ResultState.SUCCESS_NO_MESS, spendingTypeService.getAll());
    }
}
