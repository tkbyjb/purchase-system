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
            addon-before="组合编号"
            allowClear
            class="search-param-item"
            size="default"
            style="width:250px;"
        />
        <a-input
            v-model:value="searchParam.combname"
            :maxlength="30"
            addon-after
            addon-before="组合名"
            allowClear
            class="search-param-item"
            size="default"
            style="width:200px;"
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
<!--        <span class="search-param-item flex">-->
<!--          <LeftLabel :width="50" label="状态"/>-->
<!--          <a-select-->
<!--              v-model:value="searchParam.state"-->
<!--              class="search-param-item"-->
<!--              placeholder="选择状态"-->
<!--              size="default"-->
<!--              style="width: 130px"-->
<!--          >-->
<!--            <a-select-option :key="-1" :value="null">全部</a-select-option>-->
<!--            <a-select-option v-for="(item) in combStateMap" :key="item" :value="item[1].value">-->
<!--              {{ item[1].mess }}</a-select-option>-->
<!--          </a-select>-->
<!--        </span>-->
        <!--        <span class="search-param-item flex">-->
        <!--                    <LeftLabel :width="80" label="审核时间"/>-->
        <!--                    <a-range-picker v-model:value="createTimeLimit" format="YYYY-MM-DD"/>-->
        <!--                </span>-->
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
            rowKey="combId"
            @change="onChange"
            @resizeColumn="(w: any, col: any) => { col.width = w; }"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'state'">
               <span>
                 <a-tag
                     :key="record.state"
                     :color="combStateMap.get(record.state)?.tagColor"
                 >{{ combStateMap.get(record.state)?.mess }}</a-tag>
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
                <el-button plain size="small" type="success" @click="()=>{visibleNote=true;viewNote=record.note;}">
                  <SvgIcon :iconWidth="17" iconColor="#67c23a" iconName="eye"/>
                  备注
                </el-button>
                <el-button
                    size="small"
                    style="margin-left:10px;"
                    type="primary"
                    @click="confirmComb(record)"
                >
                  <SvgIcon :icon-width="15" icon-color="white" icon-name="处理"/>
                  申领
                </el-button>
<!--                <a-popconfirm-->
<!--                    cancel-text="取消"-->
<!--                    ok-text="确定"-->
<!--                    title="确定取消采购?"-->
<!--                    @cancel="()=>{}"-->
<!--                    @confirm="cancelComb(record)"-->
<!--                >-->
<!--                  <el-button-->
<!--                      size="small"-->
<!--                      style="margin-left:10px;"-->
<!--                      type="danger"-->
<!--                  >-->
<!--                    <SvgIcon :icon-width="15" icon-color="white" icon-name="取消"/>-->
<!--                    取消处理-->
<!--                  </el-button>-->
<!--                </a-popconfirm>-->
              </div>
            </template>
          </template>
          <template #expandedRowRender="{ record }">
            <CombDetailTable :comb-id="record.combId"/>
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
      title="申请备注"
  >
    <NoteView :view-note="viewNote"/>
  </a-modal>


  <a-modal
      v-model:visible="visibleUser"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:600px;"
      title="指定审核人"
  >
     <span class="search-param-item flex">
                    <LeftLabel :width="160" label="选择部门管理审核人"/>
                    <a-select
                        v-model:value="reviewParam.assignUserId"
                        class="search-param-item"
                        placeholder="选择审核人"
                        size="default"
                        style="width: 200px"
                    >
                      <a-select-option v-for="(item) in assignUsers" :key="item" :value="item.userId">{{
                          item.realname
                        }}</a-select-option>
                    </a-select>
                </span>
    <div>
      <a-button size="small" type="primary" @click="toReview">确认提交审核</a-button>
    </div>
  </a-modal>
</template>


<script lang="ts">
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {TableColumnsType, TableProps} from "ant-design-vue";
import {OrderBy, PageParam} from "@/type/public";
import {CombGetParam} from "@/type/comb";
import moment from "moment";
import {combCanEditStateList, combStateMap, purchaseWayStateMap} from '@/util/state'
import NoteView from '@/components/NoteView.vue'
import LeftLabel from '@/components/LeftLabel.vue'
import CombDetailTable from "@/views/panel/comb/CombDetailTable.vue";
import SvgIcon from "@/components/SvgIcon.vue";
import {useStore} from "vuex";
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";

