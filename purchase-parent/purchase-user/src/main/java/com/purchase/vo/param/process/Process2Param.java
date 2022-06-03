package com.purchase.vo.param.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class Process2Param {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long purchaseId;
    private List<Process2DetailParam> detailDeals;
}

