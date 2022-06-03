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
      <template v-if="['unitPrice', 'totalPrice'].includes(column.dataIndex)">
        <a-input-number v-if="record.isEdit"
                        v-model:value="record[column.dataIndex]"
                        :min="0"
                        :precision="2"
                        size="small"
                        style="width:150px;"
                        @blur="()=>{if(column.dataIndex=='unitPrice'){record.totalPrice=record.unitPrice*record.count}}">
          <template #addonAfter>
            元
          </template>
        </a-input-number>
        <span v-else>{{ record[column.dataIndex] }}</span>
      </template>
      <template v-if="column.key === 'operation'">
        <el-button plain size="small" type="success" @click="getNote(record)">
          <SvgIcon :iconWidth="17" iconColor="#67c23a" iconName="eye"/>
          备注
        </el-button>
        <el-button v-if="!record.isEdit" size="small" type="primary"
                   @click="()=>{record.isEdit=true}">
          修改报价
        </el-button>
        <el-button v-else size="small" type="primary"
                   @click="()=>{record.isEdit=false;}">
          保存
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
    <a-button type="primary" @click="clickToPrice">确认给出报价</a-button>
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
import {ElMessage} from "element-plus";
import {InquriyPriceParam, InquiryDetail} from "@/type/purchase";


export default defineComponent({
  components: {
    NoteView,
  },
  props: ['combId', 'purchaseId'],
  emits: ['refresh','click-close'],
  setup(props, context) {
    const {proxy}: any = getCurrentInstance()
    let combIdC = ref(props.combId)
    let purchaseIdC = ref(props.purchaseId)
    const details = reactive({
      data: new Array<InquiryDetail>(),
      total: 0,
    })
    onMounted(() => {
      getDetail()
    })
    function getDetail(): void {
      proxy.$api.purchase.getInquiryDetail(purchaseIdC.value)
          .then((response: any) => {
            console.log('&&&&&&', response.data)
            details.data = response.data.data
            details.data.forEach((d: any) => {
              d.isEdit = false;//用于临时保存是否给出报价用的
            })
          })
    }
    const item = ref<TableColumnsType>([
      {title: "明细名", dataIndex: "detailname", key: "detailname", resizable: false},
      {title: "数量", dataIndex: "count", key: "count", resizable: true},
      {title: "期望单价", dataIndex: "predictUnitPrice", key: "predictUnitPrice", resizable: true},
      {title: "期望总价", dataIndex: "predictTotalPrice", key: "predictTotalPrice", resizable: true},
      {
        title: "给出单价", dataIndex: "unitPrice",
        width: '182px', key: "unitPrice", resizable: true
      },
      {
        title: "给出总价", dataIndex: "totalPrice",
        width: '182px', key: "totalPrice", resizable: true
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
    function clickToPrice(): void {
      //点击确认给出报价
      //必须所有明细给出报价
      let detailDeals = new Array<InquriyPriceParam>()
      for (let d of details.data) {
        if (d.predictUnitPrice == null || d.totalPrice == null) {
          ElMessage({
            type: 'warning',
            message: '必须给出每个明细价格'
          })
          return
        }
        detailDeals.push({
          inquiryId: d.inquiryId,
          unitPrice: d.unitPrice,
          totalPrice: d.totalPrice,
        })
      }
      proxy.$api.purchase.setInquiryPrice(detailDeals)
          .then((response: any) => {
            if (response.data.state == proxy.$state.SUCCESS) {
              context.emit('click-close')
              context.emit('refresh')
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
      clickToPrice,
      purchaseIdC,
    }
  }
})
</script>
