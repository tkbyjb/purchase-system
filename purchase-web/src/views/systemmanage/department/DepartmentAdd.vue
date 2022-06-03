<template>
    <el-form
        :model="addParam"
        label-width="120px"
        label-position="left"
        :rules="rules"
        ref="ruleFormRef"
    >
        <el-form-item label="部门名" prop="departmentname">
            <el-input v-model="addParam.departmentname" />
        </el-form-item>
        <el-form-item label="部门类型" prop="type">
            <el-radio :label="0" size="large" border v-model="addParam.type">学院</el-radio>
            <el-radio :label="1" size="large" border v-model="addParam.type">部门</el-radio>
        </el-form-item>
        <el-form-item label="剩余经费" prop="balance">
            <el-input-number
                v-model="addParam.balance"
                :min="0"
                :max="10000000"
                controls-position="right"
            />
        </el-form-item>
        <el-form-item label="备注">
            <el-input v-model="addParam.note" type="textarea" :rows="5" />
        </el-form-item>
        <div class="flex justify-content-center">
            <el-button type="primary" @click="add(ruleFormRef)" size="large">添加部门</el-button>
        </div>
    </el-form>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, ref, reactive } from "vue";
import type { FormInstance } from 'element-plus'
import { ElMessage } from 'element-plus'
import {DepartmentAddParam} from '@/type/department'

export default defineComponent({
    emits: ['click-close', 'refresh'],
    setup(props, context) {
        const { proxy }: any = getCurrentInstance()
        const addParam: DepartmentAddParam = reactive({
            departmentname: '',
            type: 1,
            balance: 0,
            note: '',
        })
        const rules = reactive({
            departmentname: [
                { required: true, pattern: /^[\s\S\u4e00-\u9fa5]{2,128}$/, message: '请输入2-128个数字/大小写字母/下划线/中文字符的部门名', trigger: 'blur' },
            ],
            type: [
                { required: true, message: '', trigger: 'blur' }
            ],
        })
        async function add(formEl: FormInstance | undefined): Promise<any> {
            //点击保存修改
            if (!formEl) return
            await formEl.validate((valid, fields) => {
                if (valid) {
                    //通过验证
                    proxy.$api.department.addDepartment(addParam)
                        .then((response: any) => {
                            console.log(response.data)
                            //修改成功,关闭蒙版
                            if (response.data.state == proxy.$state.SUCCESS) {
                                setTimeout(() => {
                                    context.emit('refresh');
                                    context.emit('click-close');
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
        const ruleFormRef = ref<FormInstance>()
        return {
            addParam,
            rules,
            add,
            proxy,
            ruleFormRef,
        }
    }
})
</script>