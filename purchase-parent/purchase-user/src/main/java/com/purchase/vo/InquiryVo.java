package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InquiryVo {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long purchaseId;

    private String combname;
    private String operatorUsername;//发起询价的操作员真名
    private String tel;//发起询价的操作员电话
    private Integer purchaseWay;//采购方式
    private Integer state;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long combId;
}
