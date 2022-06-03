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
@TableName("t_inquiry")
@ApiModel(value = "Inquiry对象", description = "")
public class Inquiry implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("询价id")
    @TableId(value = "inquiryId", type = IdType.ASSIGN_ID)
    private Long inquiryId;

    @ApiModelProperty("对应的采购id")
    private Long purchaseId;

    @ApiModelProperty("明细id")
    private Long detailId;

    @ApiModelProperty("供应商id")
    private Long supplierId;

    @ApiModelProperty("报价说明(存md的)")
    private String offer;

    @ApiModelProperty("状态(0删除1正常)")
    private Integer state;

    @ApiModelProperty("1删除")
    private Integer isDelete;

    private Double unitPrice;
    private Double totalPrice;
    private LocalDateTime createTime;
}
