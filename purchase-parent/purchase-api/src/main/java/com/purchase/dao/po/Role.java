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
@TableName("t_role")
@ApiModel(value = "Role对象", description = "")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色id")
    @TableId(value = "roleId", type = IdType.ASSIGN_ID)
    private Long roleId;

    @ApiModelProperty("角色名")
    private String rolename;

    @ApiModelProperty("备注(只存字符文本)")
    private String note;

    @ApiModelProperty("状态(0删除1正常)为了防止删除一个角色会删掉相关数据,直接根据用户对应角色状态限制之后操作")
    private Integer state;

    @ApiModelProperty("1删除")
    private Integer isDelete;

}
