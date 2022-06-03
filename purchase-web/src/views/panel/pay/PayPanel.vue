<template>
  <a-card style="width:100%;">
    <div class="flex align-items-center" style="margin-bottom:20px;">
      <spna class="flex align-items-center">
        <SvgIcon :iconWidth="22" iconColor="#5c5c5c" iconName="清单"/>
        <span style="color:#5c5c5c;">数据</span>
        <a-button class="flex align-items-center" size="small" style="margin-left:15px;font-size:70%;"
                  type="primary" @click="search">
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
          <template v-if="column.key === 'state'">
               <span>
                 <a-tag
                     :key="record.state"
                     :color="purchaseStateMap.get(record.state)?.tagColor"
                 >{{ purchaseStateMap.get(record.state)?.mess }}</a-tag>
               </span>
          </template>
          <template v-if="column.key === 'purchaseWay'">
               <span>
                 <a-tag
                     :key="record"
                     :color="purchaseWayStateMap.get(record.purchaseWay)?.tagColor"
                 >{{ purchaseWayStateMap.get(record.purchaseWay)?.mess }}</a-tag>
               </span>
          </template>
          <teplate v-if="column.key === 'combname'">
            <span>{{ record.combVo.combname }}</span>
          </teplate>
          <template v-if="column.key === 'operation'">
            <div class="flex align-items-center">
              <el-button
                  size="small"
                  style="margin-left:10px;"
                  type="primary"
                  @click="toProcess(record)"
              >
                <SvgIcon :icon-width="15" icon-color="white" icon-name="处理"/>
                去处理
              </el-button>
            </div>
          </template>
        </template>
      </a-table>
    </div>
  </a-card>

  <a-modal
      v-model:visible="visiblePay"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
      title="详情"
  >
    <PayInfo :comb-id="combId" :purchase-id="processPurchaseId" @refresh="search" @click-close="visiblePay=false"/>
  </a-modal>
</template>

<script lang="ts">
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {TableColumnsType} from "ant-design-vue";
import {OrderBy, PageParam} from "@/type/public";
import {useStore} from "vuex";
import {useRouter} from "vue-router";
import {purchaseStateMap, purchaseWayStateMap} from '@/util/state'
import PayInfo from "@/views/panel/pay/PayInfo.vue";

export default defineComponent({
  components: {
    PayInfo,
  },
  setup() {
    const {proxy}: any = getCurrentInstance();
    const store = useStore()
    const router = useRouter()
    const item = ref<TableColumnsType>([
      {title: "采购流水号", dataIndex: "serialNumber", key: "serialNumber", resizable: true},
      {title: "采购名", dataIndex: "combname", key: "combname", resizable: true},
      {title: "当前状态", dataIndex: "state", key: "state", resizable: true},
      {title: "负责人", dataIndex: "operatorUsername", key: "operatorUsername", resizable: true},
      {title: "采购方式", dataIndex: "purchaseWay", key: "purchaseWay", resizable: true},
      {title: "供应商", dataIndex: "suppliername", key: "suppliername", resizable: true},
      {title: "创建时间", dataIndex: "createTime", key: "createTime", resizable: true, sorter: {multiple: 1}},
      {title: "操作", key: "operation", fixed: "right",},
    ]);
    const data = reactive({
      lines: [],
      total: 0,
    })
    const searchParam = reactive({
      pageParam: new PageParam(1, 5),
      orderBys: new Array<OrderBy>(),
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
      proxy.$api.purchase.getUnpayPurchase(searchParam)
          .then((response: any) => {
            data.lines = response.data.data.purchaseVos
            data.total = response.data.data.total
          })
    }

    onMounted(() => {
      search()
    })

    let visiblePay = ref(false)
    let processPurchaseId = ref('')
    let combId = ref('')

    function toProcess(record: any): void {
      //点击去处理
      visiblePay.value = true;
      processPurchaseId.value = record.purchaseId;
      combId.value = record.combVo.combId;
    }

    return {
      proxy,
      item,
      data,
      searchParam,
      search,
      pagination,
      onChange,
      store,
      router,
      purchaseWayStateMap,
      purchaseStateMap,
      toProcess,
      visiblePay,
      processPurchaseId,
      combId,
    }
  }
})
</script>
