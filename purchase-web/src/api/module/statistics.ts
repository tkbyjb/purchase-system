import request from '@/api/request'
import {AxiosPromise} from 'axios'
import {SpendingTypePercentageGetParam} from "@/type/statistics";

function getSpendingTypePercentage(param: SpendingTypePercentageGetParam): AxiosPromise {
    //获取各种支付类型的比列
    return request({
        url: '/statistics/spendingTypePercentage',
        method: 'post',
        data: param,
    })
}

function getApplyCount(param: SpendingTypePercentageGetParam): AxiosPromise {
    //获取申请数量,和采购的分开请求,但显示在一个图表上,仅此而已
    return request({
        url: '/statistics/applyCount',
        method: 'post',
        data: param,
    })
}

function getPurchaseCount(param: SpendingTypePercentageGetParam) : AxiosPromise {
    //获取采购数量,成交总价
    return request({
        url: '/statistics/purchaseCount',
        method: 'post',
        data: param
    })
}

function getHeadinfo(): AxiosPromise {
    //获取统计信息头部的数据
    return request({
        url: '/statistics/headinfo',
        method: 'get',
    })
}
export default {
    getSpendingTypePercentage,
    getApplyCount,
    getPurchaseCount,
    getHeadinfo,
}
