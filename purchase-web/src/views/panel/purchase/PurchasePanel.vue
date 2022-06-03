<template>
  <div>
    <a-card style="width:100%;">
      <div class="flex align-items-center" style="margin-bottom:20px;">
        <SvgIcon :iconWidth="20" iconColor="#5c5c5c" iconName="search"/>
        <span style="color:#5c5c5c;">筛选搜索</span>
      </div>
      <div class="flex align-items-center flex-wrap">
        <a-input
            v-model:value="searchParam.serialNumber"
            :maxlength="30"
            addon-after
            addon-before="采购流水号"
            allowClear
            class="search-param-item"
            size="default"
            style="width:250px;"
        />
        <span class="search-param-item flex">
          <LeftLabel :width="80" label="采购方式"/>
          <a-select
              v-model:value="searchParam.purchaseWay"
              class="search-param-item"
              placeholder="选择状态"
              size="default"
              style="width: 100px"
          >
            <a-select-option :key="-1" :value="null">全部</a-select-option>
            <a-select-option v-for="(item) in purchaseWayStateMap" :key="item" :value="item[1].value">{{
                item[1].mess
              }}</a-select-option>
          </a-select>
        </span>
        <span class="search-param-item flex">
          <LeftLabel :width="80" label="状态"/>
          <a-select
              v-model:value="searchParam.state"
              class="search-param-item"
              placeholder="选择状态"
              size="default"
              style="width: 250px"
          >
            <a-select-option :key="-1" :value="null">全部</a-select-option>
            <a-select-option v-for="(item) in purchaseStateMap" :key="item" :value="item[1].value">{{
                item[1].mess
              }}</a-select-option>
          </a-select>
        </span>
        <span class="search-param-item flex" style="margin-top:11px;">
        <a-button
            class="flex align-items-center search-param-item"
            type="primary"
            @click="search"
        >
          <SvgIcon :iconWidth="20" iconColor="white" iconName="search"/>搜索
        </a-button>
        </span>
      </div>
    </a-card>
    <a-card style="width:100%;">
      <div
          class="flex align-items-center justify-content-between"
          style="margin-bottom:20px;"
      >
        <span class="flex align-items-center">
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
        </span>
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
            <template v-if="column.key === 'operation'">
              <div class="flex align-items-center">
                <el-button
                    size="small"
                    style="margin-left:10px;"
                    type="primary"
                    @click="toPurchaseProcess(record)"
                >
                  <SvgIcon :icon-width="15" icon-color="white" icon-name="处理"/>
                  继续采购
                </el-button>
                <el-button
                    size="small"
                    style="margin-left:10px;"
                    type="warning"
                    @click="showInquiry(record)"
                >
                  <SvgIcon :icon-width="15" icon-color="white" icon-name="eye"/>
                  查看询价单
                </el-button>
              </div>
            </template>
          </template>
          <template #expandedRowRender="{ record }">
            <el-descriptions :column="3" border size="large">
              <el-descriptions-item :span="1">
                <template #label>组合编号</template>
                {{ record.combVo.serialNumber }}
              </el-descriptions-item>
              <el-descriptions-item :span="1">
                <template #label>组合名</template>
                {{ record.combVo.combname }}
              </el-descriptions-item>
              <el-descriptions-item :span="1">
                <template #label>备注</template>
                <el-button plain size="small" type="success" @click="()=>{viewNote=record.combVo.note;visibleNote=true;}">
                  <SvgIcon :iconWidth="17" iconColor="#67c23a" iconName="eye"/>
                  备注
                </el-button>
              </el-descriptions-item>
            </el-descriptions>
            <CombDetailTable :comb-id="record.combVo.combId"/>
          </template>
        </a-table>
      </div>
    </a-card>
  </div>


  <a-modal
      v-model:visible="visibleNote"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
      title="备注"
  >
    <NoteView :view-note="viewNote"/>
  </a-modal>
  <a-modal
      v-model:visible="visibleInquiry"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
      title="询价单"
  >
    <InquiryTable :purchas-id="purchaseId"/>
  </a-modal>

</template>


<script lang="ts">
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {TableColumnsType} from "ant-design-vue";
import {OrderBy, PageParam} from "@/type/public";
import {combCanEditStateList, combStateMap, purchaseStateMap, purchaseWayStateMap} from '@/util/state'
import NoteView from '@/components/NoteView.vue'
import LeftLabel from '@/components/LeftLabel.vue'
import CombDetailTable from "@/views/panel/comb/CombDetailTable.vue";
import SvgIcon from "@/components/SvgIcon.vue";
import {useStore} from "vuex";
import {useRouter} from "vue-router";
import {PurchaseGetParam} from "@/type/purchase";
import InquiryTable from "@/views/panel/purchase/InquiryTable.vue";
import purchase from "@/api/module/purchase";

export default defineComponent({
  components: {
    SvgIcon,
    NoteView,
    LeftLabel,
    CombDetailTable,
    InquiryTable,
  },
  setup() {
    const {proxy}: any = getCurrentInstance();
    const store = useStore()
    const router = useRouter()
    const item = ref<TableColumnsType>([
      {title: "采购流水号", dataIndex: "serialNumber", key: "serialNumber", resizable: true},
      {title: "当前状态", dataIndex: "state", key: "state", resizable: true},
      {title: "采购方式", dataIndex: "purchaseWay", key: "purchaseWay", resizable: true},
      {title: "成交供应商", dataIndex: "suppliername", key: "suppliername", resizable: true},
      {title: "操作员", dataIndex: "operatorUsername", key: "operatorUsername", resizable: true},
      {title: "合同签署人", dataIndex: "signUsername", key: "signUsername", resizable: true},
      {title: "成交总价", dataIndex: "dealTotalPrice", key: "dealTotalPrice", resizable: true},
      {title: "创建时间", dataIndex: "createTime", key: "createTime", resizable: true, sorter: {multiple: 1}},
      {title: "操作", key: "operation", fixed: "right",},
    ]);
    const searchParam: PurchaseGetParam = reactive({
      supplierId: null,
      state: null,
      serialNumber: '',
      purchaseWay: null,
      pageParam: new PageParam(1, 5),
      orderBys: new Array<OrderBy>(),
    });
    const data = reactive({
      lines: [],
      total: 0,
    })
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
      proxy.$api.purchase.getPurchase(searchParam)
          .then((response: any) => {
            console.log(response.data)
            data.lines = response.data.data.purchaseVos
            data.total = response.data.data.total
          })
    }

    onMounted(() => {
      search()
    })
    let visibleNote = ref(false)
    let viewNote = ref('')
    function toPurchaseProcess(record: any): void {
      //点击继续采购
      const tab = {
        title: '采购',
        name: 'PurchaseProcess',
        content: 'PurchaseProcess',
      }
      store.commit('addTab', tab)
      router.push({
        name: 'PurchaseProcess',
        query: {
          purchaseId: record.purchaseId,
          combId: record.combVo.combId,
          state: record.state,
        }
      })
    }
    let visibleInquiry = ref(false)
    function showInquiry(record: any): void{
      //点击查看询价单
      visibleInquiry.value = true;
      purchaseId.value = record.purchaseId;
    }
    let purchaseId = ref('')
    return {
      pagination,
      item,
      onChange,
      data,
      search,
      combStateMap,
      visibleNote,
      viewNote,
      searchParam,
      store,
      router,
      combCanEditStateList,
      purchaseWayStateMap,
      purchaseStateMap,
      toPurchaseProcess,
      showInquiry,
      visibleInquiry,
      purchaseId,
    }
  }
})
</script>
