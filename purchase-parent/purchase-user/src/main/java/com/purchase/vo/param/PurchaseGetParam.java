package com.purchase.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.purchase.global.OrderBy;
import com.purchase.global.PageParam;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseGetParam {
    private PageParam pageParam;
    private List<OrderBy> orderBys;
    private Integer state;
    private String serialNumber;
    private Integer purchaseWay;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long supplierId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long signUserId;
}
