<template>
  <el-descriptions :column="4" border size="large" title="采购单详情">
    <el-descriptions-item :span="4">
      <template #label>采购明细</template>
      <a-table
          :columns="item"
          :data-source="details.data"
          :pagination="false"
          :scroll="{ x: 'max-content' }"
          rowKey="detailId"
          @resizeColumn="(w: any, col: any) => { col.width = w; }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'state'">
                <span>
                    <a-tag
                        :key="record.state"
                        :color="detailStateMap.get(record.state)?.tagColor"
                    >{{ detailStateMap.get(record.state)?.mess }}</a-tag>
                </span>
          </template>
        </template>
      </a-table>
    </el-descriptions-item>
    <el-descriptions-item :span="4">
      <template #label>总价(元)</template>
      {{ totalPrice }}
    </el-descriptions-item>
    <el-descriptions-item :span="4">
      <template #label>采购合同</template>
      <a :href="contractUrl" target="_blank">
      <el-button size="small" type="text">下载合同</el-button>
      </a>
    </el-descriptions-item>
  </el-descriptions>

  <div class="flex justify-content-center" style="margin:30px;">
    <a-button type="primary" @click="toPay" size="large">支付</a-button>
  </div>

</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {applyCanEditStateList, detailStateMap} from '@/util/state'
import type {TableColumnsType} from 'ant-design-vue';
import {Detail} from "@/type/detail";

export default defineComponent({
  components: {
  },
  props: ['combId', 'purchaseId'],
  emits: ['refresh', 'click-close'],
  setup(props, context) {
    const {proxy}: any = getCurrentInstance()
    let combIdC = ref(props.combId)
    let purchaseIdC = ref(props.purchaseId)
    const details = reactive({
      data: new Array<Detail>(),
      total: 0,
    })
    onMounted(() => {
      getDetail()
    })
    let totalPrice = ref(0)

    function getDetail(): void {
      proxy.$api.detail.getCombDetails(combIdC.value)
          .then((response: any) => {
            details.data = response.data.data
            details.data.forEach((d: any) => {
              totalPrice.value += d.dealTotalPrice
            })
          })
    }
    const item = ref<TableColumnsType>([
      {title: "明细名", dataIndex: "detailname", key: "detailname", resizable: false},
      {title: "类型", dataIndex: "spendingType", key: "spendingType", resizable: true},
      {title: "数量", dataIndex: "count", key: "count", resizable: true},
      {title: "单位", dataIndex: "unit", key: "unit", resizable: true},
      {
        title: "单价(元)", dataIndex: "dealUnitPrice",
        width: '182px', key: "dealUnitPrice", resizable: true
      },
      {
        title: "总价(元)", dataIndex: "dealTotalPrice",
        width: '182px', key: "dealTotalPrice", resizable: true
      },
    ]);

    function toPay(): void {
      //点击支付
      proxy.$api.purchase.process8(purchaseIdC.value)
      .then((response: any)=>{
        if(response.data.state == proxy.$state.SUCCESS) {
          setTimeout(()=>{
            context.emit('refresh')
            context.emit('click-close')
          },1000)
        }
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
      combIdC,
      proxy,
      details,
      item,
      getDetail,
      applyCanEditStateList,
      detailStateMap,
      purchaseIdC,
      totalPrice,
      toPay,
      contractUrl,
    }
  }
})
</script>
