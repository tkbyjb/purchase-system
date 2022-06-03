package com.purchase.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ReviewCombParam {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long combId;
    @NotNull
    @Min(0)
    @Max(1)
    private Integer result;
    private String opinion;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long confirmUserId;//指定操作员id
}
