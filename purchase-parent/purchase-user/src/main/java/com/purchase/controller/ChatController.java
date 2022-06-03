package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.service.impl.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@Api(tags = "聊天控制器")
public class ChatController {
    @Resource
    private ChatService chatService;

    @GetMapping("/chat/get/left")
    @ApiOperation("获取登录用户的聊天左侧栏的用户信息")
    @SaCheckLogin
    public RespResult getUser() {
        return new RespResult(ResultState.SUCCESS_NO_MESS, chatService.getChatLeftVo(Long.parseLong(StpUtil.getLoginId()+"")));
    }

    @GetMapping("/chat/get/messs")
    @ApiOperation("获取右侧聊天信息并设置此发送用户的消息全部为已读")
    @SaCheckLogin
    public RespResult getMesss(@NotNull Long sendUserId) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, chatService.getMesss(sendUserId, Long.parseLong(StpUtil.getLoginId()+"")));
    }
}
