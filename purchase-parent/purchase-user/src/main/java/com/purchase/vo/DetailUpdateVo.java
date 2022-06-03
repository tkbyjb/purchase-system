package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class DetailUpdateVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long detailId;
    @Pattern(regexp = "[\\s\\S\\u4e00-\\u9fa5]{2,128}")
    private String detailname;
    @Min(1)
    @Max(10000)
    private Integer count;
    @Min(0)
    private Double predictUnitPrice;
    @Min(0)
    private Double predictTotalPrice;
    @Pattern(regexp = "[\\w\\u4e00-\\u9fa5]{1,10}")
    private String unit;
    private String note;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long spendingTypeId;
}
