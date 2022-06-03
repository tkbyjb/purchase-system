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
@TableName("t_menu")
@ApiModel(value = "Menu对象", description = "")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单id")
    @TableId(value = "menuId", type = IdType.ASSIGN_ID)
    private Long menuId;

    @ApiModelProperty("菜单名")
    private String menuname;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("permission")
    private String permission;

    @ApiModelProperty("备注(只存字符文本)")
    private String note;

    @ApiModelProperty("父菜单id")
    private Long parentId;

}
