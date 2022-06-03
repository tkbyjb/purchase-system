package com.purchase.global;

import lombok.Data;


@Data
public class PageParam {
    private int pageIndex = 1; //默认为第一页
    private int pageSize = 5;  //默认取5条数据
}
