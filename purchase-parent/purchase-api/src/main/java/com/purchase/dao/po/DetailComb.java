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
@TableName("t_detail_comb")
@ApiModel(value = "DetailComb对象", description = "")
public class DetailComb implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("明细组合id")
    @TableId(value = "detailCombId", type = IdType.ASSIGN_ID)
    private Long detailCombId;

    @ApiModelProperty("明细id")
    private Long detailId;

    @ApiModelProperty("所属组合id")
    private Long combId;

}
