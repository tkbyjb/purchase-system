import request from '@/api/request'
import {ApplyGetParam, ApplyGetUnreviewParam, ApplyUpdateParam} from '@/type/apply'
import {AxiosPromise} from 'axios'

function add(formData: FormData): AxiosPromise {
    //添加申请
    return request({
        url: '/apply/department/add',
        method: 'post',
        // headers: {
        //     'Content-type' : 'multipart/form-data'
        // },
        data: formData,
    })
}

function get(param: ApplyGetParam): AxiosPromise {
    //查询申请
    return request({
        url: '/apply/get',
        method: 'post',
        data: param,
    })
}


function deletes(applyIds: Array<string>): AxiosPromise {
    //删除选中的申请
    return request({
        url: '/apply/delete/applyIds',
        method: 'post',
        data: applyIds,
    })
}

function update(applyUpdateParam: ApplyUpdateParam): AxiosPromise {
    //修改申请
    return request({
        url: '/apply/update/applyId',
        method: 'post',
        data: applyUpdateParam,
    })
}

function toReview(applyId: string, assignUserId: string): AxiosPromise {
    //提交审核
    return request({
        url: '/apply/update/toReview',
        method: 'get',
        params: {applyId, assignUserId},
    })
}

// *************************************************************************

function getUnreview1(applyGetUnreviewParam: ApplyGetUnreviewParam): AxiosPromise {
    //查询未进行第一层审核的申请
    return request({
        url: '/apply/get/unreview1',
        method: 'post',
        data: applyGetUnreviewParam,
    })
}

async function getUnreview(level: number, applyGetUnreviewParam: ApplyGetUnreviewParam): Promise<any> {
    //判断是那层未审核的，方便组件复用
    if (level == 1) {
        return getUnreview1(applyGetUnreviewParam)
    } else if (level == 2) {
        return getUnreview2(applyGetUnreviewParam)
    } else if (level == 3) {
        return getUnreview3(applyGetUnreviewParam)
    }
}

function getUnreview2(applyGetUnreviewParam: ApplyGetUnreviewParam): AxiosPromise {
    //查询未进行第二层资产部审核的申请
    return request({
        url: '/apply/get/unreview2',
        method: 'post',
        data: applyGetUnreviewParam,
    })
}

function getUnreview3(applyGetUnreviewParam: ApplyGetUnreviewParam): AxiosPromise {
    //查询未进行第二层资产部审核的申请
    return request({
        url: '/apply/get/unreview3',
        method: 'post',
        data: applyGetUnreviewParam,
    })
}

// *************************************************************************

function getReviewInfo1(applyId: string): AxiosPromise {
    //查询一个申请的详细信息,包括申请本身,明细,审核,1
    return request({
        url: '/apply/get/reviewInfo1',
        method: 'get',
        params: {
            applyId,
        }
    })
}


function getReviewInfo2(applyId: string): AxiosPromise {
    //查询一个申请的详细信息,包括申请本身,明细,审核,2
    return request({
        url: '/apply/get/reviewInfo2',
        method: 'get',
        params: {
            applyId,
        }
    })
}

function getReviewInfo3(applyId: string): AxiosPromise {
    //查询一个申请的详细信息,包括申请本身,明细,审核,3
    return request({
        url: '/apply/get/reviewInfo3',
        method: 'get',
        params: {
            applyId,
        }
    })
}

async function getReviewInfo(level: number, applyId: string): Promise<any> {
    //判断是那层未审核的，方便组件复用
    if (level == 1) {
        return getReviewInfo1(applyId)
    } else if (level == 2) {
        return getReviewInfo2(applyId)
    } else if (level == 3) {
        return getReviewInfo3(applyId)
    }
}

// *************************************************************************

function getDetailApplyInfo(detailId: string): AxiosPromise {
    //获取明细对应的申请详细信息
    return request({
        url: 'apply/get/detailInfo',
        method: 'get',
        params: {
            detailId,
        }
    })
}

function getAllReviewdApply(param: ApplyGetParam):AxiosPromise {
    //查询所有审核通过的申请
    return request({
        url: '/apply/get/reviewd',
        method: 'post',
        data: param,
    })
}
export default {
    add,
    get,
    deletes,
    update,
    toReview,
    getUnreview1,
    getReviewInfo,
    getUnreview,
    getUnreview2,
    getReviewInfo2,
    getReviewInfo1,
    getUnreview3,
    getReviewInfo3,
    getDetailApplyInfo,
    getAllReviewdApply,
}
