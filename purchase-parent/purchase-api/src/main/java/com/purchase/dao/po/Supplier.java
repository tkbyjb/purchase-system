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
@TableName("t_supplier")
@ApiModel(value = "Supplier对象", description = "")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("供应商id")
    @TableId(value = "supplierId", type = IdType.ASSIGN_ID)
    private Long supplierId;

    @ApiModelProperty("供应商名")
    private String suppliername;

    @ApiModelProperty("邮箱(用于提醒)")
    private String email;

    @ApiModelProperty("用于直接打电话联系")
    private String tel;

    @ApiModelProperty("用于登录的账号名")
    private String loginname;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("大致介绍提供的商品")
    private String introduce;

    @ApiModelProperty("是否是学校的协议(0不是1是)")
    private Integer agreement;

    @ApiModelProperty("状态(0删除1正常)")
    private Integer state;

    @ApiModelProperty("1删除")
    private Integer isDelete;


    @ApiModelProperty("头像")
    private String avatar;
}
