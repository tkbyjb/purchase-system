package com.purchase.vo.param.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 选择供应商的参数
 */
@Data
public class Process1Param {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long supplierId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long purchaseId;
}
