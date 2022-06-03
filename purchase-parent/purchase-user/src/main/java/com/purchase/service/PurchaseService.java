package com.purchase.service;

import com.purchase.dao.po.User;
import com.purchase.global.exception.ParamIllegalException;
import com.purchase.vo.PurchaseListVo;
import com.purchase.vo.param.ApplyGetUnreviewParam;
import com.purchase.vo.param.PurchaseGetParam;
import com.purchase.vo.param.process.Process1Param;
import com.purchase.vo.param.process.SupplierPurchaseGetParam;
import com.purchase.vo.process.SupplierPurchaseListVo;

public interface PurchaseService {
    Boolean addPurchase(Long combId, Long userId);
    PurchaseListVo getPurchaseListVo(PurchaseGetParam param);
    SupplierPurchaseListVo getSupplierPurchaseListVo(SupplierPurchaseGetParam param, Long supplierId);
    PurchaseListVo getUnpayPurchaseListVo(PurchaseGetParam param, Long userId);
}
