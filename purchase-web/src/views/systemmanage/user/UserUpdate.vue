<template>
    <div class="flex justify-content-center grid">
        <el-form :model="updateParam" label-width="120px" :rules="rules" ref="ruleFormRef">
            <el-form-item label="用户名" prop="username">
                <el-input v-model="updateParam.username" />
            </el-form-item>
            <el-form-item label="账号状态" prop="state">
                <a-select
                    v-model:value="updateParam.state"
                    size="default"
                    placeholder="账号状态"
                    style="width: 100px"
                    class="search-param-item"
                >
                    <a-select-option
                        v-for="(item) in userStateMap"
                        :key="item[0]"
                        :value="item[1].value"
                    >{{ item[1].mess }}</a-select-option>
                </a-select>
            </el-form-item>
            <el-form-item label="用户头像(需要修改时才重新上传)">
                <a-upload
                    v-model:file-list="updateParam.fileList"
                    action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
                    list-type="picture-card"
                    :maxCount="1"
                    :beforeUpload="beforeUpload"
                >
                    <div v-if="updateParam.fileList && updateParam.fileList.length < 1">
                        <plus-outlined />
                        <div style="margin-top: 8px">上传图片</div>
                    </div>
                </a-upload>
            </el-form-item>
            <el-form-item label="注册邮箱" prop="email">
                <el-input v-model="updateParam.email" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input v-model="updateParam.password" type="password" show-password />
            </el-form-item>
            <el-form-item label="电话号码" prop="tel">
                <el-input v-model="updateParam.tel" />
            </el-form-item>
            <el-form-item label="真实姓名" prop="realname">
                <el-input v-model="updateParam.realname" />
            </el-form-item>
            <el-form-item label="身份证号" prop="idNumber">
                <el-input
                    v-model="updateParam.idNumber"
                    @blur.prevent="() => { updateParam.birth = updateParam.idNumber.substring(6, 10) + '-' + updateParam.idNumber.substring(10, 12) + '-' + updateParam.idNumber.substring(12, 14) }"
                />
            </el-form-item>
            <el-form-item label="出生日期" prop="birth">
                <el-date-picker
                    v-model="updateParam.birth"
                    type="date"
                    placeholder="选择出生日期"
                    format="YYYY-MM-DD"
                />
            </el-form-item>
            <el-form-item label="性别">
                <el-switch
                    v-model="updateParam.sex"
                    size="large"
                    :active-value="1"
                    :inactive-value="0"
                    active-text="男"
                    inactive-text="女"
                />
            </el-form-item>
            <el-form-item label="角色" prop="roleId">
                <el-select v-model="updateParam.roleId" class="m-2" placeholder="选择角色" size="large">
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
                    v-model="updateParam.departmentId"
                    class="m-2"
                    placeholder="选择部门"
                    size="large"
                >
                    <el-option
                        v-for="item in departments"
                        :key="item.departmentId"
                        :label="item.departmentname"
                        :value="item.departmentId"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="开启邮件提醒">
                <a-switch
                    v-model:checked="updateParam.emailNotice"
                    :checkedValue="1"
                    :unCheckedChildren="0"
                />
            </el-form-item>
            <div style="text-align: center;margin-top:40px;">
                <el-button type="primary" size="large" @click="submitForm(ruleFormRef)">保存修改</el-button>
            </div>
        </el-form>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, getCurrentInstance, onMounted, toRaw } from 'vue'
import { UserUpdateParam } from '@/type/user'
import { DepartmentName } from '@/type/department'
import { RoleName } from '@/type/role'
import { AxiosResponse } from 'axios'
import type { FormInstance } from 'element-plus'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import type { UploadProps } from 'ant-design-vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { userStateMap } from '@/util/state'

