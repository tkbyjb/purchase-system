package com.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentBalanceListVo {
    private List<DepartmentBalanceVo> departmentBalanceVos;
    private Integer total;
}

