import request from '@/api/request'
import {AxiosPromise} from 'axios'

function setDeadline(deadline: string): AxiosPromise {
    //设置采购截止时间
    return request({
        url: '/setting/set/deadline',
        method: 'get',
        params: {
            deadline,
        }
    })
}


function getDeadline(): AxiosPromise {
    //设置采购截止时间
    return request({
        url: '/setting/get/deadline',
        method: 'get',
    })
}
export default {
    setDeadline,
    getDeadline,
}
