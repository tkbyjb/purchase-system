<template>
    <div class="flex justify-content-center grid">
        <el-form
            :model="registerParam"
            label-width="120px"
            label-position="top"
            class="sm:col-8 md:col-6 lg:col-5 xl:col-3 purchase-register-form"
            :rules="rules"
            ref="ruleFormRef"
        >
            <div class="flex justify-content-between">
                <span
                    style="font-weight: bold;font-size: 130%;color:#7d7d7d;margin-bottom: 30px;"
                >用户注册</span>
                <span>
                    <router-link
                        :to="{
                            path: '/public/supplier/register',
                        }"
                        style="color: #3b82f6"
                    >供应商注册</router-link>
                </span>
            </div>
            <el-form-item label="用户名" prop="username">
                <el-input v-model="registerParam.username">
                    <template #prefix>
                        <SvgIcon iconName="user" :iconWidth="20" iconColor="#3b82f6" />
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="注册邮箱" prop="email">
                <el-input v-model="registerParam.email">
                    <template #prefix>
                        <SvgIcon iconName="email" :iconWidth="20" iconColor="#3b82f6" />
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input v-model="registerParam.password" type="password" show-password>
                    <template #prefix>
                        <SvgIcon iconName="password" :iconWidth="20" iconColor="#3b82f6" />
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="电话号码" prop="tel">
                <el-input v-model="registerParam.tel">
                    <template #prefix>
                        <SvgIcon iconName="tel" :iconWidth="20" iconColor="#3b82f6" />
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="真实姓名" prop="realname">
                <el-input v-model="registerParam.realname">
                    <template #prefix>
                        <SvgIcon iconName="realname" :iconWidth="20" iconColor="#3b82f6" />
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="身份证号" prop="idNumber">
                <el-input
                    v-model="registerParam.idNumber"
                    @blur.prevent="() => { registerParam.birth = registerParam.idNumber.substring(6, 10) + '-' + registerParam.idNumber.substring(10, 12) + '-' + registerParam.idNumber.substring(12, 14) }"
                >
                    <template #prefix>
                        <SvgIcon iconName="idNumber" :iconWidth="20" iconColor="#3b82f6" />
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="出生日期" prop="birth">
                <el-date-picker
                    v-model="registerParam.birth"
                    type="date"
                    placeholder="选择出生日期"
                    format="YYYY-MM-DD"
                />
            </el-form-item>
            <el-form-item label="性别">
                <el-switch
                    v-model="registerParam.sex"
                    size="large"
                    :active-value="1"
                    :inactive-value="0"
                    active-text="男"
                    inactive-text="女"
                />
            </el-form-item>
            <el-form-item label="角色" prop="roleId">
                <el-select
                    v-model="registerParam.roleId"
                    class="m-2"
                    placeholder="选择角色"
                    size="large"
                    @focus.once="getRoles"
                >
                    <el-option
                        v-for="item in roles"
                        :key="item.roleId"
                        :label="item.rolename"
                        :value="item.roleId"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="部门" prop="departmentId">
                <el-select
                    v-model="registerParam.departmentId"
                    class="m-2"
                    placeholder="选择部门"
                    size="large"
                    @focus.once="getDepartments"
                >
                    <el-option
                        v-for="item in departments"
                        :key="item.departmentId"
                        :label="item.departmentname"
                        :value="item.departmentId"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="验证码" prop="verifyCode">
                <div class="flex">
                    <el-input v-model="registerParam.verifyCode" />
                    <el-button
                        type="primary"
                        style="margin-left:5px;"
                        v-if="canGetVerifyCode"
                        @click="clickSendVerifyCode"
                    >获取验证码</el-button>
                    <el-button
                        type="primary"
                        style="margin-left:5px;"
                        v-else
                        disabled
                    >{{ seconds }} 秒后重试</el-button>
                </div>
            </el-form-item>
            <div>
                <router-link
                    :to="{
                        path: '/public/user/login',
                    }"
                    style="color: #3b82f6"
                >已有账号?去登录</router-link>
            </div>
            <div style="text-align: center;margin-top:40px;">
                <el-button type="primary" size="large" @click="submitForm(ruleFormRef)">注册账号</el-button>
            </div>
        </el-form>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, getCurrentInstance } from 'vue'
import { RegisterParam } from '@/type/user'
import { DepartmentName } from '@/type/department'
import { RoleName } from '@/type/role'
import { AxiosResponse } from 'axios'
import type { FormInstance } from 'element-plus'
import { ElMessage } from 'element-plus'
import {useRouter} from 'vue-router'

