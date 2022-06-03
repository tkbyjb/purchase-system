package com.purchase.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Review3Param {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long applyId;
    @NotNull
    @Min(0)
    @Max(1)
    private Integer result;
    private String opinion;
    private List<Long> purchaseWay0;
    private List<Long> purchaseWay1;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long assignUserId;
}
