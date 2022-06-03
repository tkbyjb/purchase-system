package com.purchase.vo.chat.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatLeftVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;//用户id或供应商id,发送消息的人id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime newestMessTime;//最新发送消息时间
    private Integer unreadMessCount;//未读消息数量

    private String realname;//用户真名
    private Integer isOnline;//0不在线1在线(通过sessionMap)
    private String avatar;//此用户的头像地址
}
