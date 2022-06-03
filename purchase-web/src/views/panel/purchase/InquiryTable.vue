<template>
  <el-descriptions :column="4" border size="large" :title="item.suppliername"
                   v-for="(item) in inquirys" :key="item">
    <el-descriptions-item :span="4">
      <template #label>给价情况</template>
      <el-table :data="item.inquiryDetailVos" style="width: 100%">
        <el-table-column label="明细名" prop="detailname" width="180"/>
<!--        <el-table-column label="类型" prop="spendingType" width="180"/>-->
        <el-table-column label="数量" prop="count"/>
        <el-table-column label="预估单价" prop="predictUnitPrice"/>
        <el-table-column label="预估总价" prop="predictTotalPrice"/>
        <el-table-column label="给出单价" prop="unitPrice"/>
        <el-table-column label="给出总价" prop="totalPrice"/>
        <el-table-column label="单位" prop="unit"/>
      </el-table>
    </el-descriptions-item>
  </el-descriptions>
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, ref} from "vue";

export default defineComponent({
  props: ['purchasId'],
  setup(props) {
    const {proxy}: any = getCurrentInstance()
    let purchaseIdC = ref(props.purchasId)

    let inquirys = ref([])
    onMounted(() => {
      proxy.$api.purchase.getInquiryAPurchase(purchaseIdC.value)
          .then((response: any) => {
            console.log("%%%%%%%", response.data)
            inquirys.value = response.data.data
          })
    })
    return {
      purchaseIdC,
      proxy,
      inquirys,
    }
  }
})
</script>
