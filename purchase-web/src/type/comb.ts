import {OrderBy, PageParam} from "@/type/public";

export interface CombAddParam {
    //添加组合的参数
    detailIds: Array<string>;
    combname: string;
    note: string|null;
}

export interface CombGetParam {
    //获取组合的参数
    pageParam: PageParam;
    orderBys: Array<OrderBy>;
    startCreateTime: string;
    endCreateTime: string;
    state: number|null;
    combname: string|null;
    serialNumber: string|null;
    purchaseWay: number|null;
}

export interface CombUpdateParam {
    //组合修改的参数
    combId: string|null;
    combname: string;
    note: string|null;
    detailIds: Array<string>;
}

