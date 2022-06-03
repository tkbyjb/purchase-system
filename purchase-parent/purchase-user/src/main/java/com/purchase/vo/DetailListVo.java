package com.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class DetailListVo {
    private List<DetailVo> detailVos;
    private Integer total;
}
