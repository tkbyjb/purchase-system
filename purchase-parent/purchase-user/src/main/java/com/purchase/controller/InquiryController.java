package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.service.InquiryService;
import com.purchase.vo.param.InquiryGetParam;
import com.purchase.vo.param.process.InquiryPriceParam;
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
import java.util.List;

@RestController
@Validated
@Api(tags = "询价控制器")
public class InquiryController {
    @Resource
    private InquiryService inquiryService;

    @GetMapping("/inquiry/add")
    @ApiOperation("添加一个采购的多个询价数据")
    @SaCheckPermission("inquiry-add")
    public RespResult addInquiry(@NotNull Long purchaseId, @NotNull Long supplierId) {
        Integer r = inquiryService.addInquiryByPruchase(purchaseId, supplierId);
        if(r==1){
            return new RespResult(ResultState.SUCCESS, "询价成功,请等待供应商给出价格");
        }else if(r==0){
            return new RespResult(ResultState.NO_PERMISSION, "此采购当前不可询价", null);
        }else if(r==-1){
            return new RespResult(ResultState.NO_PERMISSION, "此采购已询价过此供应商,不可重复询价", null);
        }else{
            return new RespResult(ResultState.SERVER_ERROR);
        }
    }

    @PostMapping("/inquiry/get/supplier")
    @ApiOperation("供应商获取自己的待询价采购单")
    @SaCheckPermission("purchase-inquiry")
    public RespResult getInquirySupplier(@RequestBody @Valid InquiryGetParam param) {
        param.setSupplierId(Long.parseLong(StpUtil.getLoginId()+""));
        return new RespResult(ResultState.SUCCESS_NO_MESS, inquiryService.getInquiryListVo2(param));
    }

    @GetMapping("/inquiry/get/detail")
    @ApiOperation("供应商获取询价明细详细信息")
    @SaCheckPermission("purchase-inquiry")
    public RespResult getInquiryDetail(@NotNull Long purchaseId) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, inquiryService.getInquiryDetailVo(purchaseId, Long.parseLong(StpUtil.getLoginId()+"")));
    }

    @PostMapping("/inquiry/set/price")
    @ApiOperation("供应商给出询价")
    @SaCheckPermission("purchase-inquiry")
    public RespResult toPrice(@RequestBody List<InquiryPriceParam> params) throws CusNoPermissionException {
        inquiryService.setInquiryPrice(params);
        return new RespResult(ResultState.SUCCESS);
    }

    @GetMapping("/inquiry/get/purchase")
    @ApiOperation("获取一个采购单的所有询价,按供应商分组")
    @SaCheckPermission("inquiry-add")
    public  RespResult getInquirysPurchase(Long purchaseId) {
        return new RespResult(ResultState.SUCCESS_NO_MESS, inquiryService.getInquirysByPurchase(purchaseId));
    }
}
