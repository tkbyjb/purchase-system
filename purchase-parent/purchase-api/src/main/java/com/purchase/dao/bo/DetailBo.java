package com.purchase.dao.bo;

import com.purchase.dao.po.Detail;
import com.purchase.dao.po.SpendingType;
import lombok.Data;

@Data
public class DetailBo {
    private Long detailId;
    private Detail detail;
    private SpendingType spendingType;
}
