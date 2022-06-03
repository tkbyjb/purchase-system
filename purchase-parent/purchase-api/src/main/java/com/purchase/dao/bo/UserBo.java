package com.purchase.dao.bo;

import com.purchase.dao.po.Department;
import com.purchase.dao.po.Role;
import com.purchase.dao.po.User;
import lombok.Data;

@Data
public class UserBo {
    private Long userId;
    private User user;
    private Role role;
    private Department department;
    private User createUser;
}
