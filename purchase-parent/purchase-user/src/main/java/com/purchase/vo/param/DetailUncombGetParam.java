package com.purchase.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class DetailUncombGetParam {
    private String applySerialNumber;
    private String detailname;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long departmentId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long spendingTypeId;
    private Integer purchaseWay;
}