export default defineComponent({
  components: {
    SvgIcon,
    NoteView,
    LeftLabel,
    CombDetailTable,
  },
  setup() {
    const {proxy}: any = getCurrentInstance();
    const store = useStore()
    const router = useRouter()

    const item = ref<TableColumnsType>([
      {title: "组合编号", dataIndex: "serialNumber", key: "serialNumber", resizable: true},
      {title: "组合名", dataIndex: "combname", key: "combname", resizable: true},
      {title: "当前状态", dataIndex: "state", key: "state", resizable: true},
      {title: "采购方式", dataIndex: "purchaseWay", key: "purchaseWay"},
      {title: "创建人", dataIndex: "creatUsername", key: "creatUsername", resizable: true},
      {title: "创建时间", dataIndex: "createTime", key: "createTime", resizable: true, sorter: {multiple: 1}},
      {title: "操作", key: "operation", fixed: "right",},
    ]);
    const searchParam: CombGetParam = reactive({
      startCreateTime: '',
      endCreateTime: '',
      state: null,
      combname: '',
      serialNumber: '',
      purchaseWay: null,
      pageParam: new PageParam(1, 5),
      orderBys: new Array<OrderBy>(),
    });
    const selectRowKeys = ref<Array<string>>([])//选中的数据
    const rowSelection: TableProps['rowSelection'] = {
      fixed: true,
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        selectRowKeys.value = selectedRowKeys
        console.log(selectRowKeys.value)
      },
      getCheckboxProps: (record: any) => ({//不可选择的筛选
        disabled: combCanEditStateList.indexOf(record.state) == -1,
        name: record.name,
      }),
    };
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
      if (createTimeLimit.value && createTimeLimit.value.length > 1) {
        searchParam.startCreateTime = moment(createTimeLimit.value[0].$d).format('yyyy-MM-DD HH:mm:ss')
        searchParam.endCreateTime = moment(createTimeLimit.value[1].$d).format('yyyy-MM-DD HH:mm:ss')
      } else {
        searchParam.startCreateTime = ''
        searchParam.endCreateTime = ''
      }
      proxy.$api.comb.getUnconfirmCombs(searchParam)
          .then((response: any) => {
            data.lines = response.data.data.combVos
            data.total = response.data.data.total
          })
    }

    const createTimeLimit = ref<any>();
    onMounted(() => {
      search()
    })
    let visibleNote = ref(false)
    let viewNote = ref('')

    function updateComb(comb: any): void {
      //点击编辑组合,跳转到组合添加标签页
      const tab = {
        title: '编辑采购组合',
        name: 'CombUpdate',
        content: 'CombUpdate',
      }
      store.commit('addTab', tab)
      router.push({
        name: 'CombUpdate',
        query: {
          way: comb.purchaseWay,
          combId: comb.combId
        }
      })
    }

    let visibleUser = ref(false)
    let assignUsers = ref([])

    function getAssignUsers(): void {
      //获取可以审核的人列表
      proxy.$api.user.getAssignUsers(4)
          .then((response: any) => {
            assignUsers.value = response.data.data
          })
    }

    const reviewParam = reactive({
      combId: '',
      assignUserId: '',
    })

    function toReview(): void {
      //点击提交审核
      if (reviewParam.assignUserId == '') {
        ElMessage({
          type: 'warning',
          message: '请选择一个审核人'
        })
        return
      }
      proxy.$api.comb.combToReview(reviewParam.combId, reviewParam.assignUserId)
          .then((response: any) => {
            if (response.data.state == proxy.$state.SUCCESS) {
              search();
            }
          })
    }


    // function cancelComb(record: any): void{
    //   //点击取消处理
    //
    // }
    function confirmComb(record: any): void{
      //点击申领,添加一条采购计划
      proxy.$api.purchase.addPurchase(record.combId)
      .then((response: any)=>{
        if(response.data.state == proxy.$state.SUCCESS) {
          search();
        }
      })
    }
    return {
      selectRowKeys,
      rowSelection,
      pagination,
      item,
      onChange,
      data,
      search,
      createTimeLimit,
      combStateMap,
      visibleNote,
      viewNote,
      searchParam,
      updateComb,
      store,
      router,
      combCanEditStateList,
      purchaseWayStateMap,
      visibleUser,
      assignUsers,
      getAssignUsers,
      reviewParam,
      toReview,
      // cancelComb,
      confirmComb,
    }
  }
})
</script>
