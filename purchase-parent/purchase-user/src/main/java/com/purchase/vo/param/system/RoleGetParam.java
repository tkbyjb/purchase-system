package com.purchase.vo.param.system;

import com.purchase.global.OrderBy;
import com.purchase.global.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Data
public class RoleGetParam {
    @NotNull
    private PageParam pageParam;
    private List<OrderBy> orderBys = null;
    private String rolename = null;
}
