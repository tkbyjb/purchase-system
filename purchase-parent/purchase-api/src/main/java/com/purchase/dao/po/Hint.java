package com.purchase.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("t_hint")
@ApiModel(value = "Hint对象", description = "")
public class Hint implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("提示id")
    @TableId(value = "hintId", type = IdType.ASSIGN_ID)
    private Long hintId;

    @ApiModelProperty("提示内容")
    private String content;

    @ApiModelProperty("提示的用户id")
    private Long hintUserId;

    @ApiModelProperty("是否已读(0未读1已读)")
    private Integer read;

    @ApiModelProperty("状态(0删除1正常)")
    private Integer state;

    @ApiModelProperty("1删除")
    private Integer isDelete;

}
