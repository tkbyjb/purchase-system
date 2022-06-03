package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import com.purchase.dao.po.User;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.service.ReviewService;
import com.purchase.vo.param.Review3Param;
import com.purchase.vo.param.ReviewCombParam;
import com.purchase.vo.param.ReviewHistoryGetParam;
import com.purchase.vo.param.ReviewParam;
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

@RestController
@Validated
@Api("审核控制器")
public class ReviewController {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private ReviewService reviewService;

    @PostMapping("/review/add/1")
    @ApiOperation("添加第一层审核")
    @SaCheckPermission({"apply-review-1"})
    public RespResult reviewAdd1(@RequestBody @Valid ReviewParam reviewParam) {
        /**
         * 1.获取用户id
         * 2.检查是否重复审核了
         * 3.添加审核记录,根据是否通过修改申请的state字段,下一个处理人id
         * 4.如果是打回,需要修改之前的审核状态为无效
         */
        User user = (User) redisTemplate.opsForValue().get("userId_" + StpUtil.getLoginId());
        Integer state = 3;
        if(reviewParam.getResult() == 0){
            state = 20;
        }
        if(reviewService.addReview(reviewParam, user.getUserId(), state, 1)){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.NO_PERMISSION, "此申请已审核过,请勿重复审核", null);
        }
    }

    @PostMapping("/review/add/2")
    @ApiOperation("添加第2层审核")
    @SaCheckPermission({"apply-review-2"})
    public RespResult reviewAdd2(@RequestBody @Valid ReviewParam reviewParam) {
        /**
         * 1.获取用户id,检查是否重复审核了2.添加审核记录,根据是否通过修改申请的当前state字段3.如果是打回,需要修改之前的审核状态为无效
         * 这里还要考虑一下不止简单判断isdelete,还有state,因为审核人能看到失效的审核记录,而申请只认这一轮的
         * 不通过,状态直接打回20,通过的才判断该是哪个状态,这个通过最新审核来确定如何更新state,不通过角色,因为角色可以添加,一个角色也可以有多层审核权限
         * 算了,要不前端传当前页面对应的state算了,因为一个页面只对应一个state
         */
        User user = (User) redisTemplate.opsForValue().get("userId_" + StpUtil.getLoginId());
        Integer state = 4;
        if(reviewParam.getResult() == 0){
            state = 20;
        }
        if(reviewService.addReview(reviewParam, user.getUserId(), state, 2)){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.NO_PERMISSION, "此申请已审核过,请勿重复审核", null);
        }
    }

    @PostMapping("/review/add/3")
    @ApiOperation("添加第3层审核,指定采购方式")
    @SaCheckPermission({"apply-review-3"})
    public RespResult reviewAdd3(@RequestBody @Valid Review3Param reviewParam) {
        /**
         * 1.检查是否已经审核过
         * 2.状态为不通过时修改之前的审核状态为0,无效
         * 3.添加审核记录4.修改明细的采购方式5.修改申请的state
         */
        User user = (User) redisTemplate.opsForValue().get("userId_" + StpUtil.getLoginId());
        Integer state = 5;
        if(reviewParam.getResult() == 0){
            state = 20;
        }
        if(reviewService.addReview3(reviewParam, user.getUserId(), state)){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.NO_PERMISSION, "此申请已审核过,请勿重复审核", null);
        }
    }

    @PostMapping("/review/add/4")
    @ApiOperation("添加第4层组合审核")
    @SaCheckPermission({"comb-review"})
    public RespResult reviewAdd4(@RequestBody @Valid ReviewCombParam reviewParam) {
        /**
         * 1.检查组合是否已经审核过
         * 2.状态为不通过时修改之前的审核状态改为10
         * 3.添加审核记录
         * 4.修改组合的state,操作员id
         */
        User user = (User) redisTemplate.opsForValue().get("userId_" + StpUtil.getLoginId());
        Integer state = 3;//待操作员申领
        if(reviewParam.getResult() == 0){
            state = 10;
        }
        if(reviewService.addReview4(reviewParam, user.getUserId(), state)){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.NO_PERMISSION, "此申请已审核过,请勿重复审核", null);
        }
    }

    @PostMapping("/review/history/get/1")
    @ApiOperation("获取一个用户的第1层审核历史记录")
    @SaCheckPermission(value = {"apply-review-1-get"}, mode = SaMode.OR)
    public RespResult getReviewHistory1(@RequestBody @Valid ReviewHistoryGetParam reviewHistoryGetParam) {
        /**
         * 能查出无效的审核
         */
        User user = (User) redisTemplate.opsForValue().get("userId_" + StpUtil.getLoginId());
        reviewHistoryGetParam.setReviewType(1);
        return new RespResult(ResultState.SUCCESS_NO_MESS, reviewService.getReviewHistoryListVo(reviewHistoryGetParam, user.getUserId()));
    }

    @PostMapping("/review/history/get/2")
    @ApiOperation("获取一个用户的第2层审核历史记录")
    @SaCheckPermission(value = {"apply-review-2-get"}, mode = SaMode.OR)
    public RespResult getReviewHistory2(@RequestBody @Valid ReviewHistoryGetParam reviewHistoryGetParam) {
        User user = (User) redisTemplate.opsForValue().get("userId_" + StpUtil.getLoginId());
        reviewHistoryGetParam.setReviewType(2);
        return new RespResult(ResultState.SUCCESS_NO_MESS, reviewService.getReviewHistoryListVo(reviewHistoryGetParam, user.getUserId()));
    }

    @PostMapping("/review/history/get/3")
    @ApiOperation("获取一个用户的第3层审核历史记录")
    @SaCheckPermission(value = {"apply-review-3-get"}, mode = SaMode.OR)
    public RespResult getReviewHistory3(@RequestBody @Valid ReviewHistoryGetParam reviewHistoryGetParam) {
        User user = (User) redisTemplate.opsForValue().get("userId_" + StpUtil.getLoginId());
        reviewHistoryGetParam.setReviewType(3);
        return new RespResult(ResultState.SUCCESS_NO_MESS, reviewService.getReviewHistoryListVo(reviewHistoryGetParam, user.getUserId()));
    }

    @PostMapping("/review/history/get/4")
    @ApiOperation("获取一个用户的第4层审核历史记录")
    @SaCheckPermission(value = {"comb-review-get"}, mode = SaMode.OR)
    public RespResult getReviewHistory4(@RequestBody @Valid ReviewHistoryGetParam reviewHistoryGetParam) {
        User user = (User) redisTemplate.opsForValue().get("userId_" + StpUtil.getLoginId());
        reviewHistoryGetParam.setReviewType(4);
        return new RespResult(ResultState.SUCCESS_NO_MESS, reviewService.getCombReviewHistoryListVo(reviewHistoryGetParam, user.getUserId()));
    }


    @GetMapping("/review/get/returnOpinion")
    @ApiOperation("获取被打回审核的最新不通过审核意见")
    @SaCheckPermission("apply-get")
    public RespResult getReturnOpinion(Long applyId) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, reviewService.getReturnOpinion(applyId));
    }
}
