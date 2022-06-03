package com.purchase.vo.redis;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginMark {
    private String userUniqueId;
    private Integer tryCount;
    private LocalDateTime endLockTime;
}
