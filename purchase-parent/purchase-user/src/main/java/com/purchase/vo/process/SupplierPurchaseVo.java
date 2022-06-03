package com.purchase.vo.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 给供应商看的采购单信息
 */
@Data
public class SupplierPurchaseVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long purchaseId;
    private String serialNumber;
    private Integer purchaseWay;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastUpdateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long combId;
    private Integer state;

    private String operatorUsername;
    private String operatorUserTel;
    private String combname;
}
