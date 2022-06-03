package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 用于审核时显示的明细数据，字段会少很多
 */
@Data
public class DetailUnreviewVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long detailId;
    private String detailname;
    private Integer count;
    private Double predictUnitPrice;
    private Double predictTotalPrice;
    private Double dealUnitPrice;
    private Double dealTotalPrice;
    private String unit;
    private String note;
//    private Integer state;
    private String spendingType;//消费类型名

}
