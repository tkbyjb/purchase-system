package com.purchase.vo.param.process;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class Process3Param {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long purchaseId;
    @NotNull
    @Min(1)
    @Max(3)
    private Integer result;
    //确认价格的结果1通过2不通过,但任然是当前供应商,给供应商修改(协商通过聊天)3取消,可以选新的供应商
}
