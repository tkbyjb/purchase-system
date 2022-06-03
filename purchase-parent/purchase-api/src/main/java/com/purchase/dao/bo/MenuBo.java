package com.purchase.dao.bo;

import com.purchase.dao.po.Menu;
import lombok.Data;

import java.util.List;

@Data
public class MenuBo {
    private Menu menu;
    private List<MenuBo> childs;
}
