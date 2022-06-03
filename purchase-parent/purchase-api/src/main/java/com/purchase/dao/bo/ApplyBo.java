package com.purchase.dao.bo;

import com.purchase.dao.po.Apply;
import com.purchase.dao.po.Department;
import com.purchase.dao.po.User;
import lombok.Data;

@Data
public class ApplyBo {
    private Long applyId;
    private Apply apply;
    private User applyUser;
    private Department applyDepartment;
}
