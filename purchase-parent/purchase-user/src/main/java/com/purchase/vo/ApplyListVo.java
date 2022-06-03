package com.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class ApplyListVo {
    private List<ApplyVo> applyVos;
    private Integer total;
}
