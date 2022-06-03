package com.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseListVo {
    private List<PurchaseVo> purchaseVos;
    private Integer total;
}
