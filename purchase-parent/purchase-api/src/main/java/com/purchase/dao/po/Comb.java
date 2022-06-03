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
@TableName(value = "t_comb",autoResultMap = true)
@ApiModel(value = "Comb对象", description = "")
public class Comb implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("组合id")
    @TableId(value = "combId", type = IdType.ASSIGN_ID)
    private Long combId;

    @ApiModelProperty("组合流水号/编号")
    private String serialNumber;

    @ApiModelProperty("组合名")
    private String combname;

    @ApiModelProperty("备注(存md的)")
    private String note;

    @ApiModelProperty("状态(0删除取消1正常)")
    private Integer state;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建此组合的用户id")
    private Long createUserId;

    @ApiModelProperty("预估总额")
    private Double predictTotalPrice;

    @ApiModelProperty("购买方式(0协议购买1其他购买)")
    private Integer purchaseWay;

    @ApiModelProperty("1删除")
    private Integer isDelete;

    @ApiModelProperty("指定操作员id")
    private Long confirmUserId;

}
