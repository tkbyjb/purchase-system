import request from '@/api/request'
import { AxiosPromise } from 'axios'
import {ReviewParam, ReviewHistoryGetParam, Review3Param, CombReviewParam} from '@/type/review'

function reviewAdd1(reviewParam: ReviewParam): AxiosPromise {
    //确认审核第一层
    return request({
        url: '/review/add/1',
        method: 'post',
        data: reviewParam
    })
}

function reviewAdd2(reviewParam: ReviewParam): AxiosPromise {
    //确认审核第2层
    return request({
        url: '/review/add/2',
        method: 'post',
        data: reviewParam
    })
}

async function reviewAdd(level: number, reviewParam: ReviewParam): Promise<any> {
    //给前端判断是哪层的审核,不影响后端
    if(level == 1){
       return reviewAdd1(reviewParam)
    }else if(level == 2){
       return reviewAdd2(reviewParam)
    }
    // else if(level == 3) {
    //    return reviewAdd3(reviewParam)
    // }
}
// **********************************************************

async function getReviewHistory(level: number, reviewHistoryGetParam: ReviewHistoryGetParam): Promise<any> {
    //查询用户的历史审核记录
    if(level == 1){
        return getReviewHistory1(reviewHistoryGetParam)
     }else if(level == 2){
        return getReviewHistory2(reviewHistoryGetParam)
     }else if(level == 3) {
        return getReviewHistory3(reviewHistoryGetParam)
     }else if(level == 4) {
        return getReviewHistory4(reviewHistoryGetParam)
    }
}

function getReviewHistory1(reviewHistoryGetParam: ReviewHistoryGetParam): AxiosPromise {
    //查询用户的历史审核记录
    return request({
        url: '/review/history/get/1',
        method: 'post',
        data: reviewHistoryGetParam
    })
}

function getReviewHistory2(reviewHistoryGetParam: ReviewHistoryGetParam): AxiosPromise {
    //查询用户的历史审核记录
    return request({
        url: '/review/history/get/2',
        method: 'post',
        data: reviewHistoryGetParam
    })
}

function getReviewHistory3(reviewHistoryGetParam: ReviewHistoryGetParam): AxiosPromise {
    //查询用户的历史审核记录
    return request({
        url: '/review/history/get/3',
        method: 'post',
        data: reviewHistoryGetParam
    })
}

function getReviewHistory4(reviewHistoryGetParam: ReviewHistoryGetParam): AxiosPromise {
    //查询用户的历史审核记录
    return request({
        url: '/review/history/get/4',
        method: 'post',
        data: reviewHistoryGetParam
    })
}

// **********************************************************
function getReturnOpinion(applyId: string): AxiosPromise {
    //获取一个申请被打回的最新审核意见
    return request({
        url: '/review/get/returnOpinion',
        method: 'get',
        params: {
            applyId,
        }
    })
}


function reviewAdd3(reviewParam: Review3Param): AxiosPromise {
    //确认审核第3层,资产部分配采购方式那块儿
    return request({
        url: '/review/add/3',
        method: 'post',
        data: reviewParam
    })
}

function reviewAdd4(combReviewParam: CombReviewParam): AxiosPromise {
    //确认审核第3层,资产部分配采购方式那块儿
    return request({
        url: '/review/add/4',
        method: 'post',
        data: combReviewParam
    })
}
export default {
    reviewAdd1,
    reviewAdd2,
    reviewAdd3,
    reviewAdd,
    getReviewHistory,
    getReviewHistory1,
    getReviewHistory2,
    getReviewHistory3,
    getReturnOpinion,
    reviewAdd4,
    getReviewHistory4,
}
