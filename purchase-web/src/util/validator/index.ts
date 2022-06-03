//校验规则
export const  validatorSelect = (rule: any, value: any, callback: any): any => {
    //校验下拉框是否选择,下拉框不能立即检查,只能等全局检查才能查到,不知道是不是个bug
    if(value == null) {
        callback(new Error('请选择'))
    } else {
        callback()
    }
}
