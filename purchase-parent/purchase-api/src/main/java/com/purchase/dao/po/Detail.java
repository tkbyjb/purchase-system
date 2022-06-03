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
@TableName(value = "t_detail", autoResultMap = true)
@ApiModel(value = "Detail对象", description = "")
public class Detail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("明细id")
    @TableId(value = "detailId", type = IdType.ASSIGN_ID)
    private Long detailId;

    @ApiModelProperty("所属申请id")
    private Long applyId;

    @ApiModelProperty("明细编号/流水号")
    private String serialNumber;

    @ApiModelProperty("明细名")
    private String detailname;

    @ApiModelProperty("数量")
    private Integer count;

    @ApiModelProperty("预估单价")
    private Double predictUnitPrice;

    @ApiModelProperty("预估总价")
    private Double predictTotalPrice;

    @ApiModelProperty("成交单价")
    private Double dealUnitPrice;

    @ApiModelProperty("成交总价")
    private Double dealTotalPrice;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("购买方式(0协议购买1其他购买)")
    private Integer purchaseWay;

    @ApiModelProperty("备注(存md)")
    private String note;

    @ApiModelProperty("状态(0删除1还没成交2已经成交)")
    private Integer state;

    @ApiModelProperty("消费类型id")
    private Long spendingTypeId;

    @ApiModelProperty("1删除")
    private Integer isDelete;

}
