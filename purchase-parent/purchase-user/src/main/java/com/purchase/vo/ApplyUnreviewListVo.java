package com.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class ApplyUnreviewListVo {
    private List<ApplyUnreviewVo> applyUnreviewVos;
    private Integer total;
}
