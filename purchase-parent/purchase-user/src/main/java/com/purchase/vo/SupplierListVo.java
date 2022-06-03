package com.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class SupplierListVo {
    private List<SupplierVo> supplierVos;
    private Integer total;
}
