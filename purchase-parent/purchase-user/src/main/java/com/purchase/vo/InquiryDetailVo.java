package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class InquiryDetailVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long inquiryId;
    private Double unitPrice;
    private Double totalPrice;


    private String detailname;
    private Integer count;
    private Double predictUnitPrice;
    private Double predictTotalPrice;
    private String unit;
    private String note;
}
