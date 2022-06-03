package com.purchase.vo.param.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class InquiryPriceParam {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long inquiryId;
    private Double unitPrice;
    private Double totalPrice;
}
