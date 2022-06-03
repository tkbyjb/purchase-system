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
@TableName(value = "t_review", autoResultMap = true)
@ApiModel(value = "Review对象", description = "")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("审核id")
    @TableId(value = "reviewId", type = IdType.ASSIGN_ID)
    private Long reviewId;

    @ApiModelProperty("被审核的id")
    private Long applyId;

    @ApiModelProperty("被审核的组合id,二选一")
    private Long combId;

    @ApiModelProperty("审核人id")
    private Long userId;

    @ApiModelProperty("审核类型(1部门管理审核2财务部审核3资产部经理审核)")
    private Integer reviewType;

    @ApiModelProperty("审核结果(0不通过1通过)")
    private Integer result;

    @ApiModelProperty("审核意见(存md)")
    private String opinion;

    @ApiModelProperty("创建时间/审核时间")
    private LocalDateTime createTime;

    @ApiModelProperty("当经费不足时,财务部会把余额放这个字段")
    private Double balance;

    @ApiModelProperty("状态(0删除1正常)")
    private Integer state;

    @ApiModelProperty("1删除")
    private Integer isDelete;
}
