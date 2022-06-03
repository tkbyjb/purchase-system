import request from '@/api/request'
import {AxiosPromise} from 'axios'
import {
    Process2DetailParam, PurchaseGetParam, PurchaseUnpayGetParam,SupplierPurchaseGetParam, InquiryGetParam,InquiryDetail,InquriyPriceParam
} from "@/type/purchase";

function addPurchase(combId: string): AxiosPromise {
    //添加一条采购
    return request({
        url: '/purchase/add',
        method: 'get',
        params: {
            combId,
        }
    })
}

function getPurchase(purchaseGetParam: PurchaseGetParam): AxiosPromise {
    //查询采购信息
    return request({
        url: '/purchase/get',
        method: 'post',
        data: purchaseGetParam,
    })
}

function getSupplierPurchase(param: SupplierPurchaseGetParam): AxiosPromise {
    //获取供应商的采购单信息
    return request({
        url: '/supplier/get/purchase',
        method: 'post',
        data: param,
    })
}

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$采购流程种$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

function process1(supplierId: string, purchaseId: string): AxiosPromise {
    //采购第一步,操作员确认供应商
    return request({
        url: '/purchase/process/1',
        method: 'post',
        data: {
            supplierId: supplierId,
            purchaseId: purchaseId,
        }
    })
}


function process2(purchaseId: string, detailDeals: Array<Process2DetailParam>): AxiosPromise {
    //采购第二步,供应商给出价格
    return request({
        url: '/purchase/process/2',
        method: 'post',
        data: {
            purchaseId,
            detailDeals,
        },
    })
}

function process3(purchaseId: string, result: number): AxiosPromise {
    //采购第三步, 操作员确认价格(同意,不同意,换供应商)
    return request({
        url: '/purchase/process/3',
        method: 'post',
        data: {
            purchaseId: purchaseId,
            result: result,
        },
    })
}

function process4(formData: FormData): AxiosPromise {
    //采购第四步,供应商上传发送合同给采购方
    return request({
        url: '/purchase/process/4',
        method: 'post',
        data: formData,
    })
}

function process5(formData: FormData): AxiosPromise {
    //采购第五步,操作员上传签署过的合同给供应商
    return request({
        url: '/purchase/process/5',
        method: 'post',
        data: formData,
    })
}


function process6(purchaseId: string): AxiosPromise {
    //采购第6步,供应商确认发货
    return request({
        url: '/purchase/process/6',
        method: 'get',
        params: {
            purchaseId
        },
    })
}


function process7(purchaseId: string, payUserId: string): AxiosPromise {
    //采购第7步,操作员确认收货
    return request({
        url: '/purchase/process/7',
        method: 'get',
        params: {
            purchaseId,
            payUserId,
        },
    })
}

function getUnpayPurchase(purchaseUnpayGetParam: PurchaseUnpayGetParam): AxiosPromise {
    //获取当前登录用户的待支付账单
    return request({
        url: '/purchase/get/unpay',
        method: 'post',
        data: purchaseUnpayGetParam,
    })
}


function process8(purchaseId: string): AxiosPromise {
    //采购第8步,财务部支付
    return request({
        url: '/purchase/process/8',
        method: 'get',
        params: {
            purchaseId,
        },
    })
}

//询价**************************************************
function inquiryAdd(purchaseId: string, supplierId: string): AxiosPromise {
    //添加一条询价记录
    return request({
        url: '/inquiry/add',
        method: 'get',
        params: {
            purchaseId,
            supplierId,
        }
    })
}

function getInquiry(inquiryGetParam: InquiryGetParam): AxiosPromise {
    //供应商获取询价
    return request({
        url: '/inquiry/get/supplier',
        method: 'post',
        data: inquiryGetParam,
    })
}

function getInquiryDetail(purchaseId: string): AxiosPromise {
    //供应商获取询价明细信息
    return request({
        url: '/inquiry/get/detail',
        method: 'get',
        params: {
            purchaseId,
        },
    })
}

function setInquiryPrice(params: Array<InquriyPriceParam>): AxiosPromise {
    //供应商给出价格
    return request({
        url: '/inquiry/set/price',
        method: 'post',
        data: params,
    })
}

function getInquiryAPurchase(purchaseId: string): AxiosPromise {
    //获取一个采购单对应的所有询价,按供应商分组
    return request({
        url: '/inquiry/get/purchase',
        method: 'get',
        params:{
            purchaseId,
        }
    })
}
export default {
    addPurchase,
    getPurchase,
    process1,
    getSupplierPurchase,
    process2,
    process3,
    process4,
    process5,
    process6,
    process7,
    getUnpayPurchase,
    process8,
    inquiryAdd,
    getInquiry,
    getInquiryDetail,
    setInquiryPrice,
    getInquiryAPurchase,
}
