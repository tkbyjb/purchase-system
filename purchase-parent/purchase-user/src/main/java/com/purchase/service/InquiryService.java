package com.purchase.service;

import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.vo.InquiryDetailVo;
import com.purchase.vo.InquiryListVo;
import com.purchase.vo.InquiryViewVo;
import com.purchase.vo.param.InquiryGetParam;
import com.purchase.vo.param.process.InquiryPriceParam;
import com.purchase.vo.process.SupplierPurchaseListVo;

import java.util.List;

public interface InquiryService {
    Integer addInquiryByPruchase(Long purchaseId, Long supplierId);
    InquiryListVo getInquiryListVo(InquiryGetParam param);
    InquiryListVo getInquiryListVo2(InquiryGetParam param);
    List<InquiryDetailVo> getInquiryDetailVo(Long purchaseId, Long supplierId);
    Boolean setInquiryPrice(List<InquiryPriceParam> params) throws CusNoPermissionException;
    List<InquiryViewVo> getInquirysByPurchase(Long purchaseId);
}
