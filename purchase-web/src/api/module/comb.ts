import request from '@/api/request'
import {AxiosPromise} from 'axios'
import {CombAddParam,CombGetParam, CombUpdateParam} from '@/type/comb'

function addComb(combAddParam: CombAddParam): AxiosPromise {
    //添加一个组合
    return request({
        url: '/comb/add',
        method: 'post',
        data: combAddParam,
    })
}

function getCombs(combGetParam: CombGetParam) {
    //获取组合
    return request({
        url: '/comb/get',
        method: 'post',
        data: combGetParam,
    })
}

function getUnreviewCombs(combGetParam: CombGetParam) {
    //获取组合
    return request({
        url: '/comb/get/unreview',
        method: 'post',
        data: combGetParam,
    })
}

function combToReview(combId: string, assignUserId: string): AxiosPromise {
    //提交组合去审核
    return request({
        url: '/comb/toReview',
        method: 'get',
        params: {
            combId,
            assignUserId,
        }
    })
}

function getUnconfirmCombs(combGetParam: CombGetParam): AxiosPromise {
    //获取未申领的组合
    return request({
        url: '/comb/get/unconfirm',
        method: 'post',
        data: combGetParam,
    })
}

function deletes(ids: Array<string>): AxiosPromise {
    //删除选中组合
    return request({
        url: '/comb/deletes',
        method: 'post',
        data: ids,
    })
}

function updateComb(param: CombUpdateParam): AxiosPromise {
    //修改组合
    return request({
        url: '/comb/update',
        method: 'post',
        data: param,
    })
}
export default  {
    addComb,
    getCombs,
    getUnreviewCombs,
    combToReview,
    getUnconfirmCombs,
    deletes,
    updateComb,
}
