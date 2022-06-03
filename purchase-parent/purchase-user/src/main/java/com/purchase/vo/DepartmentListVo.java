package com.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentListVo {
    private List<DepartmentVo> departmentVos;
    private Integer total;
}
