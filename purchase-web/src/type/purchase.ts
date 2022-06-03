import {OrderBy, PageParam} from "@/type/public";

export interface PurchaseGetParam {
    //查询采购参数
    pageParam: PageParam;
    orderBys: Array<OrderBy>;
    serialNumber: string | null;
    state: number | null;
    purchaseWay: number | null;
    supplierId: string | null;
}

export interface SupplierPurchaseGetParam {
    //供应商查询自己收到的采购订单的参数
    pageParam: PageParam;
    orderBys: Array<OrderBy>;
    serialNumber: string | null;
    state: number | null;
}

export interface Process2DetailParam {
    //供应商给出价格的参数
    detailId: string;
    dealUnitPrice: number;
    dealTotalPrice: number;
}
// export interface Process2Param {
//     //供应商给出价格的参数
//     purchaseId: string;
//     List<>
// }

export interface  PurchaseUnpayGetParam {
    //查询待支付订单的参数
    pageParam: PageParam;
    orderBys: Array<OrderBy>;
}

// export interface InquiryAddParam {
//     //添加一个采购的询价的参数
//
// }

export interface InquiryGetParam {
    //供应商获取询价的参数
    pageParam: PageParam;
    orderBys: Array<OrderBy>;
    state: number|null;
}
export interface InquiryDetail {
    inquiryId: string;
    unitPrice: number|null;
    totalPrice: number|null;
    detailname: string;
    count: number;
    predictUnitPrice: number;
    predictTotalPrice: number;
    unit: string;
}
export interface InquriyPriceParam {
    inquiryId: string;
    unitPrice: number|null;
    totalPrice: number|null;
}
