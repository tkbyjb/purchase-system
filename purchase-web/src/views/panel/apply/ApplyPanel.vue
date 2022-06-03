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
            addon-before="流水号"
            allowClear
            class="search-param-item"
            size="default"
            style="width:250px;"
        />
        <a-input
            v-model:value="searchParam.applyname"
            :maxlength="30"
            addon-after
            addon-before="申请名"
            allowClear
            class="search-param-item"
            size="default"
            style="width:250px;"
        />
        <a-input
            v-model:value="searchParam.note"
            :maxlength="60"
            addon-after
            addon-before="备注"
            allowClear
            class="search-param-item"
            size="default"
            style="width:250px;"
        />
        <span class="search-param-item flex">
                    <LeftLabel :width="80" label="创建时间"/>
                    <a-range-picker v-model:value="createTimeLimit" format="YYYY-MM-DD"/>
                </span>
        <span class="search-param-item flex">
                    <LeftLabel :width="120" label="是否下一年计划"/>
                    <a-select
                        v-model:value="searchParam.putoff"
                        class="search-param-item"
                        placeholder="选择状态"
                        size="default"
                        style="width: 100px"
                    >
                        <a-select-option :key="-1" :value="null">全部</a-select-option>
                        <a-select-option :key="1" :value="1">是</a-select-option>
                        <a-select-option :key="0" :value="0">否</a-select-option>
                    </a-select>
                </span>
        <span class="search-param-item flex">
                    <LeftLabel :width="80" label="当前状态"/>
                    <a-select
                        v-model:value="searchParam.state"
                        class="search-param-item"
                        placeholder="选择状态"
                        size="default"
                        style="width: 200px"
                    >
                        <a-select-option :key="-1" :value="null">全部</a-select-option>
                        <a-select-option
                            v-for="(item) in applyStateMap"
                            :key="item[0]"
                            :value="item[1].value"
                        >{{ item[1].mess }}</a-select-option>
                    </a-select>

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
        <el-popconfirm
            cancel-button-text="取消"
            confirm-button-text="删除"
            icon-color="#626AEF"
            title="确定删除选中数据?"
            @cancel="() => { }"
            @confirm="deleteApplys"
        >
          <template #reference>
            <a-button
                class="flex align-items-center search-param-item"
                size="small"
                style="font-size:80%;"
                type="primary"
            >
              <SvgIcon
                  :iconWidth="15"
                  iconColor="white"
                  iconName="回收站"
                  @click="search"
              />
              删除选中
            </a-button>
          </template>
        </el-popconfirm>
      </div>
      <div>
        <a-table
            :columns="item"
            :data-source="data.lines"
            :pagination="pagination"
            :row-selection="rowSelection"
            :scroll="{ x: 'max-content' }"
            rowKey="applyId"
            @change="onChange"
            @resizeColumn="(w: any, col: any) => { col.width = w; }"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'operation'">
              <div class="flex align-items-center">
                <a-dropdown-button
                    danger
                    size="small"
                    style="margin-left:10px;"
                    @click="clickViewNote(record)"
                >
                  <!-- <SvgIcon iconName="eye" :iconWidth="17" iconColor="white" /> -->
                  查看备注
                  <template #overlay>
                    <a-menu>
                      <a-menu-item key="1" v-if="record.attachment != null && record.attachment != ''">
                        <a :href="record.attachment" target="_blank">
                          <el-button
                              size="small"
                              type="warning"
                              @click="getAttachment(record.applyId)"
                          >

                            <SvgIcon
                                :iconWidth="20"
                                iconColor="white"
                                iconName="download"
                            />
                            下载附件
                          </el-button>
                        </a>
                      </a-menu-item>
                      <a-menu-item key="2">
                        <el-button
                            size="small"
                            type="primary"
                            @click="getTimeLine(record.applyId)"
                        >
                          <SvgIcon
                              :iconWidth="20"
                              iconColor="white"
                              iconName="download"
                          />
                          查看时间线
                        </el-button>
                      </a-menu-item>
                    </a-menu>
                  </template>
                </a-dropdown-button>
                <a-dropdown-button
                    v-if="applyCanEditStateList.indexOf(record.state) != -1"
                    size="small"
                    style="margin-left:10px;"
                    type="primary"
                    @click="editApply(record)"
                >
                  编辑
                  <template #overlay>
                    <a-menu>
                      <a-menu-item key="1">
                        <el-button
                            size="small"
                            type="primary"
                            @click="clickDetailAdd"
                        >
                          <SvgIcon
                              :iconWidth="15"
                              iconColor="white"
                              iconName="添加"
                          />
                          添加明细
                        </el-button>
                      </a-menu-item>
                      <a-menu-item key="2">
                        <!--                                                <a-popconfirm-->
                        <!--                                                    title="确定提交?提交后未打回时不能编辑"-->
                        <!--                                                    ok-text="确定"-->
                        <!--                                                    cancel-text="取消"-->
                        <!--                                                    @confirm="toReview(record.applyId)"-->
                        <!--                                                    @cancel="() => { }"-->
                        <!--                                                >-->
                        <el-button size="small" type="danger" @click="()=>{
                          visibleUser=true;
                          applyId=record.applyId;
                          getAssignUsers();}">
                          <SvgIcon
                              :iconWidth="15"
                              iconColor="white"
                              iconName="提交"
                          />
                          提交审核
                        </el-button>
                        <!--                                                </a-popconfirm>-->
                      </a-menu-item>
                      <a-menu-item v-if="record.state == 20" key="1">
                        <el-button
                            size="small"
                            type="warning"
                            @click="clickReturnInfo(record.applyId)"
                        >
                          <SvgIcon
                              :iconWidth="15"
                              iconColor="white"
                              iconName="eye"
                          />
                          打回信息
                        </el-button>
                      </a-menu-item>
                    </a-menu>
                  </template>
                </a-dropdown-button>
              </div>
            </template>
            <template v-else-if="column.key === 'state'">
                            <span>
                                <a-tag
                                    :key="record.state"
                                    :color="applyStateMap.get(record.state)?.tagColor"
                                >{{ applyStateMap.get(record.state)?.mess }}</a-tag>
                            </span>
            </template>
            <template v-if="column.key === 'putoff'">
                            <span>
                                <a-tag
                                    :key="record.putoff"
                                    :color="record.putoff === 0 ? 'blue' : 'red'"
                                >{{ record.putoff == 0 ? '否' : '是' }}</a-tag>
                            </span>
            </template>
          </template>
          <template #expandedRowRender="{ record }">
            <DetailTable
                ref="detailTableRef"
                :apply-id="record.applyId"
                :apply-state="record.state"
            />
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
      v-model:visible="visibleReturn"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
      title="审核不通过意见"
  >
    <NoteView :view-note="viewReturn"/>
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
                        v-model:value="assignUserId"
                        class="search-param-item"
                        placeholder="选择管理部门审核人"
                        size="default"
                        style="width: 200px"
                    >
                      <a-select-option v-for="(item) in assignUsers" :key="item" :value="item.userId">{{
                          item.realname
                        }}</a-select-option>
                    </a-select>
                </span>
    <div>
      <a-button size="small" type="primary" @click="toReview(applyId, assignUserId)">确认提交审核</a-button>
    </div>
  </a-modal>
