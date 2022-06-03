<template>
    <a-upload-dragger
        v-model:fileList="files"
        :multiple="false"
        :maxCount="maxCount"
        :before-upload="beforeUpload"
        style="padding-left:40px;padding-right:40px;"
    >
        <p class="ant-upload-drag-icon">
            <SvgIcon iconName="upload" :iconWidth="60" iconColor="#3b82f6" />
        </p>
        <p class="ant-upload-text">拖拽或点击上传文件</p>
        <p class="ant-upload-hint">注意:文件不大于4MB</p>
    </a-upload-dragger>
</template>

<script lang="ts">
import { defineComponent, ref, Ref } from "vue";
import { ElMessage } from 'element-plus'
import type { UploadProps } from 'ant-design-vue';

export default defineComponent({
    props: ['maxCount'],
    setup() {
        const files = ref<UploadProps['fileList']>([]);
        // const beforeUpload = (file: any, fileList: any) => {
        //     //取消掉文件自动上传
        //     if (file.size > 4000000) {
        //         ElMessage({
        //             message: '文件不大于4MB',
        //             type: 'warning',
        //         })
        //         return false;
        //     }
        //     files.value = [...fileList];
        //     return false;
        // };
        const beforeUpload: UploadProps['beforeUpload'] = file => {
            if (file.size > 4000000) {
                ElMessage({
                    message: '文件不大于4MB',
                    type: 'warning',
                })
                return false;
            }
            files.value = [file];
            return false;
        };
        const handleUpload = () => {
            //手动上传文件,这里不上传,只返回files
            // const formData = new FormData();
            // files.value.forEach((file: any) => {
            //     formData.append('files', file as any);
            // });
            return files.value
        };
        function clear(): void{
            //清除文件
            files.value = []
        }
        return {
            beforeUpload,
            handleUpload,
            files,
            clear,
        }
    }
})
</script>