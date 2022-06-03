package com.purchase.vo.param.process;

import com.purchase.global.OrderBy;
import com.purchase.global.PageParam;
import lombok.Data;

import java.util.List;

@Data
public class SupplierPurchaseGetParam {
    private PageParam pageParam;
    private List<OrderBy> orderBys;
    private String serialNumber;
    private Integer state;
}
