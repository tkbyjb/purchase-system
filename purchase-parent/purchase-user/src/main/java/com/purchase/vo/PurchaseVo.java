package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchaseVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long purchaseId;
    private Integer state;
    private String serialNumber;
    private String suppliername;
    private String contract;
    private String operatorUsername;
    private String signUsername;
    private Integer purchaseWay;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    private Double dealTotalPrice;
    private CombVo combVo;
}
