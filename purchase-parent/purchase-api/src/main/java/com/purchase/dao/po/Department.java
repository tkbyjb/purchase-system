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
@TableName(value = "t_department",autoResultMap = true)
@ApiModel(value = "Department对象", description = "")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("部门id")
    @TableId(value = "departmentId", type = IdType.ASSIGN_ID)
    private Long departmentId;

    @ApiModelProperty("部门名")
    private String departmentname;

    @ApiModelProperty("部门类型(0学院1部门)")
    private Integer type;

    @ApiModelProperty("经费余额")
    private Double balance;

    @ApiModelProperty("状态(0删除1正常)防止删除部门会删掉相关采购数据")
    private Integer state;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("状态(0删除1正常")
    private Integer isDelete;

}
