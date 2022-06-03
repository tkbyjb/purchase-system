package com.purchase.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
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
@TableName(value = "t_user", autoResultMap = true)
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @TableId(value = "userId", type = IdType.ASSIGN_ID)
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String tel;

    @ApiModelProperty("真实姓名")
    private String realname;

    @ApiModelProperty("0女1男")
    private Integer sex;

    @ApiModelProperty("身份证号")
    private String idNumber;

    @ApiModelProperty("出生日期")
    private LocalDate birth;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人id")
    private Long createUserId;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("所属部门id")
    private Long departmentId;

    @ApiModelProperty("状态(0删除1正常)")
    private Integer state;

    @ApiModelProperty("是否开启有消息时邮件提醒(0不开启1开启)")
    private Integer emailNotice;

    @ApiModelProperty("头像(聊天用)")
    private String avatar;

    @ApiModelProperty("1删除")
    private Integer isDelete;
}
