<template>
  <div>
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
                     :key="record.state"
                     :color="purchaseWayStateMap.get(record.purchaseWay)?.tagColor"
                 >{{ purchaseWayStateMap.get(record.purchaseWay)?.mess }}</a-tag>
               </span>
            </template>
            <template v-if="column.key === 'operation'">
              <el-button plain size="small" type="success" @click="()=>{visibleNote=true;viewNote=record.note;}">
                <SvgIcon :iconWidth="17" iconColor="#67c23a" iconName="eye"/>
                备注
              </el-button>
              <a-button  size="small" type="primary" style="margin-left:10px;"
                         @click="()=>{visibleReview=true;reviewParam.combId=record.combId}">
                去审核
              </a-button>
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
      v-model:visible="visibleReview"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"

  >
    <el-card>
      <div style="font-size:120%;font-weight: bold;margin:20px;">我的审核</div>
      <el-form>
        <el-form-item label="审核意见">
          <MavonEditorCuVue ref="editor"/>
        </el-form-item>
        <el-form-item label="审核结果">
          <el-radio v-model="reviewParam.result" :label="1" border size="large">通过</el-radio>
          <el-radio v-model="reviewParam.result" :label="0" border size="large">不通过</el-radio>
        </el-form-item>
        <el-form-item label="指定操作员" v-if="reviewParam.result==1">
          <a-select
              v-model:value="reviewParam.confirmUserId"
              class="search-param-item"
              placeholder="选择操作员"
              size="default"
              style="width: 200px"
          >
            <a-select-option v-for="(item) in assignUsers" :key="item" :value="item.userId">{{
                item.realname
              }}
            </a-select-option>
          </a-select>
        </el-form-item>
        <div class="flex justify-content-center">
          <el-button size="large" type="primary" @click="toReview">确认审核</el-button>
        </div>
      </el-form>

    </el-card>
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
import {CombReviewParam, ReviewParam} from "@/type/review";
import MavonEditorCuVue from "@/components/MavonEditorCu.vue";
import {ElMessage} from "element-plus";

export default defineComponent({
  components: {
    SvgIcon,
    NoteView,
    // LeftLabel,
    CombDetailTable,
    MavonEditorCuVue,
  },
  setup() {
    const {proxy}: any = getCurrentInstance();
    const store = useStore()
    const router = useRouter()

    const item = ref<TableColumnsType>([
      {title: "组合编号", dataIndex: "serialNumber", key: "serialNumber", resizable: true},
      {title: "组合名", dataIndex: "combname", key: "combname", resizable: true},
      {title: "当前状态", dataIndex: "state", key: "state", resizable: true},
      {title: "创建人", dataIndex: "creatUsername", key: "creatUsername", resizable: true},
      {title: "创建时间", dataIndex: "createTime", key: "createTime", resizable: true, sorter: {multiple: 1}},
      {title: "采购方式", dataIndex: "purchaseWay", key: "purchaseWay"},
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
      proxy.$api.comb.getUnreviewCombs(searchParam)
          .then((response: any) => {
            data.lines = response.data.data.combVos
            data.total = response.data.data.total
          })
    }

    const createTimeLimit = ref<any>();
    onMounted(() => {
      search()
      getAssignUsers()
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
    let visibleReview = ref(false)
    const reviewParam: CombReviewParam = reactive({
      combId: '',
      result: 1,
      opinion: '',
      confirmUserId: '',
    })
    let assignUsers = ref<Array<any>>([])
    function getAssignUsers(): void {
      //获取可以审核的人列表
      proxy.$api.user.getAssignUsers(5)//5表示操作员了
          .then((response: any) => {
            assignUsers.value = response.data.data
          })
    }
    function toReview(): void {
      //点击确认审核按钮
      if(reviewParam.confirmUserId == '' && reviewParam.result==1){
        ElMessage({
          type: 'warning',
          message: '请选择一个操作员'
        })
        return
      }

      reviewParam.opinion = editor.value.getMd()
      proxy.$api.review.reviewAdd4(reviewParam)
          .then((response: any) => {
            //添加审核成功，关闭此详情,刷新待审核申请列表
            console.log(response.data)
            if (response.data.state == proxy.$state.SUCCESS) {
              search()
              setTimeout(() => {
                visibleReview.value = false
              }, 1000)
            }
          })
    }
    const editor = ref()
    return {
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
      visibleReview,
      reviewParam,
      getAssignUsers,
      assignUsers,
      toReview,
      editor,
      purchaseWayStateMap,
    }
  }
})
</script>
