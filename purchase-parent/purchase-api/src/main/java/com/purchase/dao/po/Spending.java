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
@TableName("t_spending")
@ApiModel(value = "Spending对象", description = "")
public class Spending implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支出id")
    @TableId(value = "spendingId", type = IdType.ASSIGN_ID)
    private Long spendingId;

    @ApiModelProperty("支出账单流水号/编号")
    private String serialNumber;

    @ApiModelProperty("金额")
    private Double amount;

    @ApiModelProperty("支出部门id")
    private Long departmentId;

    @ApiModelProperty("支出处理这个付款的财务部用户id")
    private Long payUserId;

    @ApiModelProperty("创建时间(支付时间)")
    private LocalDateTime createTime;

    @ApiModelProperty("消费支出类型id")
    private Long spendingTypeId;

    @ApiModelProperty("备注(存md的)")
    private String note;

    @ApiModelProperty("状态(0删除1正常)")
    private Integer state;

    @ApiModelProperty("对应采购id(除了资产部采购也可以是其他订单号)")
    private Long purchaseId;

    @ApiModelProperty("1删除")
    private Integer isDelete;

}
