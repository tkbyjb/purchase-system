<template>
    <el-form
        :model="applyUpdateParam"
        label-width="120px"
        label-position="left"
        :rules="rules"
        ref="ruleFormRef"
    >
        <el-form-item label="申请名" prop="applyname">
            <el-input v-model="applyUpdateParam.applyname" />
        </el-form-item>
        <el-form-item label="备注">
            <MavonEditorCu ref="editor" />
        </el-form-item>
        <el-form-item label="附件(注:需要修改附件时才需要重新上传)">
            <FileUploadDrop ref="upload" :max-count="1" />
        </el-form-item>
        <div class="flex justify-content-center">
            <el-button type="primary" @click="addApply" size="large">修改申请</el-button>
        </div>
    </el-form>
</template>

<script lang="ts">
import { defineComponent, reactive, ref, getCurrentInstance, onMounted } from "vue";
import { ApplyUpdateParam } from '@/type/apply'
import type { FormInstance } from 'element-plus'
import MavonEditorCu from '@/components/MavonEditorCu.vue'
import FileUploadDrop from '@/components/FileUploadDrop.vue'
// import type { UploadProps } from 'ant-design-vue';
import { useRouter, useRoute } from "vue-router";
import { useStore } from "vuex";

// 修改新建个tab,但是修改不加入菜单栏,直接通过vuex通知TabPanelAntd添加一个就行
export default defineComponent({
    components: {
        MavonEditorCu,
        FileUploadDrop,
    },
    setup() {
        const route = useRoute()
        const { proxy }: any = getCurrentInstance()
        const router = useRouter()
        const store = useStore()

        const applyUpdateParam: any = reactive({
            applyId: route.query.applyId,
            applyname: route.query.applyname,
            note: route.query.note,
            attachment: route.query.attachment,
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
            //点击修改申请
            let formData = new FormData()
            applyUpdateParam.note = editor.value.getMd()
            let files = upload.value.handleUpload()
            if (files.length > 0) {
                for (let file of files) {
                    formData.append('files', file.originFileObj);
                }
            }
            formData.set('applyname', applyUpdateParam.applyname)
            formData.set('note', applyUpdateParam.note)
            formData.set('applyId', applyUpdateParam.applyId)

            proxy.$api.apply.update(formData)
                .then((response: any) => {
                    //修改成功关闭修改tab,跳转到此申请的详情页
                    // if (response.data.state == proxy.$state.SUCCESS) {
                    //     setTimeout(() => {
                    //         upload.value.clear()
                    //         editor.value.clear()
                    //         store.commit('removeTab', 'ApplyUpdate')//这里去掉修改tab后当前tab是空的,
                    //         //tabpanel并没有监听到tabValue @change事件没触发,所所以是一片白,应该是这个原因了
                    //         // const tab = {
                    //         //     title: '查看',
                    //         //     name: 'ApplyAdd',
                    //         //     content: 'ApplyAdd',
                    //         // }
                    //         // store.commit('addTab', tab)//新添加一个也是一片白
                    //     }, 1000);

                    // }
                })
        }
        onMounted(() => {
            editor.value.setMd(applyUpdateParam.note)
        })
        return {
            applyUpdateParam,
            rules,
            ruleFormRef,
            editor,
            upload,
            addApply,
            proxy,
            router,
            store,
        }
    }
})
</script>