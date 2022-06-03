import {PageParam, OrderBy} from '@/type/public'

export interface ReviewParam {
    //确认审核参数
    applyId: string;
    result: number;
    opinion: string;
    assignUserId: string;
}

export interface ReviewHistoryGetParam {
    //查询用户自己的审核历史记录的参数
     pageParam: PageParam;
     orderBys: Array<OrderBy>;
     startCreateTime: string;
     endCreateTime: string;
     result: number|null;
     reviewType?: number|null;
     state?: number|null;
}

export interface Review3Param {
    //第三层提交审核需要的参数,资产部审核需要指定每个明细的采购方式
    applyId: string;
    result: number;
    opinion: string;
    purchaseWay0: Array<string>;//协议采购的明细id列表
    purchaseWay1: Array<string>;//其他采购的明细id列表
    assignUserId?: string;
}
export interface CombReviewParam {
    //确认组合审核参数,第四层
    combId: string;
    result: number;
    opinion: string;
    confirmUserId: string;
}
