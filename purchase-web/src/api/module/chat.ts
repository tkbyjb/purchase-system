import request from '@/api/request'
import {AxiosPromise} from 'axios'

function getChatLeft(): AxiosPromise {
    //获取聊天左边信息
    return request({
        url: '/chat/get/left',
        method: 'get',
    })
}

function getMesss(sendUserId: string): AxiosPromise{
    //获取右侧的信息
    return request({
        url: '/chat/get/messs',
        method: 'get',
        params: {
            sendUserId,
        }
    })
}
export default {
    getChatLeft,
    getMesss,
}
