import {PageParam, OrderBy} from "@/type/public";

export interface SupplierRegisterParam {
    //供应商注册账号参数
    suppliername: string;
    email: string;
    tel: string;
    loginname: string;
    password: string;
    agreement: number|null;
    verifyCode: string;
}


export interface SupplierGetParam {
    //查询供应商的参数
    pageParam: PageParam;
    orderBys: Array<OrderBy>;
    agreement: number|null;
    state: number|null;
    suppliername: string|null;
    introduce: string|null;
}
