<template>
    <el-form
        :model="applyAddParam"
        label-width="120px"
        label-position="left"
        :rules="rules"
        ref="ruleFormRef"
    >
        <el-form-item label="申请名" prop="applyname">
            <el-input v-model="applyAddParam.applyname" />
        </el-form-item>
        <el-form-item label="备注">
            <MavonEditorCu ref="editor" />
        </el-form-item>
        <el-form-item label="附件">
            <FileUploadDrop ref="upload" :max-count="1" />
        </el-form-item>
        <div class="flex justify-content-center">
            <el-button type="primary" @click="addApply" size="large">添加申请</el-button>
        </div>
    </el-form>
</template>

<script lang="ts">
import { defineComponent, reactive, ref, getCurrentInstance } from "vue";
import { ApplyAddParam } from '@/type/apply'
import type { FormInstance } from 'element-plus'
import MavonEditorCu from '@/components/MavonEditorCu.vue'
import FileUploadDrop from '@/components/FileUploadDrop.vue'
// import type { UploadProps } from 'ant-design-vue';
import { useRouter } from "vue-router";

export default defineComponent({
    components: {
        MavonEditorCu,
        FileUploadDrop,
    },
    setup() {
        const { proxy }: any = getCurrentInstance()
        const router = useRouter()
        const applyAddParam: ApplyAddParam = reactive({
            applyname: '',
            note: '',
            attachment: '',
        })
        const ruleFormRef = ref<FormInstance>()
        const rules = reactive({
            applyname: [
                { required: true, message: '请输入2-128个数字/大小写字母/下划线/中文字符的申请名', trigger: 'blur' },
                { pattern: /^[\s\S\u4e00-\u9fa5]{2,128}$/, message: '请输入2-128个数字/大小写字母/下划线/中文字符的申请名', trigger: 'blur' },
            ],
        })
        const editor = ref()
        const upload = ref()
        function addApply(): void {
            //点击添加申请
            applyAddParam.note = editor.value.getMd()
            let files = upload.value.handleUpload()
            let formData = new FormData()
            // 官方写法,但是获取不到
            // files.forEach((file: UploadProps['fileList'][number]) => {
            //     formData.append('files', file as any);
            // });
            for(let file of files){
                formData.append('files', file.originFileObj);
            }
            formData.set('applyname', applyAddParam.applyname)
            formData.set('note', applyAddParam.note)
            proxy.$api.apply.add(formData)
            .then((response: any)=>{
                //添加成功清除输入数据(因为组件会缓存)跳转到此申请的详情页
                if(response.data.state==proxy.$state.SUCCESS) {
                    applyAddParam.applyname = ''
                    applyAddParam.note = ''
                    upload.value.clear()
                    editor.value.clear()
                    // router.push({
                    //     path: '',
                    // })
                }
            })
        }
        return {
            applyAddParam,
            rules,
            ruleFormRef,
            editor,
            upload,
            addApply,
            proxy,
            router,
        }
    }
})
</script>