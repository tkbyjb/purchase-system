package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.purchase.dao.po.User;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.global.exception.UploadFileNotLegalException;
import com.purchase.service.UserService;
import com.purchase.vo.param.system.UserAddParam;
import com.purchase.vo.param.system.UserGetParam;
import com.purchase.vo.param.system.UserUpdateParam;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.List;

@RestController
@Api("用户控制器")
@Validated
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/user/get")
    @ApiOperation("查询用户")
    @SaCheckPermission("user-manage")
    public RespResult getUsers(@RequestBody @Valid UserGetParam userGetParam) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, userService.getUserBos(userGetParam));
    }

    @PostMapping("/user/update")
    @ApiOperation("修改用户")
    @SaCheckPermission("user-manage")
    public RespResult updateUser(@Valid UserUpdateParam userUpdateParam) throws IOException, UploadFileNotLegalException {
        /**
         *修改了头像则修改信息并且删除之前的头像(发给消息队列慢慢做)
         */
        if(userService.updateUser(userUpdateParam)) {
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.SERVER_ERROR,"修改失败", null);//但其实可能是用户id不存在,算了,这里乱判断
        }
    }

    @PostMapping("/user/add")
    @ApiOperation("管理员添加用户")
    @SaCheckPermission("user-manage")
    public RespResult addUser(@Valid UserAddParam userAddParam) throws IOException, UploadFileNotLegalException {
        /**
         *1.用户名,邮箱是否重复
         */
        if(userService.isExistEmail(userAddParam.getEmail()) != null) {
            return new RespResult(ResultState.PARMA_ERROR,"邮箱已注册", null);
        }
        if(userService.isExistUsername(userAddParam.getUsername()) != null) {
            return new RespResult(ResultState.PARMA_ERROR,"用户名已经存在", null);
        }
        User user = (User) redisTemplate.opsForValue().get("userId_" + StpUtil.getLoginId());
        if(userService.addUserBySystem(userAddParam, user)) {
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.SERVER_ERROR,"添加失败", null);//但其实可能是用户id不存在,算了,这里乱判断
        }
    }

    @PostMapping("/user/deletes")
    @ApiOperation("删除多个用户")
    @SaCheckPermission("user-manage")
    public RespResult deletes(@RequestBody @NotNull List<Long> userIds) {
        userService.deleteUsers(userIds);
        return new RespResult(ResultState.SUCCESS);
    }

    @GetMapping("/user/review")
    @ApiOperation("审核用户账号")
    @SaCheckPermission("user-manage")
    public RespResult userReview(@NotNull @Max(2) @Min(0) Integer state, @NotNull Long userId) {
        if(userService.reviewUser(state, userId)){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.NO_PERMISSION, "审核失败", null);
        }
    }

    @GetMapping("/user/get/assign")
    @ApiOperation("获取能进行level层审核的用户")
//    @SaCheckPermission("")
    public RespResult getAssginUser(@NotNull @Min(1) @Max(5) Integer level) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, userService.getUserAssignVo(level));
    }

    @GetMapping("/user/get/pay")
    @ApiOperation("获取可以支付的人列表")
    public RespResult getPayUser() {
        return new RespResult(ResultState.SUCCESS_NO_MESS, userService.getPayUsers());
    }
}