</template>

<script lang="ts">
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";
import type {Dayjs} from 'dayjs';
import {OrderBy, PageParam} from '@/type/public'
import {Apply, ApplyGetParam} from '@/type/apply'
import LeftLabel from "@/components/LeftLabel.vue";
import type {TableColumnsType, TableProps} from 'ant-design-vue';
import DetailTable from '@/views/panel/detail/DetailTable.vue'
import {applyCanEditStateList, applyStateMap} from '@/util/state'
import moment from 'moment'
import NoteView from '@/components/NoteView.vue'
import {ElMessage} from "element-plus";
import {useStore} from 'vuex'
import {useRouter} from 'vue-router'

type RangeValue = [Dayjs, Dayjs];
// import DetailAdd from '@/views/panel/detail/DetailAdd.vue'

export default defineComponent({
  components: {
    DetailTable,
    NoteView,
    LeftLabel,
    // DetailAdd,
  },
  setup() {
    const {proxy}: any = getCurrentInstance();
    const store = useStore()
    const router = useRouter()

    const item = ref<TableColumnsType>([
      // title是字段显示名,dataIndex是匹配数据字段名,key就是个key,唯一标识dataIndex
      {title: "申请编号", dataIndex: "serialNumber", key: "serialNumber", resizable: true},
      {title: "申请名", dataIndex: "applyname", key: "applyname", resizable: true},
      {title: "当前状态", dataIndex: "state", key: "state", resizable: true},
      {title: "是否下一年计划", dataIndex: "putoff", key: "putoff", resizable: true},
      {title: "申请人", dataIndex: "applyUsername", key: "applyUsername", resizable: true},
      {title: "申请部门", dataIndex: "applyDepartmentname", key: "applyDepartmentname", resizable: true},
      {title: "创建时间", dataIndex: "createTime", key: "createTime", resizable: true, sorter: {multiple: 1}},
      {title: "提交审核时间", dataIndex: "applyTime", key: "applyTime", resizable: true, sorter: {multiple: 2}},
      {title: "上次修改时间", dataIndex: "lastUpdateTime", key: "lastUpdateTime", resizable: true, sorter: {multiple: 3}},
      {title: "操作", key: "operation", fixed: "right",},
    ]);
    const data = reactive({
      lines: [],
      total: 0,
    })
    const searchParam: ApplyGetParam = reactive({
      serialNumber: "",
      applyname: "",
      startCreateTime: "",
      endCreateTime: "",
      note: "",
      state: null,
      pageParam: new PageParam(1, 5),
      orderBys: new Array<OrderBy>(),
      putoff: null,
    });
    // const createTimeLimit = ref<RangeValue>();
    const createTimeLimit = ref<any>();//类型暂时改成ang,不然RangeValue会报找不到$d属性
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
      // console.log("params", pagination, filters, sorter, extra);
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
      proxy.$api.apply.get(searchParam)
          .then((response: any) => {
            console.log(response.data)
            data.lines = response.data.data.applyVos
            data.total = response.data.data.total
          })
    }

    onMounted(() => {
      search()

    })
    // 下边是功能操作区域

    const detailTableRef = ref()

    function clickDetailAdd(): void {
      //点击添加明细按钮,

      console.log(detailTableRef.value)
      detailTableRef.value.showDetailAdd = true
    }

    // 查看备注
    let visibleNote = ref(false)
    let viewNote = ref('')

    function clickViewNote(record: any): void {
      //点击查看备注
      visibleNote.value = true;
      viewNote.value = record.note;
    }

    //删除申请相关的
    function deleteApplys(): void {
      //删除选中的申请
      if (selectRowKeys.value.length < 1) {//没有选中数据时
        ElMessage({
          message: '请先选择数据',
          type: 'warning',
        })
        return
      }
      proxy.$api.apply.deletes(selectRowKeys.value)
          .then((response: any) => {
            if (response.data.state === proxy.$state.SUCCESS) {
              //删除成功,刷新申请表格数据
              search()
            }
          })
    }

    const selectRowKeys = ref<Array<string>>([])//选中的数据
    const rowSelection: TableProps['rowSelection'] = {
      fixed: true,
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        // console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
        selectRowKeys.value = selectedRowKeys
        console.log(selectRowKeys.value)
      },
      getCheckboxProps: (record: any) => ({//不可选择的筛选
        disabled: [1, 20, 21].indexOf(record.state) == -1,
        name: record.name,
      }),
    };

    //      编辑申请
    function editApply(record: Apply): void {
      //点击编辑申请按钮回调
      addTab(record)
    }

    function getAttachment(applyId: string): void {
      //点击下载附件
    }

    function getTimeLine(applyId: string): void {
      //点击查看申请时间线

    }

    function addTab(apply: Apply): void {
      //添加修改申请tab到store
      const tab = {
        title: "修改申请",
        name: 'ApplyUpdate',
        content: 'ApplyUpdate',
      }
      store.commit('addTab', tab)
      router.push({
        name: 'ApplyUpdate',
        query: {
          applyId: apply.applyId,
          applyname: apply.applyname,
          note: apply.note,
          attachment: apply.attachment,
        }
      })
    }

    function toReview(applyId: string, assignUserId: string): void {
      //点击提交审核
      if (assignUserId == '') {
        ElMessage({
          type: 'warning',
          message: '请选择一个审核人'
        })
        return
      }
      proxy.$api.apply.toReview(applyId, assignUserId)
          .then((response: any) => {
            if (response.data.state === proxy.$state.SUCCESS) {
              //提交成功,刷新一下当页面
              search()
              visibleUser.value = false
            }
          })
    }

    let visibleReturn = ref(false)
    let viewReturn = ref('')

    async function clickReturnInfo(applyId: string): Promise<any> {
      //点击查看打回的审核信息
      await proxy.$api.review.getReturnOpinion(applyId)
          .then((response: any) => {
            viewReturn.value = response.data.data
          })
      visibleReturn.value = true
    }

    let visibleUser = ref(false)

    let assignUsers = ref<Array<any>>([])
    let applyId = ref('')
    let assignUserId = ref('')

    function getAssignUsers(): void {
      //获取可以审核的人列表
      proxy.$api.user.getAssignUsers(1)
          .then((response: any) => {
            console.log("**********", response.data)
            assignUsers.value = response.data.data
          })
    }

    return {
      proxy,
      searchParam,
      item,
      data,
      pagination,
      onChange,
      createTimeLimit,
      search,
      selectRowKeys,
      rowSelection,
      applyStateMap,
      applyCanEditStateList,
      detailTableRef,
      clickDetailAdd,
      visibleNote,
      viewNote,
      clickViewNote,
      deleteApplys,
      editApply,
      getAttachment,
      getTimeLine,
      store,
      addTab,
      router,
      toReview,
      clickReturnInfo,
      visibleReturn,
      viewReturn,
      visibleUser,
      assignUsers,
      getAssignUsers,
      applyId,
      assignUserId,
    };
  },
})
</script>

<style lang="scss" scoped>
/deep/ .ant-card-body {
  padding: 10px;
}

.search-param-item {
  margin-right: 10px;
  margin-bottom: 10px;
}
</style>
