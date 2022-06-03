import { OrderBy, PageParam } from "./public";

export interface RoleName {
    roleId: string;
    rolename: string;
}

export interface RoleGetParam {
    //获取角色的参数
    pageParam: PageParam;
    orderBys: Array<OrderBy>;
    rolename: string|null;
}

export  interface RoleUpdateParam {
    //修改角色的参数
    roleId: string;
    rolename: string;
    note: string;
    menuIds: Array<string>;
}

export  interface RoleAddParam {
    //修改角色的参数
    rolename: string;
    note: string;
    menuIds: Array<string>;
}