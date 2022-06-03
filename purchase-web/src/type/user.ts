import { PageParam, OrderBy } from '@/type/public'
import type { UploadProps } from 'ant-design-vue';
import FileType from 'ant-design-vue'

export interface RegisterParam {
    //用户自己注册需要的参数
    username: string;
    email: string;
    password: string;
    tel: string;
    realname: string;
    sex: number;
    idNumber: string;
    birth: string;
    departmentId: string;
    verifyCode: string;
    roleId: string;
}

export interface LoginParam {
    //用户登录参数
    userUniqueId: string;
    password: string;
}

export interface UserGetParam {
    //系统管理员查询用户的参数
    pageParam: PageParam;
    orderBys: Array<OrderBy>;
    username: string|null;
    realname: string|null;
    sex: number|null;
    tel: string|null;
    idNumber: string|null;
    startCreateTime: string|null;
    endCreateTime: string|null;
    roleId: string|null;
    departmentId: string|null;
    state: number|null;

}
export interface UserUpdateParam {
    //修改用户的参数
    userId: string;
    username: string;
    email: string;
    password: string;
    tel: string;
    realname: string;
    sex: number;
    idNumber: string;
    birth: string;
    departmentId: string;
    roleId: string;
    state: number;
    emailNotice: number;
    fileList: UploadProps['fileList'];
    files?: Array<any>;
}

export interface UserAddParam {
    //系统管理员添加用户的参数
    username: string;
    email: string;
    password: string;
    tel: string;
    realname: string;
    sex: number;
    idNumber: string;
    birth: string;
    departmentId: string;
    roleId: string;
    emailNotice: number;
    fileList: UploadProps['fileList'];
    files?: Array<any>;
}

export interface AccountSettingParam {
    //修改用户的参数
    userId: string;
    username: string;
    email: string;
    password: string;
    tel: string;
    emailNotice: number;
    fileList: UploadProps['fileList'];
    files?: Array<any>;
}
