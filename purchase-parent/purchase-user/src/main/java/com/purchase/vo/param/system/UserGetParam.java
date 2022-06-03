package com.purchase.vo.param.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.purchase.global.OrderBy;
import com.purchase.global.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserGetParam {
    @NotNull
    private PageParam pageParam;
    private List<OrderBy> orderBys = null;
    private String username;
    private String realname;
    private Integer sex;
    private String tel;
    private String idNumber;
    private String startCreateTime;
    private String endCreateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roleId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long departmentId;
    private Integer state;
}
