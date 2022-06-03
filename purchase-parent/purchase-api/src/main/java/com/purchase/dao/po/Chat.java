package com.purchase.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author 作者
 * @since 2022-04-30
 */
@Data
@TableName(value = "t_chat",autoResultMap = true)
@ApiModel(value = "Chat对象", description = "")
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("聊天id")
    @TableId(value = "chatId", type = IdType.ASSIGN_ID)
    private Long chatId;

    @ApiModelProperty("聊天内容")
    private String content;

    @ApiModelProperty("发送消息用户id")
    private Long sendUserId;

    @ApiModelProperty("接收消息用户id")
    private Long receiveUserId;

    @ApiModelProperty("是否已读(0未读1已读)")
    private Integer isRead;

    @ApiModelProperty("状态(0删除1正常)")
    private Integer state;

    @ApiModelProperty("1删除")
    private Integer isDelete;

    private LocalDateTime createTime;

}
