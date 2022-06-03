package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class UserAssignVo {
    private String realname;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
}
