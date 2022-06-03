package com.purchase.vo.system;

import lombok.Data;
import java.util.List;

@Data
public class UserListVo {
    private List<UserVo> userVos;
    private Integer total;
}
