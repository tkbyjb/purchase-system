<template>
  <a-card style="width:100%;">
    <div
        class="flex align-items-center justify-content-between"
        style="margin-bottom:20px;"
    >
      <spna class="flex align-items-center">
        <SvgIcon :iconWidth="22" iconColor="#5c5c5c" iconName="清单"/>
        <span style="color:#5c5c5c;">数据</span>
        <a-button
            class="flex align-items-center"
            size="small"
            style="margin-left:15px;font-size:70%;"
            type="primary"
            @click="search"
        >
          <SvgIcon :iconWidth="15" iconColor="white" iconName="刷新"/>
          刷新数据
        </a-button>
      </spna>
    </div>
    <div>
      <a-table
          :columns="item"
          :data-source="data.lines"
          :pagination="pagination"
          :scroll="{ x: 'max-content' }"
          rowKey="purchaseId"
          @change="onChange"
          @resizeColumn="(w: any, col: any) => { col.width = w; }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key == 'purchaseWay'">
        <span>
                 <a-tag
                     :key="record"
                     :color="purchaseWayStateMap.get(record.purchaseWay)?.tagColor"
                 >{{ purchaseWayStateMap.get(record.purchaseWay)?.mess }}</a-tag>
               </span>
          </template>
          <template v-if="column.key == 'state'">
        <span>
                 <a-tag
                     :key="record"
                     :color="inquiryStateMap.get(record.state)?.tagColor"
                 >{{ inquiryStateMap.get(record.state)?.mess }}</a-tag>
               </span>
          </template>
          <template v-if="column.key === 'operation'">
            <el-button size="small" type="primary" @click="toPrice(record)">去处理</el-button>
          </template>
        </template>
      </a-table>
    </div>
  </a-card>


  <a-modal
      v-model:visible="visibleInfo"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
      title="询价详情"
  >
    <SupplierInquiryProcess :comb-id="combId" :purchase-id="purchaseId" @click-close="visibleInfo=false" @refresh="search"/>
  </a-modal>
</template>

<script lang="ts">
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {useStore} from "vuex";
import {useRouter} from "vue-router";
import {TableColumnsType} from "ant-design-vue";
import {OrderBy, PageParam} from "@/type/public";
import {InquiryGetParam} from "@/type/purchase";
import {inquiryStateMap, purchaseWayStateMap} from "@/util/state";
import SupplierInquiryProcess from "@/views/panel/supplier/SupplierInquiryProcess.vue";

export default defineComponent({
  components:{
    SupplierInquiryProcess,
  },
  setup() {
    const {proxy}: any = getCurrentInstance();
    const store = useStore()
    const router = useRouter()
    const item = ref<TableColumnsType>([
      {title: "采购名", dataIndex: "combname", key: "combname", resizable: true},
      {title: "询价人", dataIndex: "operatorUsername", key: "operatorUsername", resizable: true},
      {title: "询价人电话", dataIndex: "tel", key: "tel", resizable: true},
      {title: "采购方式", dataIndex: "purchaseWay", key: "purchaseWay", resizable: true},
      {title: "状态", dataIndex: "state", key: "state", resizable: true},
      {title: "询价时间", dataIndex: "createTime", key: "createTime", resizable: true},
      {title: "操作", key: "operation", fixed: "right",},
    ]);
    const data = reactive({
      lines: [],
      total: 0,
    })
    const searchParam: InquiryGetParam = reactive({
      pageParam: new PageParam(1, 5),
      orderBys: new Array<OrderBy>(),
      state: null,
    });
    const pagination = computed(() => ({
      total: data.total,
      current: searchParam.pageParam.pageIndex,
      pageSize: searchParam.pageParam.pageSize,
      pageSizeOptions: ["5", "10", "20", "50"],
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: (total: any, range: any) => `当前 ${range[0]}-${range[1]} 共 ${total}`
    }));
    const onChange = (pagination: any, filters: any, sorter: any, extra: any) => {
      searchParam.pageParam.pageIndex = pagination.current
      searchParam.pageParam.pageSize = pagination.pageSize
      searchParam.orderBys.length = 0
      if (sorter.length) {
        for (let s of sorter) {
          searchParam.orderBys.push(new OrderBy(s.field, s.order == "ascend" ? 'asc' : 'desc'))
        }
      } else if (sorter.field) {
        searchParam.orderBys.push(new OrderBy(sorter.field, sorter.order == "ascend" ? 'asc' : 'desc'))
      }
      search()
    }

    function search(): void {
      proxy.$api.purchase.getInquiry(searchParam)
          .then((response: any) => {
            data.lines = response.data.data.inquiryVos
            data.total = response.data.data.total
          })
    }

    onMounted(() => {
      search()
    })

    function toPrice(record: any): void {
      //点击去给出价格,获取明细信息
      combId.value = record.combId
      purchaseId.value = record.purchaseId
      visibleInfo.value = true

    }

    let visibleInfo = ref(false)
    let combId = ref('')
    let purchaseId = ref('')
    return {
      item,
      proxy,
      router,
      search,
      onChange,
      pagination,
      searchParam,
      data,
      store,
      purchaseWayStateMap,
      visibleInfo,
      toPrice,
      inquiryStateMap,
      combId,
      purchaseId,
    }
  }
})
</script>
