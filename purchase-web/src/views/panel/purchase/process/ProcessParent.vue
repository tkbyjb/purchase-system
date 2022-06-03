<template>
  <a-steps :current="step">
    <a-step title="选择供应商"/>
    <a-step title="确认价格"/>
    <a-step title="签署合同"/>
<!--    <a-step title="发货"/>-->
    <a-step title="确认收货"/>
    <a-step title="支付"/>
    <a-step title="完成"/>
  </a-steps>
  <div style="height: 60px;"></div>
  <div v-if="step==0">
    <ChooseSupplier1 :comb-id="combId" @setSupplier="setSupplier" :purchase-id="purchaseId"/>
    <a-card>
      当前采购选择的供应商:
      <a-tag color="red">{{ param.suppliername }}</a-tag>
      <el-tooltip
          class="box-item"
          content="供应商会根据明细信息给出价格"
          effect="dark"
          placement="right-start"
      >
        <el-button size="small" style="margin-left:40px;" type="warning"
        @click="clickSendInfoToSupplier">发送明细信息</el-button>
      </el-tooltip>
    </a-card>
  </div>

  <div v-if="step==1">
    <ConfirmPrice2 :comb-id="combId" :purchase-id="purchaseId"/>
  </div>

  <div v-if="step==2">
    <Process3 :purchase-id="purchaseId" />
  </div>

  <div v-if="step==3">
    <Process4 :purchase-id="purchaseId" />
  </div>
  <div v-if="step==4">
    等待财务部支付
  </div>
  <div v-if="step==5">
    此采购已经完成
  </div>


  <div class="flex justify-content-center" style="margin:20px;">
    <a-button :disabled="step==0"
              size="large"
              @click="()=>{if(step>0){--step;}}">
      上一步
    </a-button>
    <a-button :disabled="step==5" size="large" style="margin-left:20px;"
              type="primary" @click="()=>{if(step<5){++step;}}">
      下一步
    </a-button>
  </div>
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, reactive, ref} from "vue";
import ChooseSupplier1 from "@/views/panel/purchase/process/ChooseSupplier1.vue";
import {useRoute} from "vue-router";
import {purchaseStateToStepMap} from "@/util/state";
import {ElMessage} from "element-plus";
import ConfirmPrice2 from "@/views/panel/purchase/process/ConfirmPrice2.vue";
import Process3 from "@/views/panel/purchase/process/Process3.vue";
import Process4 from "@/views/panel/purchase/process/Process4.vue";

export default defineComponent({
  components: {
    ChooseSupplier1,
    ConfirmPrice2,
    Process3,
    Process4,
  },
  setup() {
    const route = useRoute()
    const {proxy}: any = getCurrentInstance()
    let step = ref(purchaseStateToStepMap.get(route.query.state as string));
    const stepC = step.value;
    let purchaseId = ref(route.query.purchaseId);
    let combId = ref(route.query.combId);
    const param = reactive({
      supplierId: '',
      suppliername: '',
      canNext: false,
    })

    function setSupplier(supplier: any): void {
      param.supplierId = supplier.supplierId;
      param.suppliername = supplier.suppliername;
    }

    function clickSendInfoToSupplier(): void{
      //点击发送明细信息给选择的供应商
      //修改采购的供应商为当前选中的供应商,发送提醒给供应商
      // 供应商能看到供应商为自己的采购对应的明细和组合信息,和操作员看到的一样
      //操作员点击下一步只能看到等待供应商提供价格,点击下一步,把canNext置为false
      if(param.supplierId == null || param.supplierId == '') {
        ElMessage({
          type: 'warning',
          message: '请先选择供应商'
        })
        return
      }
      proxy.$api.purchase.process1(param.supplierId, purchaseId.value)
      .then((response: any)=>{
        if(response.data.state == proxy.$state.SUCCESS) {
          param.canNext = true
        }
      })
    }
    return {
      route,
      purchaseId,
      combId,
      param,
      setSupplier,
      step,
      purchaseStateToStepMap,
      clickSendInfoToSupplier,
      proxy,
      stepC,
    }
  }
})
</script>
