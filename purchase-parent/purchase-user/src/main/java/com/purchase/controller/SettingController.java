package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.service.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RestController
@Validated
@Api(tags = "设置控制器")
public class SettingController {
    @Resource
    private SettingService settingService;

    @GetMapping("/setting/set/deadline")
    @ApiOperation("设置今年截止采购的时间")
    @SaCheckPermission("setting-deadline")
    public RespResult setDeadline(@NotNull String deadline) {
        settingService.setDeadline(deadline);
        return new RespResult(ResultState.SUCCESS);
    }

    @GetMapping("/setting/get/deadline")
    @ApiOperation("获取截止时间")
    @SaCheckPermission("setting-deadline")
    public RespResult getDeadline() {
        return new RespResult(ResultState.SUCCESS_NO_MESS, settingService.getDeadlineString());
    }
}
