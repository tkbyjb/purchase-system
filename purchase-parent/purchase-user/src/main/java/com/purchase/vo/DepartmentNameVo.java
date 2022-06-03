package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class DepartmentNameVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long departmentId;
    private String departmentname;
}
