package com.purchase.vo.param;

import com.purchase.global.OrderBy;
import com.purchase.global.PageParam;
import lombok.Data;

import java.util.List;

@Data
public class InquiryGetParam {
    private PageParam pageParam;
    private List<OrderBy> orderBys;
    private Integer state;
    private Long supplierId;
}
