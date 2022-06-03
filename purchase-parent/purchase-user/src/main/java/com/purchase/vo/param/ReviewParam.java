package com.purchase.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ReviewParam {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long applyId;
    @NotNull
    @Min(0)
    @Max(1)
    private Integer result;
    private String opinion;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long assignUserId;
}
