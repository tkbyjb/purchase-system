package com.purchase.vo.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CombAddParam {
    @NotNull
    @Size(min = 1)
    private List<Long> detailIds;
    @NotNull
    @NotEmpty
    private String combname;
    private String note;
}
