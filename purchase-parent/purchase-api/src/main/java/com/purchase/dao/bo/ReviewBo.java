package com.purchase.dao.bo;

import com.purchase.dao.po.Apply;
import com.purchase.dao.po.Comb;
import com.purchase.dao.po.Review;
import com.purchase.dao.po.User;
import lombok.Data;

@Data
public class ReviewBo {
    private Long reviewId;
    private ApplyBo reviewApplyBo;
    private Review review;
    private User reviewUser;
    private CombBo reviewCombBo;
}
