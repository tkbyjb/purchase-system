import request from '@/api/request'
import {AxiosPromise} from 'axios'
import {LoginParam, RegisterParam, UserGetParam} from '@/type/user'

function register(registerParam: RegisterParam): AxiosPromise{
    return request({
        url: '/public/user/register',
        method: 'post',
        data: registerParam,
    })
}

function getRegisterVerifyCode(email: string): AxiosPromise{
    return request({
        url: '/public/verifyCode/register/user',
        method: 'get',
        params: {
            email,
        }
    })
}

function login(loginParam: LoginParam): AxiosPromise{
    return request({
        url: '/public/user/login',
        method: 'post',
        data: loginParam,
    })
}

function getUsers(userGetParam: UserGetParam): AxiosPromise {
    //系统管理查询用户信息
    return request({
        url: '/user/get',
        method: 'post',
        data: userGetParam,
    })
}

function updateUser(formData: FormData): AxiosPromise {
    //系统管理员修改用户信息,按理说用户修改是要重新验证邮箱的,暂时还没做
    return request({
        url: '/user/update',
        method: 'post',
        data: formData,
    })
}


function addUser(formData: FormData): AxiosPromise {
    //系统管理员修改用户信息,按理说用户修改是要重新验证邮箱的,暂时还没做
    return request({
        url: '/user/add',
        method: 'post',
        data: formData,
    })
}


function deletes(userIds: Array<string>): AxiosPromise {
    //系统管理员修改用户信息,按理说用户修改是要重新验证邮箱的,暂时还没做
    return request({
        url: '/user/deletes',
        method: 'post',
        data: userIds,
    })
}

function reviewUser(state: number, userId: string): AxiosPromise {
    //系统管理员审核用户账号
    return request({
        url: '/user/review',
        method: 'get',
        params: {
            state,
            userId,
        }
    })
}


function getAssignUsers(level: number): AxiosPromise {
    //获取能进行第level层审核的用户列表
    return request({
        url: '/user/get/assign',
        method: 'get',
        params: {
            level,
        }
    })
}

function getPayUsers(): AxiosPromise {
    //获取能支付的用户
    return request({
        url: '/user/get/pay',
        method: 'get',
    })
}

function logout(): AxiosPromise {
    //退出登录
    return request({
        url: '/logout',
        method: 'get',
    })
}
export default {
    register,
    getRegisterVerifyCode,
    login,
    getUsers,
    updateUser,
    addUser,
    deletes,
    reviewUser,
    getAssignUsers,
    getPayUsers,
    logout,
}
