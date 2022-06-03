package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class MenuVo {
//    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    private Long menuId;
    private String menuname;
    private String icon;
    private String permission;
//    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    private Long parentId;
    private List<MenuVo> childs;
}
