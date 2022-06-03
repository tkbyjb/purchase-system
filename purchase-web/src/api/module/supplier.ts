import request from '@/api/request'
import {AxiosPromise} from 'axios'
import {LoginParam} from '@/type/user'
import {SupplierGetParam, SupplierRegisterParam} from '@/type/supplier'


function login(loginParam: LoginParam): AxiosPromise {
    //供应商登录
    return request({
        url:'/public/supplier/login',
        method: 'post',
        data: loginParam,
    })
}

function register(registerParam: SupplierRegisterParam): AxiosPromise {
    //供应商登录
    return request({
        url:'/public/supplier/register',
        method: 'post',
        data: registerParam,
    })
}

function getRegisterVerifyCode(email: string): AxiosPromise{
    return request({
        url: '/public/verifyCode/register/supplier',
        method: 'get',
        params: {
            email,
        }
    })
}


function getSupplier(supplierGetParam: SupplierGetParam): AxiosPromise {
    //查询供应商
    return request({
        url: '/supplier/get',
        method: 'post',
        data: supplierGetParam,
    })
}

export default {
    login,
    register,
    getRegisterVerifyCode,
    getSupplier,
}
