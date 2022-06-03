package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.purchase.dao.mapper.PurchaseMapper;
import com.purchase.dao.po.Purchase;
import com.purchase.global.DefaultData;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@Api(tags = "文件相关的控制器")
public class FileController {
    @Resource
    private PurchaseMapper purchaseMapper;
    @Resource
    private DefaultData defaultData;

    @GetMapping("/file/get/contractUrl")
    @ApiOperation("获取合同的下载路径")
//    @SaCheckPermission("")
    public RespResult getContractUrl(@NotNull Long purchaseId) {
        String url = null;
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        if (purchase.getContract() != null && !purchase.getContract().equals("")) {
            url = defaultData.CONTRACT_ACCESS_BASE_URL+ "/" + purchase.getPurchaseId() +"/" + purchase.getContract();
        }
        return new RespResult(ResultState.SUCCESS_NO_MESS, url);
    }
}
