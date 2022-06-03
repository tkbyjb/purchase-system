package com.purchase.vo.param.system;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoleAddParam {
    @NotNull
    @NotEmpty
    private String rolename;
    private String note;
    private List<Long> menuIds;
}
