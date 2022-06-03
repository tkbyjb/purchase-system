package com.purchase.vo.param;

import com.purchase.global.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DepartmentBalanceGetParam {
    @NotNull
    private PageParam pageParam;
    private String departmentname;
}
