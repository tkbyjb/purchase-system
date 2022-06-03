export interface DetailAddParam {
    //添加明细参数
    applyId: string;
    detailname: string;
    count: number;
    predictUnitPrice: number;
    predictTotalPrice: number;
    unit: string;
    note: string;
    spendingTypeId: string | null;
}

export interface DetailUpdateParam {
    //修改明细的参数
    detailId: string;
    detailname: string;
    count: number;
    predictUnitPrice: number;
    predictTotalPrice: number;
    unit: string;
    note: string;
    spendingTypeId: string | null;
}

export interface DetailUncombGetParam {
    //获取未分配组合的明细参数
    // pageParam?: PageParam;
    // orderBys?: Array<OrderBy>;
    applySerialNumber: string;//所属申请流水号
    detailname: string;
    departmentId: string | null;//明细所属部门id
    // applyUserId?: string;//明细的申请人真名
    spendingTypeId: string | null;//明细支出类型id
    purchaseWay: number | null;
}

export interface Detail {
    //明细数据
    detailId: string;
    serialNumber: string;
    detailname: string;
    count: number;
    predictUnitPrice: number;
    predictTotalPrice: number;
    dealUnitPrice: number;
    dealTotalPrice: number;
    unit: string;
    purchaseWay: number;
    note: string;
    state: number;
    spendingType: string;
}
