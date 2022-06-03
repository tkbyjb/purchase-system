import { PageParam } from "./public";

export interface DepartmentName {
    //部门列表,只有部门名和部门id
    departmentId: string;
    departmentname: string;
}

export interface DepartmentGetParam {
    //获取部门完整信息的参数
    departmentname: string|null;
    pageParam: PageParam,
}


export interface DepartmentUpdateParam {
    //修改部门的参数
    departmentId: string;
    departmentname: string;
    type: number;
    balance: number;
    note: string|null;
}

export interface DepartmentAddParam {
    //添加部门的参数
    departmentname: string;
    type: number;
    balance: number;
    note: string|null;
}

// export interface DepartmentBalanceGetParam {
//     //部门经费查询参数

// }