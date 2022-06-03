package com.purchase.vo.process;

import lombok.Data;

import java.util.List;

@Data
public class SupplierPurchaseListVo {
    private List<SupplierPurchaseVo> supplierPurchaseVos;
    private Integer total;
}
