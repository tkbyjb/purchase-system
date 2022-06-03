package com.purchase.vo.chat.param;

import lombok.Data;

@Data
public class SendMessParam {
    private String mess;
    private Long sendUserId;
    private Long receiveUserId;
}
