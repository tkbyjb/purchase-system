import request from '@/api/request'
import {AxiosPromise} from 'axios'
import { RoleGetParam, RoleUpdateParam, RoleAddParam } from '@/type/role'

function getAllRole(): AxiosPromise{
    return request({
        url: '/public/role/get/all',
        method: 'get',
    })
}

function getRoles(roleGetParam: RoleGetParam): AxiosPromise {
    //系统管理获取角色信息
    return request({
        url: '/role/get',
        method: 'post',
        data: roleGetParam,
    })
}

function updateRole(roleUpdateParam: RoleUpdateParam): AxiosPromise {
    //修改角色信息
    return request({
        url: '/role/update',
        method: 'post',
        data: roleUpdateParam
    })
}

function addRole(roleAddParam: RoleAddParam): AxiosPromise {
    //修改角色信息
    return request({
        url: '/role/add',
        method: 'post',
        data: roleAddParam,
    })
}

function deleteRoles(roleIds: Array<string>): AxiosPromise {
    //删除选中角色
    return request({
        url: '/role/deletes',
        method: 'post',
        data: roleIds,
    })
}
export default {
    getAllRole,
    getRoles,
    updateRole,
    addRole,
    deleteRoles,
}