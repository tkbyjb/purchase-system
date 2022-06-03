package com.purchase.vo.chat.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessVo {
    private String mess;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;
    private Integer type=0;//是自己发的还是对方发的,1是自己0是对方
}
