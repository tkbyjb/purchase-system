package com.purchase.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class MenuManaVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long menuId;
    private String menuname;
    private List<MenuManaVo> childs;
}
