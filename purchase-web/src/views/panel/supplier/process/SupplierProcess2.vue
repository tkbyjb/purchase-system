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
    </el-descriptions-item>
    <el-descriptions-item :span="4">
      <template #label>总价(元)</template>
      {{ totalPrice }}
    </el-descriptions-item>
    <el-descriptions-item :span="4">
      <template #label>上传合同</template>
      <FileUploadDrop :max-count="1" ref="upload"/>
    </el-descriptions-item>
  </el-descriptions>

  <div class="flex justify-content-center" style="margin:30px;">
    <a-button type="primary" @click="uploadContract">确认订单并上传合同给采购方</a-button>
  </div>

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
import FileUploadDrop from "@/components/FileUploadDrop.vue";
import {ElMessage} from "element-plus";

export default defineComponent({
  components: {
    NoteView,
    FileUploadDrop,
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
    let totalPrice = ref(0)

    function getDetail(): void {
      proxy.$api.detail.getCombDetails(combIdC.value)
          .then((response: any) => {
            details.data = response.data.data
            details.data.forEach((d: any) => {
              d.isEdit = false;//用于临时保存是否给出报价用的
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

    let upload = ref()
    function uploadContract(): void{
      //点击确认发送合同
      //检查是否选择了文件
      let formData = new FormData()
      let files = upload.value.handleUpload()
      if(files.length < 1){
        ElMessage({
          type: 'warning',
          message: '必须上传合同文件'
        })
        return
      }
      for(let file of files){
        formData.append('files', file.originFileObj);
      }
      formData.set("purchaseId", purchaseIdC.value);
      proxy.$api.purchase.process4(formData)
      .then((response: any)=>{
        //
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
      purchaseIdC,
      totalPrice,
      upload,
      uploadContract,
    }
  }
})
</script>
