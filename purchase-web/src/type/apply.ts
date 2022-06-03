import { OrderBy, PageParam } from "./public";

export interface ApplyAddParam {
    //添加申请参数
    applyname: string;
    note: string;
    attachment?: string;
}

export interface ApplyUpdateParam {
    //修改申请的参数(物管员修改(管理员能改的更多,后边加))
    applyId: string;
    applyname: string;
    note: string;
    attachment?: string;
}

export interface ApplyGetParam {
    //搜索申请的参数(暂时不全,只有物管员的)
    serialNumber: string;
    applyname: string;
    startCreateTime: string;
    endCreateTime: string;
    note: string;
    state: number | null;
    pageParam: PageParam,
    orderBys: Array<OrderBy>,
    putoff?: number | null;
}

export interface Apply {
    //申请vo
    applyId: string;
    applyname: string;
    note: string;
    attachment: string;
}

export interface ApplyGetUnreviewParam {
    //查询未审核申请的参数
    pageParam: PageParam,
    orderBys: Array<OrderBy>,
}

export interface ReviewInfo {
    //申请详细信息
    applyUnreviewVo: ApplyUnreviewVo;
    detailUnreviewVos: Array<DetailUnreviewVo>;
    reviewUnreviewVos: Array<ReviewUnreviewVo>;
}

export interface DetailUnreviewVo {
    detailname: string;
    count: number;
    predictUnitPrice: number;
    predictTotalPrice: number;
    unit: string;
    note: string;
    spendingType: string;
}
export interface ReviewUnreviewVo {
    reviewUserRealname: string;
    reviewType: number;
    result: number;
    opinion: string;
    createTime: string;
}
export interface ApplyUnreviewVo {
    applyId: string;
    serialNumber: string;
    applyname: string;
    applyUsername: string;
    applyDepartmentname: string;
    applyTime: string;
    note: string | null;
    attachment: string | null;
    state: number;
    putoff: number | null;
}

// export interface ApplyReviewdGetParam {
//     //搜索3层审核通过的申请的参数
//     serialNumber: string;
//     applyname: string;
//     state: number | null;
//     pageParam: PageParam,
//     orderBys: Array<OrderBy>,
//     putoff?: number | null;
// }
