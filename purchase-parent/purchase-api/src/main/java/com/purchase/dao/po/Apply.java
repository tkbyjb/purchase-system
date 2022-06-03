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
@TableName(value = "t_apply", autoResultMap = true)
@ApiModel(value = "Apply对象", description = "")
public class Apply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("申请id")
    @TableId(value = "applyId", type = IdType.ASSIGN_ID)
    private Long applyId;

    @ApiModelProperty("申请编号/流水号")
    private String serialNumber;

    @ApiModelProperty("申请名")
    private String applyname;

    @ApiModelProperty("申请人id")
    private Long applyUserId;

    @ApiModelProperty("申请部门id")
    private Long applyDepartmentId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("提交申请的时间")
    private LocalDateTime applyTime;

    @ApiModelProperty("上次修改时间")
    private LocalDateTime lastUpdateTime;

    @ApiModelProperty("备注(存md)")
    private String note;

    @ApiModelProperty("附件名")
    private String attachment;

    @ApiModelProperty("状态(0删除1未申请2待部门管理审核3待财务部审核4待资产部总经理审核5通过所有审核未生成采购单6生成了采购单,待采购7采购中8采购完成9被取消,未完成采购)	采购的状态要根据明细来	只记录当前状态,历史状态去审核表和采购表等查询")
    private Integer state;

    @ApiModelProperty("1删除")
    private Integer isDelete;

    @ApiModelProperty("是否推迟")
    private Integer putoff;

    @ApiModelProperty("当前审核指定人")
    private Long currentAssignUserId;
}
