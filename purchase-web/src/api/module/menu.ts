import request from '@/api/request'
import {AxiosPromise} from 'axios'


function getMenuTree(): AxiosPromise {
    //获取所有权限tree
    return request({
        url: '/menu/get/all/tree',
        method: 'get',
    })
}

export default {
    getMenuTree,
}