export default defineComponent({
    setup() {
        const { proxy }: any = getCurrentInstance()
        const router = useRouter()
        const ruleFormRef = ref<FormInstance>()
        const registerParam: RegisterParam = reactive({
            username: '',
            email: '',
            password: '',
            tel: '',
            realname: '',
            sex: 0,
            idNumber: '',
            birth: '',
            departmentId: '',
            verifyCode: '',
            roleId: '',
        })
        const departments: Array<DepartmentName> = reactive([])
        let canGetVerifyCode = ref(true)
        let seconds = ref(60);
        function clickSendVerifyCode(): void {
            //点击发送验证码
            if (!/^[A-Za-z0-9\u4e00-\u9fa5]{1,64}@[a-zA-Z0-9_-]{1,64}(\.[a-zA-Z0-9_-]{1,64}){1,64}$/.test(registerParam.email)) {
                ElMessage({
                    message: '请输入正确邮箱',
                    type: 'warning',
                })
                return;
            }
            proxy.$api.user.getRegisterVerifyCode(registerParam.email)
                .then((response: AxiosResponse) => {
                    if (response.data.state == proxy.$state.SUCCESS) {
                        //验证码发送成功
                        canGetVerifyCode.value = false;
                        const interval = setInterval(function () {
                            seconds.value--;
                            if (seconds.value < 0) {
                                clearInterval(interval)
                                canGetVerifyCode.value = true
                                seconds.value = 60
                            }
                        }, 1000)
                    }
                })
        }
        const roles: Array<RoleName> = reactive([])
        function getRoles(): void {
            //获取角色列表
            proxy.$api.role.getAllRole()
                .then((response: AxiosResponse) => {
                    response.data.data.forEach((item: RoleName) => {
                        roles.push(item)
                    })
                })
        }
        function getDepartments(): void {
            //获取部门列表
            proxy.$api.department.getAllDepartmentName()
                .then((response: AxiosResponse) => {
                    response.data.data.forEach((item: DepartmentName) => {
                        departments.push(item)
                    })
                })
        }
        const rules = reactive({
            username: [
                { required: true, pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]{2,128}$/,message: '请输入2-128个数字/大小写字母/下划线/中文字符', trigger: 'blur' },
            ],
            email: [
                { required: true,pattern: /^[A-Za-z0-9\u4e00-\u9fa5]{1,64}@[a-zA-Z0-9_-]{1,64}(\.[a-zA-Z0-9_-]{1,64}){1,64}$/, message: '请输入正确邮箱', trigger: 'blur' },
            ],
            password: [
                { required: true, pattern: /^[a-zA-Z0-9_]{6,128}$/,message: '请输入6-128个数字/大小写字母/下划线', trigger: 'blur' },
            ],
            tel: [
                { required: true,pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/, message: '请输入11位正确手机号', trigger: 'blur' },
            ],
            realname: [
                { required: true,pattern: /^[\u4e00-\u9fa5]{2,6}$/, message: '请输入中文真实姓名', trigger: 'blur' },
            ],
            idNumber: [
                { required: true,pattern: /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: '请输入正确的中国居民身份证号', trigger: 'blur' },
            ],
            birth: [
                { required: true, message: '请输入出生日期', trigger: 'blur' },
                // {pattern: /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/, message: '请输入出生日期', trigger: 'blur'}
            ],
            roleId: [
                { required: true, pattern: /^[0-9]{1,30}$/, message: '请选择角色', trigger: 'blur' },
            ],
            departmentId: [
                { required: true,pattern: /^[0-9]{1,30}$/, message: '请选择部门', trigger: 'blur' },
            ],
            verifyCode: [{
                required: true, message: '请输入验证码', trigger: 'blur'
            }]
        })
        const submitForm = async (formEl: FormInstance | undefined) => {
            //点击注册账号,注册成功跳转到登录界面
            if (!formEl) return
            await formEl.validate((valid, fields) => {
                if (valid) {
                    //通过验证
                    registerParam.password = proxy.$md5(registerParam.password)
                    proxy.$api.user.register(registerParam)
                    .then((response: any)=>{
                        if(response.data.state==proxy.$state.SUCCESS){
                            router.push({
                                path: '/public/user/login'
                            })
                        }
                    })
                } else {
                    ElMessage({
                        message: '请检查输入是否合法',
                        type: 'warning',
                    })
                }
            })
        }
        return {
            registerParam,
            departments,
            canGetVerifyCode,
            clickSendVerifyCode,
            seconds,
            getRoles,
            proxy,
            roles,
            getDepartments,
            ruleFormRef,
            rules,
            submitForm,
            router,
        }
    }
})
</script>

<style lang="scss" scoped>
.purchase-register-form {
    background-color: white;
    padding: 40px;
    border-radius: 15px;
    border: 2px solid #3b82f6;
    margin-top: 50px;
    margin-bottom: 90px;
}
.grid {
    margin: 0 !important;
}
</style>