package com.purchase.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CombUpdateParam {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    private Long combId;
    @NotNull
    @NotEmpty
    private String combname;
    private String note;
    @NotNull
    @Size(min = 1)
    private List<Long> detailIds;

}