export default defineComponent({
    components: {
        PlusOutlined,
    },
    props: ['user'],
    emits: ['click-close', 'refresh'],
    setup(props, context) {
        const { proxy }: any = getCurrentInstance()
        const router = useRouter()
        const ruleFormRef = ref<FormInstance>()
        const updateParam: UserUpdateParam = reactive({
            userId: props.user.userId,
            username: props.user.username,
            email: props.user.email,
            password: props.user.password,
            tel: props.user.tel,
            realname: props.user.realname,
            sex: props.user.sex,
            idNumber: props.user.idNumber,
            birth: props.user.birth,
            departmentId: props.user.departmentId,
            roleId: props.user.roleId,
            state: props.user.state,
            emailNotice: props.user.emailNotice,
            fileList: [
                {
                    uid: '-1',
                    name: 'image.png',
                    status: 'done',
                    url: props.user.avatar,
                },
            ],
        })
        const oldPassword = ref(props.user.password)
        const oldAvatar = ref(props.user.avatar)
        const departments: Array<DepartmentName> = reactive([])
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
                { required: true, pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]{2,128}$/, message: '请输入2-128个数字/大小写字母/下划线/中文字符', trigger: 'blur' },
            ],
            email: [
                { required: true, pattern: /^[A-Za-z0-9\u4e00-\u9fa5]{1,64}@[a-zA-Z0-9_-]{1,64}(\.[a-zA-Z0-9_-]{1,64}){1,64}$/, message: '请输入正确邮箱', trigger: 'blur' },
            ],
            password: [
                { required: true, pattern: /^[a-zA-Z0-9_]{6,128}$/, message: '请输入6-128个数字/大小写字母/下划线', trigger: 'blur' },
            ],
            tel: [
                { required: true, pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/, message: '请输入11位正确手机号', trigger: 'blur' },
            ],
            realname: [
                { required: true, pattern: /^[\u4e00-\u9fa5]{2,6}$/, message: '请输入中文真实姓名', trigger: 'blur' },
            ],
            idNumber: [
                { required: true, pattern: /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: '请输入正确的中国居民身份证号', trigger: 'blur' },
            ],
            birth: [
                { required: true, message: '请输入出生日期', trigger: 'blur' },
                // {pattern: /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/, message: '请输入出生日期', trigger: 'blur'}
            ],
            roleId: [
                { required: true, pattern: /^[0-9]{1,30}$/, message: '请选择角色', trigger: 'blur' },
            ],
            departmentId: [
                { required: true, pattern: /^[0-9]{1,30}$/, message: '请选择部门', trigger: 'blur' },
            ],
        })
        const submitForm = async (formEl: FormInstance | undefined) => {
            //点击修改用户
            if (!formEl) return
            await formEl.validate((valid, fields) => {
                if (valid) {
                    let formData = new FormData()
                    //通过验证
                    if (oldPassword.value != updateParam.password) {//改了密码才加密
                        updateParam.password = proxy.$md5(updateParam.password)
                    }
                    //判断头像是否是重新上传的
                    if (updateParam.fileList && updateParam.fileList?.length > 0) {
                        if (updateParam.fileList[0].url === oldAvatar.value) {//没改头像
                            // updateParam.fileList = undefined;
                        } else {
                            for (let file of updateParam.fileList) {
                                formData.append('files', file.originFileObj as Blob);
                            }
                        }
                    }
                    for (let e of Object.entries(toRaw(updateParam))) {
                        if (e[0] != 'files' && e[0] != 'fileList') {
                            console.log(e)
                            formData.append(e[0], e[1])
                        }
                    }
                    proxy.$api.user.updateUser(formData)
                        .then((response: any) => {
                            if (response.data.state == proxy.$state.SUCCESS) {
                                //修改成功,关闭修改页面
                                setTimeout(() => {
                                    context.emit('refresh');
                                    context.emit('click-close')
                                }, 1000)
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
        onMounted(() => {
            getRoles()
            getDepartments()
        })
        const beforeUpload: UploadProps['beforeUpload'] = file => {
            if (file.size > 4000000) {
                ElMessage({
                    message: '文件不大于4MB',
                    type: 'warning',
                })
                return false;
            }
            if (file.type.indexOf('image') == -1) {
                ElMessage({
                    message: '只能上传图片类型文件',
                    type: 'warning',
                })
                return false;
            }
            updateParam.fileList = [file];
            return false;
        };
        return {
            updateParam,
            departments,
            getRoles,
            proxy,
            roles,
            getDepartments,
            ruleFormRef,
            rules,
            submitForm,
            router,
            userStateMap,
            beforeUpload,
            oldPassword,
            oldAvatar,
        }
    }
})
</script>
