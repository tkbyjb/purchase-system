import request from '@/api/request'
import {AxiosPromise} from 'axios'

function getSpendingTypes(): AxiosPromise {
    //获取所有支出类型列表
    return request({
        url: '/spendingType/get/all',
        method: 'get',
    })
}


export default {
    getSpendingTypes,
}