package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.purchase.dao.po.User;
import com.purchase.global.PageParam;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.global.exception.UploadFileNotLegalException;
import com.purchase.service.ApplyService;
import com.purchase.vo.param.ApplyAddParam;
import com.purchase.vo.param.ApplyGetParam;
import com.purchase.vo.param.ApplyGetUnreviewParam;
import com.purchase.vo.param.ApplyUpdateParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;


@RestController
@Validated
@Api(tags = "申请控制器")
public class ApplyController {
    @Resource
    private ApplyService applyService;
    @Resource
    private RedisTemplate redisTemplate;

    //    @PostMapping("/apply/department/add")
//    public RespResult applyAdd(String applyname, @RequestParam(value = "note",required=false) String note, @RequestParam(value = "files",required=false) MultipartFile[] fileList) {
//        System.out.println(applyname);
//        System.out.println(note);
//        System.out.println(fileList[0].getOriginalFilename());
//        return null;
//    }
//    @PostMapping("/apply/department/add")
//    public RespResult applyAdd(@Valid ApplyAddParam applyAddParam, @RequestParam(value = "files", required = false) MultipartFile[] fileList) {
//        System.out.println(applyAddParam);
//        System.out.println(fileList[0].getOriginalFilename());
//        return null;
//    }
    @PostMapping("/apply/department/add")
    @SaCheckPermission("apply-add")
    @ApiOperation("添加一个申请")
    public RespResult applyAdd(@Valid ApplyAddParam applyAddParam) throws IOException, UploadFileNotLegalException {
        /**
         * 1.检查上传文件是否恶意文件
         * 2.生成当前时间_uuid文件名保存文件到上传用户目录
         * 3.添加一个申请记录
         */
        User user = (User) redisTemplate.opsForValue().get("userId_" + (String) StpUtil.getLoginId());
        if(applyService.addApply(applyAddParam, user)) {
            return new RespResult(ResultState.SUCCESS, "添加成功", null);
        }else {
            return new RespResult(ResultState.SERVER_ERROR, "添加失败");
        }
    }

    @PostMapping("/apply/get")
    @SaCheckPermission("apply-get")
    @ApiOperation("查看申请")
    public RespResult get(@RequestBody ApplyGetParam applyGetParam) {
        applyGetParam.setApplyUserId(Long.parseLong((String) StpUtil.getLoginId()));
        return new RespResult(ResultState.SUCCESS_NO_MESS, applyService.getApplyVo(applyGetParam));
    }

    @PostMapping("/apply/delete/applyIds")
    @SaCheckPermission("apply-delete")
    @ApiOperation("删除多个申请")
    public RespResult deletes(@RequestBody List<Long> applyIds) throws CusNoPermissionException {
        /**
         * 1检查申请是否处于不能修改状态,不能则不能删除,能则删除对应审核?->明细->申请
         */
        applyService.deletes(applyIds);
        return new RespResult(ResultState.SUCCESS);
    }

