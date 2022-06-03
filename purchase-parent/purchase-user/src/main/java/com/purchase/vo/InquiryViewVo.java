package com.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class InquiryViewVo {
    private String suppliername;
    List<InquiryDetailVo> inquiryDetailVos;
}
