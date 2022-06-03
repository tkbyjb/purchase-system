package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.global.exception.ParamIllegalException;
import com.purchase.service.DetailService;
import com.purchase.vo.DetailUpdateVo;
import com.purchase.vo.param.DetailAddParam;
import com.purchase.vo.param.DetailUncombGetParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Api(tags = "明细控制器")
@Validated
public class DetailController {
    @Resource
    private DetailService detailService;

    @GetMapping("/detail/get/applyId")
    @ApiOperation("获取一个申请的所有明细")
//    @SaCheckPermission(value = {"detail-get", "apply-get-reviewd"}, mode = SaMode.OR)
    //这里设计得不好,没限制住
    public RespResult getDetailByApplyId(@RequestParam(value = "applyId", required = true) String applyId) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, detailService.getDetailVoByApplyId(Long.parseLong(applyId)));
    }

    @PostMapping("/detail/add")
    @ApiOperation("添加一个明细")
    @SaCheckPermission("detail-add")
    public RespResult addDetail(@RequestBody @Valid DetailAddParam detailAddParam) {
        if(detailService.addDetail(detailAddParam)){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.SERVER_ERROR, "添加失败");
        }
    }

    @GetMapping("/detail/delete/detailId")
    @ApiOperation("根据明细id删除一个明细")
    @SaCheckPermission("detail-delete")
    public RespResult deleteByDetailId(@NotNull Long detailId) throws ParamIllegalException, CusNoPermissionException {
        /**
         * 1.检查明细对应的申请是否可编辑,这里顺便可以检查明细是否存在(因为要查出applyid所以顺便检查一下)
         * 2申请可编辑则删除明细
         */
        if(detailService.deleteByDetailId(detailId) > 0){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.SERVER_ERROR, "删除失败", null);
        }
    }

    @GetMapping("/detail/get/detailId")
    @ApiOperation("根据明细id获取一个明细")
    @SaCheckPermission("detail-update")
    public RespResult getByDetailId(@NotNull Long detailId) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, detailService.getByDetailId(detailId));
    }

    @PostMapping("/detail/update")
    @ApiOperation("修改一个明细")
    @SaCheckPermission("detail-update")
    public RespResult updateDetail(@RequestBody @Valid DetailUpdateVo detailUpdateVo) throws ParamIllegalException, CusNoPermissionException {
        /**
         * 1.对应申请是否可编辑,顺便是否存在
         * 2.修改
         */
        if(detailService.updateDetail(detailUpdateVo) > 0){
            return new RespResult(ResultState.SUCCESS);
        }else{
            return new RespResult(ResultState.SERVER_ERROR, "修改失败", null);
        }
    }

    @PostMapping("/detail/get/uncomb")
    @ApiOperation("获取未分配组合的明细")
    @SaCheckPermission("comb-get")
    public RespResult getUncomb(@RequestBody @Valid DetailUncombGetParam detailUncombGetParam) {
        /**
         * 只能查到对应申请为今年的
         */
        return new RespResult(ResultState.SUCCESS_NO_MESS, detailService.getUncombDetails(detailUncombGetParam));
    }

    @GetMapping("/detail/get/combId")
    @ApiOperation("获取一个组合的明细")
//    @SaCheckPermission(value = {"comb-get","purchase-get", "purchase-todo", "pay"}, mode = SaMode.OR)
    public RespResult getCombDetails(@NotNull Long combId) {
        System.out.println(combId);
        return new RespResult(ResultState.SUCCESS_NO_MESS, detailService.getCombDetails(combId));
    }
}
