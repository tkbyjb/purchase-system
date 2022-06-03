package com.purchase.vo;

import com.purchase.dao.po.Inquiry;
import lombok.Data;

import java.util.List;

@Data
public class InquiryListVo {
    private List<InquiryVo> inquiryVos;
    private Integer total;
}
