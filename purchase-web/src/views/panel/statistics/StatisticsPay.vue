<template>
  <div class="flex">
    <el-card style="margin-right: 20px;width:20%;">
      <a-statistic :value="headinfo.userCount" title="系统用户数"/>
    </el-card>
    <el-card style="margin-right: 20px;width:20%;">
      <a-statistic :value="headinfo.supplierCount" title="供应商数"/>
    </el-card>
    <el-card style="margin-right: 20px;width:20%;">
      <a-statistic :value="headinfo.buydPurchaseCount" title="采购完成数"/>
    </el-card>
    <el-card style="margin-right: 20px;width:20%;">
      <a-statistic :value="headinfo.applyCount" title="采购申请数"/>
    </el-card>
    <el-button @click="getData">
      <SvgIcon :icon-width="22" icon-color="#3b82f6" icon-name="reload"/>
    </el-button>
  </div>
  <div class="flex" style="margin-top:30px;">
    <SpendingTypePercentageChart/>
    <PurchaseCountChart style="margin-left:20px;"/>
  </div>
  <div style="margin-top:40px;">
    <ApplyCountChart/>
  </div>

  <div style="height:100px;"></div>

</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, reactive} from "vue";
import SpendingTypePercentageChart from "@/views/panel/statistics/SpendingTypePercentageChart.vue";
import PurchaseCountChart from "@/views/panel/statistics/PurchaseCountChart.vue";
import ApplyCountChart from "@/views/panel/statistics/ApplyCountChart.vue";

export default defineComponent({
  components: {ApplyCountChart, PurchaseCountChart, SpendingTypePercentageChart},
  setup() {
    const {proxy}: any = getCurrentInstance()
    const headinfo = reactive({
      userCount: null,
      supplierCount: null,
      buydPurchaseCount: null,
      applyCount: null,
    })
    function getData(): void {
      proxy.$api.statistics.getHeadinfo()
          .then((response: any)=>{
            console.log(response.data)
            Object.assign(headinfo, response.data.data)
          })
    }
    onMounted(()=>{
      getData()
    })
    return {
      headinfo,
      proxy,
      getData,
    }
  }
})
</script>
