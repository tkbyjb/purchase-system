package com.purchase.vo.param;

import com.purchase.global.OrderBy;
import com.purchase.global.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SupplierGetParam {
    private Integer agreement;//是否合作
    private Integer state;//状态,后端赋值
    private String suppliername;
    private String introduce;
    @NotNull
    private PageParam pageParam;
    private List<OrderBy> orderBys;
}
