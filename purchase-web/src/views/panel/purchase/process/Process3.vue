<template>
  <el-form>
    <el-form-item label="下载供应商的合同">
      <a :href="contractUrl" target="_blank">
      <a-button type="primary">下载合同</a-button>
      </a>
    </el-form-item>
    <el-form-item label="上传签署过的合同">
      <FileUploadDrop ref="upload" :max-count="1"/>
    </el-form-item>
    <div style="margin-left:100px;">
      <a-button danger @click="clickUpload">确认上传</a-button>
    </div>
  </el-form>

</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, ref} from "vue";
import FileUploadDrop from "@/components/FileUploadDrop.vue";
import {ElMessage} from "element-plus";

export default defineComponent({
  components: {
    FileUploadDrop
  },
  props: ['purchaseId'],
  setup(props) {
    const {proxy}: any = getCurrentInstance()
    let upload = ref()
    let purchaseIdC = ref(props.purchaseId)

    function clickUpload(): void {
      //点击上传签署过的合同
      let formData = new FormData()
      let files = upload.value.handleUpload()
      if (files.length < 1) {
        ElMessage({
          type: 'warning',
          message: '必须上传合同文件'
        })
        return
      }
      for (let file of files) {
        formData.append('files', file.originFileObj);
      }
      formData.set("purchaseId", purchaseIdC.value);
      proxy.$api.purchase.process5(formData)
          .then((response: any) => {
            //
          })
    }

    let contractUrl = ref('')
    onMounted(()=>{
      //获取合同链接
      proxy.$api.file.getContractUrl(purchaseIdC.value)
      .then((response: any)=>{
        console.log('*********',response.data)
        contractUrl.value = response.data.data
      })
    })
    return {
      clickUpload,
      upload,
      purchaseIdC,
      contractUrl,
    }
  }
})
</script>
