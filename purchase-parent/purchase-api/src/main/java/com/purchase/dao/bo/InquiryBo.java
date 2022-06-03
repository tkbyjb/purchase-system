package com.purchase.dao.bo;

import com.purchase.dao.po.Detail;
import com.purchase.dao.po.Inquiry;
import com.purchase.dao.po.Purchase;
import com.purchase.dao.po.Supplier;
import lombok.Data;

@Data
public class InquiryBo {
    private Long inquiryId;
    private Inquiry inquiry;
    private Purchase purchase;
    private Supplier supplier;
    private Detail detail;
}
