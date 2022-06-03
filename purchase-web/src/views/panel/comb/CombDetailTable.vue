<template>
  <a-table
      :columns="item"
      :data-source="details.data"
      :scroll="{ x: 'max-content' }"
      rowKey="detailId"
      :pagination="false"
      @resizeColumn="(w: any, col: any) => { col.width = w; }"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'operation'">
        <el-button type="success" size="small" @click="getNote(record)" plain>
          <SvgIcon iconName="eye" :iconWidth="17" iconColor="#67c23a" />备注
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
  <!-- 添加明细的弹出框 -->
  <a-modal
      v-model:visible="showDetailAdd"
      :destroyOnClose="true"
      title="添加明细"
      :footer="null"
      :closable="false"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
  >
    <DetailAdd
        :apply-id="combIdC"
        @click-cancel="showDetailAdd = false"
        @refresh="() => getDetail()"
    />
  </a-modal>

  <!-- 查看备注的弹出框 -->
  <a-modal
      v-model:visible="visibleNote"
      :destroyOnClose="true"
      title="申请备注"
      :footer="null"
      :closable="true"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
  >
    <NoteView :view-note="viewNote" />
  </a-modal>

  <!-- 修改明细的弹出框 -->
  <a-modal
      v-model:visible="visibleUpdate"
      :destroyOnClose="true"
      title="编辑明细"
      :footer="null"
      :closable="true"
      :keyboard="false"
      :maskClosable="false"
      okText="保存"
      style="width:1000px;"
  >
    <DetailUpdate
        :detail-id="updateDetailId"
        @click-cancel="visibleUpdate=false"
        @refresh="() => getDetail()"/>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, getCurrentInstance, reactive } from "vue";
import DetailAdd from '@/views/panel/detail/DetailAdd.vue'
import { applyCanEditStateList, detailStateMap } from '@/util/state'
import NoteView from '@/components/NoteView.vue'
import DetailUpdate from '@/views/panel/detail/DetailUpdate.vue'
import type { TableColumnsType } from 'ant-design-vue';

export default defineComponent({
  components: {
    DetailAdd,
    NoteView,
    DetailUpdate,
  },
  props: ['combId', 'combState'],
  setup(props) {
    const { proxy }: any = getCurrentInstance()
    let combIdC = ref(props.combId)
    const details = reactive({
      data: [],
      total: 0,
    })
    onMounted(() => {
      getDetail()
    })
    function getDetail(): void {
      proxy.$api.detail.getCombDetails(combIdC.value)
          .then((response: any) => {
            details.data = response.data.data
          })
    }
    const item =  ref<TableColumnsType>([
      // { title: "明细编号", dataIndex: "serialNumber", key: "serialNumber", resizable: true },
      { title: "明细名", dataIndex: "detailname", key: "detailname", resizable: true },
      // { title: "状态", dataIndex: "state", key: "state", resizable: true },
      { title: "类型", dataIndex: "spendingType", key: "spendingType", resizable: true },
      { title: "数量", dataIndex: "count", key: "count", resizable: true },
      { title: "预估单价", dataIndex: "predictUnitPrice", key: "predictUnitPrice", resizable: true },
      { title: "预估总价", dataIndex: "predictTotalPrice", key: "predictTotalPrice", resizable: true },
      // { title: "购买单价", dataIndex: "dealUnitPrice", key: "dealUnitPrice", resizable: true },
      // { title: "购买总价", dataIndex: "dealTotalPrice", key: "dealTotalPrice", resizable: true },
      { title: "单位", dataIndex: "unit", key: "unit", resizable: true },
      // { title: "购买方式", dataIndex: "purchaseWay", key: "purchaseWay", resizable: true },
      { title: "操作", key: "operation", fixed: "right", },
    ]);
    let showDetailAdd = ref(false)
    // let combStateC = ref(props.combState)
    function getNote(record: any): void {
      //查看备注
      viewNote.value = record.note
      visibleNote.value = true
    }
    let visibleNote = ref(false)
    let viewNote = ref('')
    function deleteDetail(detailId: string): void {
      //删除一个明细
      proxy.$api.detail.deleteDetail(detailId)
          .then((response: any) => {
            if (response.data.state === proxy.$state.SUCCESS) {
              getDetail()
            }
          })
    }

    let visibleUpdate = ref(false)
    let updateDetailId = ref('')
    function clickUpdateDetail(record: any): void {
      //点击修改明细的按钮回调
      updateDetailId.value = record.detailId;
      visibleUpdate.value = true
    }
    return {
      combIdC,
      proxy,
      details,
      item,
      showDetailAdd,
      getDetail,
      // combStateC,
      applyCanEditStateList,
      detailStateMap,
      getNote,
      visibleNote,
      viewNote,
      deleteDetail,
      clickUpdateDetail,
      visibleUpdate,
      updateDetailId,
    }
  }
})
</script>
