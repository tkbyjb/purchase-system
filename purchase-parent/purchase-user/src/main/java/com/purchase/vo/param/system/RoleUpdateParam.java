package com.purchase.vo.param.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoleUpdateParam {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roleId;
    @NotNull
    @NotEmpty
    private String rolename;
    private String note;
    private List<Long> menuIds;
}
