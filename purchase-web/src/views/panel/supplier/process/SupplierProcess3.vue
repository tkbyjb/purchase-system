<template>
  <a-card class="flex justify-content-center">
    <div style="margin:20px;">
      <a :href="contractUrl" target="_blank">
      <a-button type="primary">下载最终合同</a-button>
      </a>
    </div>
    <div style="margin:20px;">
      <a-popconfirm
          cancel-text="取消"
          ok-text="确定"
          title="确定已经发货?"
          @cancel="()=>{}"
          @confirm="clickFahuo"
      >
        <a-button danger type="primary">确认发货</a-button>
      </a-popconfirm>
    </div>
  </a-card>


</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, ref} from "vue";

export default defineComponent({
  props: ['purchaseId'],
  setup(props) {
    const {proxy}: any = getCurrentInstance()
    let purchaseIdC = ref(props.purchaseId)

    function clickFahuo(): void{
      //点击确认发货
      proxy.$api.purchase.process6(purchaseIdC.value)
      .then((response: any)=>{
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
      purchaseIdC,
      proxy,
      clickFahuo,
      contractUrl,
    }
  }
})
</script>
