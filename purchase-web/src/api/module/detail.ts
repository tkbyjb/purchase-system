import request from '@/api/request'
import {AxiosPromise} from 'axios'
import {DetailAddParam, DetailUncombGetParam, DetailUpdateParam} from "@/type/detail";

function getDetailByApplyId(applyId: string): AxiosPromise {
    //根据申请id获取一个申请的所有明细
    return request({
        url: '/detail/get/applyId',
        method: 'get',
        params: {applyId},
    })
}

function addDetail(detailAddParam: DetailAddParam): AxiosPromise{
    //添加一个明细
    return request({
        url: '/detail/add',
        method: 'post',
        data: detailAddParam,
    })
}

function deleteDetail(detailId: string): AxiosPromise{
    //删除一个明细
    return request({
        url: '/detail/delete/detailId',
        method: 'get',
        params: {
            detailId,
        }
    })
}

function updateDetail(detailUpdateParam: DetailUpdateParam): AxiosPromise{
    //修改一个明细
    return request({
        url: '/detail/update',
        method: 'post',
        data: detailUpdateParam,
    })
}

function getByDetailId(detailId: string): AxiosPromise{
    //通过明细id获取明细
    return request({
        url: '/detail/get/detailId',
        method: 'get',
        params: {
            detailId,
        }
    })
}

function getUncombDetails(detailUncombGetParam: DetailUncombGetParam): AxiosPromise {
    //获取未加入组合的明细
    return request({
        url: '/detail/get/uncomb',
        method: 'post',
        data: detailUncombGetParam,
    })
}


function getCombDetails(combId: string):AxiosPromise{
    //获取一个组合的明细信息
    return request({
        url: '/detail/get/combId',
        method: 'get',
        params:{
            combId,
        }
    })
}
export default {
    getDetailByApplyId,
    addDetail,
    deleteDetail,
    updateDetail,
    getByDetailId,
    getUncombDetails,
    getCombDetails,
}
