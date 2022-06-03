package com.purchase.dao.bo;

import com.purchase.dao.po.Spending;
import com.purchase.dao.po.SpendingType;
import lombok.Data;

@Data
public class SpendingBo {
    private Long spendingId;
    private Spending spending;
    private SpendingType spendingType;
    private Long spendingTypeId;
    private Double amount;
}
