package com.purchase.vo.param;

import com.purchase.global.PageParam;
import lombok.Data;

@Data
public class DepartmentGetParam {
    private PageParam pageParam;
    private String departmentname;
}