    @PostMapping("/apply/update/applyId")
    @SaCheckPermission("apply-update")
    @ApiOperation("根据applyId修改申请")
    public RespResult update(@Valid ApplyUpdateParam applyUpdateParam) throws UploadFileNotLegalException, CusNoPermissionException, IOException {
        /**
         * 1.检查申请是否能被编辑
         * 2检查是否修改附件和上传文件是否恶意文件
         * 3生成当前时间_uuid文件名保存文件到上传用户目录
         * 4更新字段,更新上次修改时间,检查是否推迟
         */
        User user = (User) redisTemplate.opsForValue().get("userId_" + (String) StpUtil.getLoginId());
        if(applyService.updateApplyPart(applyUpdateParam, user) > 0){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.SERVER_ERROR, "修改失败", null);
        }
    }

    @GetMapping("/apply/update/toReview")
    @ApiOperation("申请提交审核")
    @SaCheckPermission("apply-update")
    public RespResult toReview(@NotNull Long applyId,@NotNull Long assignUserId) throws CusNoPermissionException {
        /**
         * 1.检查此申请是否可编辑,明细是否为空,为空不能提交申请抛出无权限异常
         * 2.修改申请状态为2,申请时间,第一层审核人
         */
        if(applyService.updateToReview(applyId, assignUserId)){
            return new RespResult(ResultState.SUCCESS, "提交成功,请等待审核,审核进度可在时间线查看", null);
        }else{
            return new RespResult(ResultState.PARMA_ERROR, "申请明细为空不能提交审核,请添加明细", null);
        }

    }

    @PostMapping("/apply/get/unreview1")
    @ApiOperation("查询未进行第1层审核的申请")
    @SaCheckPermission("apply-unreview-1-get")
    public RespResult getUnreview1(@RequestBody @Valid ApplyGetUnreviewParam applyGetUnreviewParam) {
        /**
         * 获取状态为2,未删除,当前处理人是此用户的申请,只需要分页参数,排序参数
         */
        User user = (User) redisTemplate.opsForValue().get("userId_"+StpUtil.getLoginId());
        return new RespResult(ResultState.SUCCESS_NO_MESS, applyService.getApplyUnreviewListVo(applyGetUnreviewParam, 2, user.getUserId()));
    }

    @PostMapping("/apply/get/unreview2")
    @ApiOperation("查询未进行第2层审核的申请")
    @SaCheckPermission("apply-unreview-2-get")
    public RespResult getUnreview2(@RequestBody @Valid ApplyGetUnreviewParam applyGetUnreviewParam) {
        /**
         * 直接获取状态为1且未删除的申请,这里直接调用那个get方法,只需要分页参数,排序参数就行,然后还是在service做
         * 算了,还是单独写一个,不然搜索功能一变,这个也要变
         */
        User user = (User) redisTemplate.opsForValue().get("userId_"+StpUtil.getLoginId());
        return new RespResult(ResultState.SUCCESS_NO_MESS, applyService.getApplyUnreviewListVo(applyGetUnreviewParam, 3, user.getUserId()));
    }

    @PostMapping("/apply/get/unreview3")
    @ApiOperation("查询未进行第3层审核的申请")
    @SaCheckPermission("apply-unreview-3-get")
    public RespResult getUnreview3(@RequestBody @Valid ApplyGetUnreviewParam applyGetUnreviewParam) {
        User user = (User) redisTemplate.opsForValue().get("userId_"+StpUtil.getLoginId());
        return new RespResult(ResultState.SUCCESS_NO_MESS, applyService.getApplyUnreviewListVo(applyGetUnreviewParam, 4, user.getUserId()));
    }

    @GetMapping("/apply/get/reviewInfo1")
    @ApiOperation("审核时获取的申请的详细信息1")
    @SaCheckPermission("apply-review-1")
    public RespResult getReview1Info(@NotNull Long applyId) throws CusNoPermissionException {
        /**
         * 1检查此申请id状态是否是2
         * 2获取信息
         */
        return new RespResult(ResultState.SUCCESS_NO_MESS, applyService.getReviewInfoVo(applyId, 2));
    }

    @GetMapping("/apply/get/reviewInfo2")
    @ApiOperation("审核时获取的申请的详细信息2")
    @SaCheckPermission("apply-review-2")
    public RespResult getReview2Info(@NotNull Long applyId) throws CusNoPermissionException {
        return new RespResult(ResultState.SUCCESS_NO_MESS, applyService.getReviewInfoVo(applyId, 3));
    }


    @GetMapping("/apply/get/reviewInfo3")
    @ApiOperation("审核时获取的申请的详细信息3")
    @SaCheckPermission("apply-review-3")
    public RespResult getReview3Info(@NotNull Long applyId) throws CusNoPermissionException {
        return new RespResult(ResultState.SUCCESS_NO_MESS, applyService.getReviewInfoVo(applyId, 4));
    }

//    @GetMapping("/apply/get/applyInfo")//暂时不做
    @ApiOperation("获取明细id对应申请的详细信息")
    @SaCheckPermission("comb-get")
    public RespResult getDetailApplyInfo(Long detailId) {
        /**
         * 前边的只能获取指定状态的申请,这个是根据明细id获取申请详细信息
         */
        return new RespResult(ResultState.SUCCESS_NO_MESS, null);
    }

    @PostMapping("/apply/get/reviewd")
    @ApiOperation("获取通过三层审核的申请,给资产部看的")
    @SaCheckPermission("apply-get-reviewd")
    public RespResult getReviewdApply(@RequestBody @Valid ApplyGetParam param) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, applyService.getReviewdApplyVo(param));
    }
}
