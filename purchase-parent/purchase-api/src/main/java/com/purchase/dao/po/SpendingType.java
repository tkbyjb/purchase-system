package com.purchase.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("t_spending_type")
@ApiModel(value = "SpendingType对象", description = "")
public class SpendingType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消费类型id")
    @TableId(value = "spendingTypeId", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long spendingTypeId;

    @ApiModelProperty("消费类型名")
    private String typename;

    @ApiModelProperty("备注(存字符串的)")
    private String note;

}
