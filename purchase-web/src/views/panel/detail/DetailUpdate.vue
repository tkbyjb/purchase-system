<template>
    <el-form
        :model="detailUpdateParam"
        label-width="120px"
        label-position="left"
        :rules="rules"
        ref="ruleFormRef"
    >
        <el-form-item label="明细名" prop="detailname">
            <el-input v-model="detailUpdateParam.detailname" />
        </el-form-item>
        <el-form-item label="数量" prop="count">
            <el-input-number
                v-model="detailUpdateParam.count"
                :min="1"
                :max="10000"
                controls-position="right"
                @blur="detailUpdateParam.predictTotalPrice=detailUpdateParam.count*detailUpdateParam.predictUnitPrice"
            />
        </el-form-item>
        <el-form-item label="预估单价" prop="predictUnitPrice">
            <el-input-number
                v-model="detailUpdateParam.predictUnitPrice"
                :min="0"
                controls-position="right"
                @blur="detailUpdateParam.predictTotalPrice=detailUpdateParam.count*detailUpdateParam.predictUnitPrice"
            />
        </el-form-item>
        <el-form-item label="预估总价" prop="predictTotalPrice">
            <el-input-number
                v-model="detailUpdateParam.predictTotalPrice"
                :min="0"
                controls-position="right"
            />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
            <el-input v-model="detailUpdateParam.unit" style="width:150px;"/>
        </el-form-item>
        <el-form-item label="类型" prop="spendingTypeId">
            <el-select
                v-model="detailUpdateParam.spendingTypeId"
                placeholder="选择类型"
                style="width: 200px"
                class="search-param-item"
                size="large"
            >
                <el-option
                    v-for="(item) in spendingTypes"
                    :key="item"
                    :value="item.spendingTypeId"
                    :label="item.typename"
                />
            </el-select>
        </el-form-item>
        <el-form-item label="备注">
            <MavonEditorCu ref="editor" />
        </el-form-item>
        <div class="flex justify-content-center">
            <el-button type="primary" @click="cancel" size="large" plain>取消</el-button>
            <el-button type="primary" @click="updateDetail(ruleFormRef)" size="large">修改</el-button>
        </div>
    </el-form>
</template>

<script lang="ts">
import { defineComponent, reactive, ref, getCurrentInstance, onMounted } from "vue";
import type { FormInstance } from 'element-plus'
import { SpendingType } from '@/type/spending'
import MavonEditorCu from '@/components/MavonEditorCu.vue'
import {validatorSelect} from '@/util/validator'
import { ElMessage } from 'element-plus'
import { DetailUpdateParam } from '@/type/detail'

export default defineComponent({
    components: {
        MavonEditorCu,
    },
    emits: ['click-cancel','refresh'],
    props: ['detailId'],
    setup(props, context) {
        const ruleFormRef = ref<FormInstance>()
        const { proxy }: any = getCurrentInstance()
        
        const detailUpdateParam: DetailUpdateParam = reactive({
            detailId: props.detailId,
            detailname: '',
            count: 1,
            predictUnitPrice: 0,
            predictTotalPrice: 0,
            unit: '',
            note: '',
            spendingTypeId: null,
        })
        const rules = reactive({
            detailname: [
                { required: true, pattern: /^[\s\S\u4e00-\u9fa5]{2,128}$/, message: '请输入2-128个数字/大小写字母/下划线/中文字符的明细名', trigger: 'blur' },
            ],
            count: [
                { required: true,type: 'number', min: 1, max:10000, message: '请输入1-1000的数量', trigger: 'blur' },
            ],
            predictUnitPrice: [
                { required: true,type: 'number', min: 0, message: '请输入预估单价', trigger: 'blur' },
            ],
            predictTotalPrice: [
                { required: true,type: 'number', min: 0, message: '请输入预估总价', trigger: 'blur' },
            ],
            unit: [
                { required: true,pattern: /^[\w\u4e00-\u9fa5]{1,10}$/, message: '请输入1-10个大小写字母/中文字符的单位', trigger: 'blur' },
            ],
            spendingTypeId: [
                { required: true,validator: validatorSelect, message: '请选择明细类型', trigger: ['blur','change'] },
            ]
        })
        let spendingTypes = ref<Array<SpendingType>>([])
        function getSpendingTypes(): void {
            //获取支出类型列表
            proxy.$api.spending.getSpendingTypes()
                .then((response: any) => {
                    spendingTypes.value = response.data.data
                })
        }
        onMounted(() => {
            getSpendingTypes()
            //获取此明细详细信息(因为detail表只有部分信息,所以需要重新获取,不像备注和申请修改,字段少)
            proxy.$api.detail.getByDetailId(props.detailId)
            .then((response: any)=>{
                Object.assign(detailUpdateParam, response.data.data)
                editor.value.setMd(detailUpdateParam.note)
            })
        })
        function cancel(): void{
            context.emit("click-cancel")
        }
        const editor = ref()
        async function updateDetail(formEl: FormInstance | undefined): Promise<any>{
            //修改明细
            if (!formEl) return
            await formEl.validate((valid, fields) => {
                if (valid) {
                    //通过验证
                    detailUpdateParam.note = editor.value.getMd()
                    proxy.$api.detail.updateDetail(detailUpdateParam)
                    .then((response: any)=>{
                        if(response.data.state==proxy.$state.SUCCESS){
                            setTimeout(()=>{
                                cancel()
                                context.emit('refresh')
                            },1000)
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
            detailUpdateParam,
            rules,
            ruleFormRef,
            spendingTypes,
            getSpendingTypes,
            proxy,
            cancel,
            updateDetail,
            editor,
        }
    }
})
</script>