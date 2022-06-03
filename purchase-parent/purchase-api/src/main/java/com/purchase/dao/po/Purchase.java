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
@TableName("t_purchase")
@ApiModel(value = "Purchase对象", description = "")
public class Purchase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("采购id")
    @TableId(value = "purchaseId", type = IdType.ASSIGN_ID)
    private Long purchaseId;

    @ApiModelProperty("组合流水号/编号")
    private String serialNumber;

    @ApiModelProperty("对应的组合id")
    private Long combId;

    @ApiModelProperty("供应商id")
    private Long supplierId;

    @ApiModelProperty("合同名")
    private String contract;

    @ApiModelProperty("成交总额(更细节的价格等在合同和明细成交额里可查到)")
    private Double dealTotalPrice;

    @ApiModelProperty("状态(0删除1已添加未选择供应商2签署合同了未发货3已发货待签收4已签收待确认5已确认,完成6未完成,中途取消了")
    private Integer state;

    @ApiModelProperty("此采购对应的操作员用户id")
    private Long operatorUserId;

    @ApiModelProperty("id")
    private Long signUserId;

    @ApiModelProperty("1删除")
    private Integer isDelete;

    @ApiModelProperty("采购方式")
    private Integer purchaseWay;

    private LocalDateTime createTime;

    private LocalDateTime lastUpdateTime;

}
