package com.purchase.vo.param;

import com.purchase.global.OrderBy;
import com.purchase.global.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ApplyGetUnreviewParam {
    @NotNull
    private PageParam pageParam;
    private List<OrderBy> orderBys;
}
