package com.purchase.dao.bo;

import com.purchase.dao.po.Comb;
import com.purchase.dao.po.Purchase;
import com.purchase.dao.po.Supplier;
import com.purchase.dao.po.User;
import lombok.Data;

@Data
public class PurchaseBo {
    private Long purchaseId;
    private Purchase purchase;
    private User operatorUser;
    private User signUser;
    private Supplier supplier;
    private Comb comb;
}
