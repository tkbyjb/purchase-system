package com.purchase.vo.statistics.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PurchaseCountVo {
    private String date;
    private Integer count;
    private Double amount = 0d;
}
