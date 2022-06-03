<template>
  <a-steps :current="step">
    <a-step title="给出价格"/>
    <a-step title="合同签署"/>
    <a-step title="发货"/>
<!--    <a-step title="确认收货"/>-->
<!--    <a-step title="支付"/>-->
    <a-step title="完成"/>
  </a-steps>
  <div style="height: 20px;" ></div>

  <SupplierProcess1 v-if="step==0" :comb-id="combId" :purchase-id="purchaseId" @refresh="refresh" />
  <SupplierProcess2 v-if="step==1" :comb-id="combId" :purchase-id="purchaseId"/>
  <SupplierProcess3 v-if="step==2" :purchase-id="purchaseId"/>
  <div v-if="step==3">
    此采购已经完成
  </div>

  <div class="flex justify-content-center" style="margin:20px;">
    <a-button :disabled="step==0"
              size="large"
              @click="()=>{if(step>0){--step;}}">
      上一步
    </a-button>
    <a-button :disabled="step==3" size="large" style="margin-left:20px;"
              type="primary" @click="()=>{if(step<3){++step;}}">
      下一步
    </a-button>
  </div>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";
import SupplierProcess1 from "@/views/panel/supplier/process/SupplierProcess1.vue";
import {useRoute} from "vue-router";
import {supplierStateToStepMap} from "@/util/state";
import SupplierProcess2 from "@/views/panel/supplier/process/SupplierProcess2.vue";
import SupplierProcess3 from "@/views/panel/supplier/process/SupplierProcess3.vue";


export default  defineComponent({
  components: {
    SupplierProcess1,
    SupplierProcess2,
    SupplierProcess3,
  },
  setup() {
    const route = useRoute()
    let combId = ref(route.query.combId)
    let purchaseId = ref(route.query.purchaseId)
    let step = ref(supplierStateToStepMap.get(route.query.state as string))

    function refresh(newStep: number): void {
      //更新状态后直接刷新页面,啊,不行,用了标签页直接刷新面板就没了
      step.value = newStep;
    }
    return {
      route,
      combId,
      purchaseId,
      step,
      refresh,
    }
  }
})
</script>

