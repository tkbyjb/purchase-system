package com.purchase.vo.param;

import com.purchase.global.OrderBy;
import com.purchase.global.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CombGetParam {
    @NotNull
    private PageParam pageParam;
    private List<OrderBy> orderBys;
    private String startCreateTime;
    private String endCreateTime;
    private Integer state;
    private String combname;
    private String serialNumber;
    private Integer purchaseWay;
    private Long confirmUserId;
    private Long createUserId;
}
