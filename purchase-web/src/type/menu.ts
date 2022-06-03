export interface Menu {
    //菜单获取到的对象
    menuname: string;
    permission: string;
    icon: string;
    childs: Array<Menu>;
    route: any; //此api对应的路由,api没有路由则为空
}

export interface MenuName {
    menuId: string;
    menuname: string;
    childs: Array<MenuName>;
}