package com.purchase.vo;

import lombok.Data;

import java.util.List;

/**
 * 用于审核详情的数据，申请+明细+之前的审核信息
 */
@Data
public class ReviewInfoVo {
    private ApplyUnreviewVo applyUnreviewVo; //申请信息
    private List<DetailUnreviewVo> detailUnreviewVos;  //申请的明细信息
    private List<ReviewUnreviewVo> reviewUnreviewVos;  //前面的审核信息
}
