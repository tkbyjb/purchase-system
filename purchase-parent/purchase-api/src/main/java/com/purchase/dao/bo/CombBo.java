package com.purchase.dao.bo;

import com.purchase.dao.po.Comb;
import com.purchase.dao.po.User;
import lombok.Data;

@Data
public class CombBo {
    private Long combId;
    private Comb comb;
    private User createUser;
    private User confirmUser;
}
