package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class DetailVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long detailId;
    private String serialNumber;
    private String detailname;
    private Integer count;
    private Double predictUnitPrice;
    private Double predictTotalPrice;
    private Double dealUnitPrice;
    private Double dealTotalPrice;
    private String unit;
    private Integer purchaseWay;
    private String note;
    private Integer state;
    private String spendingType;
}
