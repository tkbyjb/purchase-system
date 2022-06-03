package com.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class CombListVo {
    private List<CombVo> combVos;
    private Integer total;
}
