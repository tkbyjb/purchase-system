import request from '@/api/request'
import {AxiosPromise} from 'axios'
import {DepartmentGetParam, DepartmentUpdateParam, DepartmentAddParam} from '@/type/department'

function getAllDepartmentName(): AxiosPromise{
    //一次性只获取所有部门的部门名和部门id
    return request({
        url: '/public/department/get/all/name',
        method: 'get',
    })
}


function get(departmentGetParam: DepartmentGetParam): AxiosPromise{
    //系统管理员获取部门数据
    return request({
        url: '/department/get/all',
        method: 'post',
        data: departmentGetParam,
    })
}

function updateDepartment(departmentUpdateParam: DepartmentUpdateParam): AxiosPromise{
    //修改部门
    return request({
        url: '/department/update',
        method: 'post',
        data: departmentUpdateParam,
    })
}

function deleteDepartments(departmentIds: Array<string>): AxiosPromise{
    //删除选中部门
    return request({
        url: '/department/deletes',
        method: 'post',
        data: departmentIds
    })
}

function addDepartment(departmentAddParam: DepartmentAddParam): AxiosPromise{
    //添加一个部门
    return request({
        url: '/department/add',
        method: 'post',
        data: departmentAddParam,
    })
}

function getBalance(departmentGetParam: DepartmentGetParam): AxiosPromise {
    //获取部门剩余经费
    return request({
        url: '/department/balance',
        method: 'post',
        data: departmentGetParam,
    })
}


export default {
    getAllDepartmentName,
    get,
    updateDepartment,
    deleteDepartments,
    addDepartment,
    getBalance,
}