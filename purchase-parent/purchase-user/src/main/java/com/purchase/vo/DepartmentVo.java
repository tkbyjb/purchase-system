package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class DepartmentVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long departmentId;
    @NotNull
    @NotEmpty
    private String departmentname;
    @NotNull
    @Min(0)
    @Max(1)
    private Integer type;
    @Min(0)
    private Double balance;
    private String note;
}
