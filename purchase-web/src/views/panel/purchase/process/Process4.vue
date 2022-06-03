<template>
  <a-card class="flex justify-content-center">
    <div class="flex">
      <LeftLabel :width="120" label="财务部支付人"/>
      <a-select
          v-model:value="payUserId"
          size="default"
          placeholder="选择财务部支付人"
          style="width: 200px"
          class="search-param-item"
      >
        <a-select-option v-for="(item) in users" :key="item" :value="item.userId">{{ item.realname }}</a-select-option>
      </a-select>
    </div>
    <div style="margin:20px;">
      <a-popconfirm
          cancel-text="取消"
          ok-text="确定"
          title="确定已经收货?"
          @cancel="()=>{}"
          @confirm="clickShouhuo"
      >
        <a-button danger type="primary">确认收货</a-button>
      </a-popconfirm>
    </div>
  </a-card>


</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, ref} from "vue";
import {ElMessage} from "element-plus";

export default defineComponent({
  props: ['purchaseId'],
  setup(props) {
    const {proxy}: any = getCurrentInstance()
    let purchaseIdC = ref(props.purchaseId)
    let payUserId = ref('')

    function clickShouhuo(): void{
      //点击确认收货
      if(payUserId.value ==null || payUserId.value == ''){
        ElMessage({
          type: 'warning',
          message: '必须选择支付人',
        })
        return
      }
      proxy.$api.purchase.process7(purchaseIdC.value, payUserId.value)
          .then((response: any)=>{
            //
          })
    }
    let users = ref([])
    function getPayUsers(): void {
      //获取所有能支付的用户
      proxy.$api.user.getPayUsers()
      .then((response: any)=>{
        console.log("&&&&&&&", response.data)
        users.value = response.data.data
      })
    }
    onMounted(()=>{
      getPayUsers()
    })
    return {
      purchaseIdC,
      proxy,
      clickShouhuo,
      payUserId,
      getPayUsers,
      users,
    }
  }
})
</script>
