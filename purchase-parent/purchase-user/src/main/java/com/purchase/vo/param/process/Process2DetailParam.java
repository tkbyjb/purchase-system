package com.purchase.vo.param.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class Process2DetailParam {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long detailId;
    private Double dealUnitPrice;
    private Double dealTotalPrice;
}