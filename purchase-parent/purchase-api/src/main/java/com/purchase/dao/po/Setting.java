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
@TableName("t_setting")
@ApiModel(value = "Setting对象", description = "")
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("一些配置项id")
    @TableId(value = "settingId", type = IdType.ASSIGN_ID)
    private Long settingId;

    @ApiModelProperty("配置名")
    private String keyc;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("ntoe")
    private String note;

}
