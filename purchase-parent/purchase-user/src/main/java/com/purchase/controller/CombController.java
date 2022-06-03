package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.purchase.dao.po.User;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.service.CombService;
import com.purchase.vo.param.CombAddParam;
import com.purchase.vo.param.CombGetParam;
import com.purchase.vo.param.CombUpdateParam;
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
import java.util.List;

@RestController
@Api("组合控制器")
@Validated
public class CombController {
    @Resource
    private CombService combService;
    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/comb/add")
    @ApiOperation("添加一个组合")
    @SaCheckPermission("comb-add")
    public RespResult addComb(@RequestBody @Valid CombAddParam combAddParam) {
        /**
         * 1.检查每个明细是否已经在一个组合了
         * 2.添加组合,明细组合关联
         */
        User user = (User) redisTemplate.opsForValue().get("userId_" + StpUtil.getLoginId());
        if (combService.addComb(combAddParam, user)) {
            return new RespResult(ResultState.SUCCESS);
        } else {
            return new RespResult(ResultState.PARMA_ERROR, "有明细已经分配组合,不可重复分配", null);
        }
    }

    @PostMapping("/comb/get")
    @ApiOperation("查询组合")
    @SaCheckPermission("comb-get")
    public RespResult getComb(@RequestBody @Valid CombGetParam combGetParam) {
        combGetParam.setCreateUserId(Long.parseLong(StpUtil.getLoginId()+""));
        return new RespResult(ResultState.SUCCESS_NO_MESS, combService.getCombVo(combGetParam));
    }

    @PostMapping("/comb/get/unreview")
    @ApiOperation("第四层审核查询,查询未审核的组合")
    @SaCheckPermission("comb-review")
    public RespResult getUnreivewComb(@RequestBody @Valid CombGetParam combGetParam) {
        User user = (User) redisTemplate.opsForValue().get("userId_"+StpUtil.getLoginId());
        combGetParam.setConfirmUserId(user.getUserId());
        return new RespResult(ResultState.SUCCESS_NO_MESS, combService.getUnreviewCombVo(combGetParam));
    }

    @GetMapping("/comb/toReview")
    @ApiOperation("组合提交给管理员审核")
    @SaCheckPermission("comb-get")
    public RespResult combToReview(@NotNull Long combId,@NotNull Long assignUserId) throws CusNoPermissionException {
        //要检查是否可编辑
        if(combService.updateCombToReview(combId, assignUserId)){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.SERVER_ERROR);
        }
    }

    @PostMapping("/comb/get/unconfirm")
    @ApiOperation("获取待申领的组合")
    @SaCheckPermission("comb-undeal-get")
    public RespResult getUnconfirmCombs(@RequestBody @Valid CombGetParam combGetParam) {
        User user = (User) redisTemplate.opsForValue().get("userId_"+StpUtil.getLoginId());
        combGetParam.setConfirmUserId(user.getUserId());
        return new RespResult(ResultState.SUCCESS_NO_MESS, combService.getUnconfirmCombVo(combGetParam));
    }

    @PostMapping("/comb/deletes")
    @ApiOperation("删除组合")
    @SaCheckPermission("comb-undeal-get")
    public RespResult deletes(@RequestBody List<Long> combIds) {
        /**
         *1.检查是否可以删除
         * 2.删除
         */
        if(combService.deletes(combIds)) {
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.NO_PERMISSION, "有组合不能处于能删除的状态", null);
        }
    }

    @PostMapping("/comb/update")
    @ApiOperation("修改组合")
    @SaCheckPermission("comb-undeal-get")
    public RespResult updateComb(@RequestBody @Valid CombUpdateParam param) {
        /**
         * 1.检查是否可以修改,明细不能为空
         * 2.修改
         */
        if(combService.updateComb(param)) {
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.NO_PERMISSION, "有组合不能处于能修改的状态", null);
        }
    }

}
