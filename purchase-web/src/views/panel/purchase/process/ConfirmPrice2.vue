<template>
  <a-table
      :columns="item"
      :data-source="details.data"
      :pagination="false"
      :scroll="{ x: 'max-content' }"
      rowKey="detailId"
      @resizeColumn="(w: any, col: any) => { col.width = w; }"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'operation'">
        <el-button plain size="small" type="success" @click="getNote(record)">
          <SvgIcon :iconWidth="17" iconColor="#67c23a" iconName="eye"/>
          备注
        </el-button>
      </template>
      <template v-else-if="column.key === 'state'">
                <span>
                    <a-tag
                        :key="record.state"
                        :color="detailStateMap.get(record.state)?.tagColor"
                    >{{ detailStateMap.get(record.state)?.mess }}</a-tag>
                </span>
      </template>
    </template>
  </a-table>

  <a-card class="flex justify-content-center">
    <a-button type="primary" @click="clickConfirmPrice">确认价格</a-button>
    <a-button style="margin-left:15px;margin-right:15px;" danger @click="refusePrice">拒绝价格</a-button>
    <a-button @click="changeSupplier">换供应商</a-button>
  </a-card>

  <!-- 查看备注的弹出框 -->
  <a-modal
      v-model:visible="visibleNote"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
      title="申请备注"
  >
    <NoteView :view-note="viewNote"/>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {applyCanEditStateList, detailStateMap} from '@/util/state'
import NoteView from '@/components/NoteView.vue'
import type {TableColumnsType} from 'ant-design-vue';
import {Detail} from "@/type/detail";

export default defineComponent({
  components: {
    NoteView,
  },
  props: ['combId', 'purchaseId'],
  emits: ['refresh'],
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

    function getDetail(): void {
      proxy.$api.detail.getCombDetails(combIdC.value)
          .then((response: any) => {
            console.log(response.data.data)
            details.data = response.data.data
            details.data.forEach((d: any) => {
              d.isEdit = false;//用于临时保存是否给出报价用的
            })
          })
    }

    const item = ref<TableColumnsType>([
      {title: "明细名", dataIndex: "detailname", key: "detailname", resizable: false},
      {title: "类型", dataIndex: "spendingType", key: "spendingType", resizable: true},
      {title: "数量", dataIndex: "count", key: "count", resizable: true},
      {title: "期望单价", dataIndex: "predictUnitPrice", key: "predictUnitPrice", resizable: true},
      {title: "期望总价", dataIndex: "predictTotalPrice", key: "predictTotalPrice", resizable: true},
      {
        title: "给出单价", dataIndex: "dealUnitPrice",
        width: '182px', key: "dealUnitPrice", resizable: true
      },
      {
        title: "给出总价", dataIndex: "dealTotalPrice",
        width: '182px', key: "dealTotalPrice", resizable: true
      },
      {title: "单位", dataIndex: "unit", key: "unit", resizable: true},
      {title: "操作", key: "operation", fixed: "right",},
    ]);
    let showDetailAdd = ref(false)

    function getNote(record: any): void {
      //查看备注
      viewNote.value = record.note
      visibleNote.value = true
    }

    let visibleNote = ref(false)
    let viewNote = ref('')

    function clickConfirmPrice(): void {
      //点击确认价格
      proxy.$api.purchase.process3(purchaseIdC.value, 1)
          .then((response: any) => {
            if (response.data.state == proxy.$state.SUCCESS) {
              // context.emit('refresh', 1)
            }
          })
    }
    function refusePrice(): void{
      //点击拒绝价格
      proxy.$api.purchase.process3(purchaseIdC.value, 2)
          .then((response: any) => {
            if (response.data.state == proxy.$state.SUCCESS) {
              // context.emit('refresh', 1)
            }
          })
    }
    function changeSupplier(): void{
      //点击换供应商
      proxy.$api.purchase.process3(purchaseIdC.value, 3)
          .then((response: any) => {
            if (response.data.state == proxy.$state.SUCCESS) {
              // context.emit('refresh', 1)
            }
          })
    }
    return {
      combIdC,
      proxy,
      details,
      item,
      showDetailAdd,
      getDetail,
      applyCanEditStateList,
      detailStateMap,
      getNote,
      visibleNote,
      viewNote,
      clickConfirmPrice,
      purchaseIdC,
      refusePrice,
      changeSupplier,
    }
  }
})
</script>
