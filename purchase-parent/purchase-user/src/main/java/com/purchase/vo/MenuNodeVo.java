package com.purchase.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuNodeVo {
    private MenuVo menuVo;
    private List<MenuVo> childs;
}
