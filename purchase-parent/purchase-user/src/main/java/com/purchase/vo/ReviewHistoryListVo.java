package com.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class ReviewHistoryListVo {
    private List<ReviewHistoryVo> reviewHistoryVos;
    private Integer total;
}
