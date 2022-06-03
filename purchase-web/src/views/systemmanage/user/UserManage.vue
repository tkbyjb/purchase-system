<template>
  <a-card style="width:100%;">
    <div class="flex align-items-center" style="margin-bottom:20px;">
      <SvgIcon :iconWidth="20" iconColor="#5c5c5c" iconName="search"/>
      <span style="color:#5c5c5c;">筛选搜索</span>
    </div>
    <div class="flex align-items-center flex-wrap">
      <a-input
          v-model:value="searchParam.username"
          :maxlength="30"
          addon-after
          addon-before="用户名"
          allowClear
          class="search-param-item"
          size="default"
          style="width:150px;"
      />
      <a-input
          v-model:value="searchParam.realname"
          :maxlength="30"
          addon-after
          addon-before="真名"
          allowClear
          class="search-param-item"
          size="default"
          style="width:150px;"
      />
      <a-input
          v-model:value="searchParam.tel"
          :maxlength="30"
          addon-after
          addon-before="手机号"
          allowClear
          class="search-param-item"
          size="default"
          style="width:250px;"
      />
      <a-input
          v-model:value="searchParam.idNumber"
          :maxlength="30"
          addon-after
          addon-before="身份证号"
          allowClear
          class="search-param-item"
          size="default"
          style="width:250px;"
      />
      <span class="search-param-item flex">
                <LeftLabel :width="80" label="账号状态"/>
                <a-select
                    v-model:value="searchParam.state"
                    class="search-param-item"
                    placeholder="选择性别"
                    size="default"
                    style="width: 100px"
                >
                    <a-select-option :key="-1" :value="null">全部</a-select-option>
                    <a-select-option
                        v-for="(item) in userStateMap"
                        :key="item[0]"
                        :value="item[1].value"
                    >{{ item[1].mess }}</a-select-option>
                </a-select>
            </span>
      <span class="search-param-item flex">
                <LeftLabel :width="50" label="性别"/>
                <a-select
                    v-model:value="searchParam.sex"
                    class="search-param-item"
                    placeholder="选择性别"
                    size="default"
                    style="width: 70px"
                >
                    <a-select-option :key="-1" :value="null">全部</a-select-option>
                    <a-select-option :key="0" :value="0">女</a-select-option>
                    <a-select-option :key="1" :value="1">男</a-select-option>
                </a-select>
            </span>
      <span class="search-param-item flex">
                <LeftLabel :width="80" label="创建时间"/>
                <a-range-picker v-model:value="createTimeLimit" format="YYYY-MM-DD"/>
            </span>
      <span class="search-param-item flex">
                <LeftLabel :width="80" label="所属角色"/>
                <a-select
                    v-model:value="searchParam.roleId"
                    class="search-param-item"
                    placeholder="选择状态"
                    size="default"
                    style="width: 200px"
                >
                    <a-select-option :key="-1" :value="null">全部</a-select-option>
                    <a-select-option
                        v-for="(item) in roles"
                        :key="item"
                        :value="item.roleId"
                    >{{ item.rolename }}</a-select-option>
                </a-select>
            </span>
      <span class="search-param-item flex">
                <LeftLabel :width="80" label="所属部门"/>
                <a-select
                    v-model:value="searchParam.departmentId"
                    class="search-param-item"
                    placeholder="选择状态"
                    size="default"
                    style="width: 200px"
                >
                    <a-select-option :key="-1" :value="null">全部</a-select-option>
                    <a-select-option
                        v-for="(item) in departments"
                        :key="item"
                        :value="item.departmentId"
                    >{{ item.departmentname }}</a-select-option>
                </a-select>
            </span>
      <span class="search-param-item flex" style="margin-top:12px;">
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
  <a-card>
    <div class="flex align-items-center justify-content-between" style="margin-bottom:20px;">
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
      <span class="flex">
                <el-button
                    class="flex align-items-center search-param-item"
                    size="small"
                    style="font-size:80%;margin-right:15px;"
                    type="success"
                    @click="() => { visibleAdd = true; }"
                >
                    <SvgIcon :iconWidth="15" iconColor="white" iconName="添加"/>添加用户
                </el-button>
                <el-popconfirm
                    cancel-button-text="取消"
                    confirm-button-text="删除"
                    icon-color="#626AEF"
                    title="确定删除选中数据?"
                    @cancel="() => { }"
                    @confirm="deletes"
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
                            />删除选中
                        </a-button>
                    </template>
                </el-popconfirm>
            </span>
    </div>
    <div>
      <a-table
          :columns="item"
          :data-source="data.lines"
          :pagination="pagination"
          :row-selection="rowSelection"
          :scroll="{ x: 'max-content' }"
          rowKey="userId"
          @change="onChange"
          @resizeColumn="(w: any, col: any) => { col.width = w; }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'avatar'">
            <el-avatar
                :size="40"
                :src="record.avatar"
                style="border:2px solid #3b82f6;"
            />
          </template>
          <template v-if="column.key === 'state'">
                        <span>
                            <a-tag
                                :key="record"
                                :color="userStateMap.get(record.state)?.tagColor"
                            >{{ userStateMap.get(record.state)?.mess }}</a-tag>
                        </span>
          </template>
          <template v-if="column.key === 'operation'">
            <div class="flex align-items-center">
              <!-- <a-button
                  type="primary"
                  size="small"
                  @click="() => { visibleUpdate = true; user = record; }"
              >编辑</a-button>-->
              <a-dropdown-button
                  danger
                  size="small"
                  style="margin-left:10px;"
                  type="primary"
                  @click="() => { visibleUpdate = true; user = record; }"
              >
                <!-- <SvgIcon iconName="eye" :iconWidth="17" iconColor="white" /> -->
                编辑
                <template #overlay>
                  <a-menu>
                    <a-menu-item v-if="record.state==0" key="1">
                      <el-button
                          size="small"
                          type="success"
                          @click="reviewUser(1, record.userId)"
                      >
                        <SvgIcon
                            :iconWidth="20"
                            iconColor="white"
                            iconName="download"
                        />
                        审核通过
                      </el-button>
                    </a-menu-item>
                    <a-menu-item v-if="record.state==0" key="2">
                      <el-button
                          size="small"
                          type="danger"
                          @click="reviewUser(2, record.userId)"
                      >
                        <SvgIcon
                            :iconWidth="20"
                            iconColor="white"
                            iconName="download"
                        />
                        审核不通过
                      </el-button>
                    </a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown-button>
            </div>
          </template>
          <template v-if="column.key === 'sex'">
            <span>{{ record.sex == 0 ? '女' : '男' }}</span>
          </template>
          <template v-if="column.key === 'emailNotice'">
            <span>{{ record.sex == 0 ? '否' : '是' }}</span>
          </template>
        </template>
      </a-table>
    </div>
  </a-card>

  <a-modal
      v-model:visible="visibleUpdate"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
      title="修改用户"
  >
    <UserUpdate :user="user" @refresh="search" @click-close="() => { visibleUpdate = false; }"/>
  </a-modal>

  <a-modal
      v-model:visible="visibleAdd"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
      title="添加用户"
  >
    <UserAdd @refresh="search" @click-close="() => { visibleAdd = false; }"/>
  </a-modal>
</template>

<script lang="ts">
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {useStore} from 'vuex'
import {useRoute, useRouter} from 'vue-router'
import {OrderBy, PageParam} from '@/type/public'
import type {TableColumnsType, TableProps} from 'ant-design-vue';
import {UserGetParam} from '@/type/user'
import {userStateMap} from '@/util/state'
import LeftLabel from "@/components/LeftLabel.vue";
import moment from 'moment'
import {RoleName} from '@/type/role'
import {DepartmentName} from '@/type/department'
import UserUpdate from '@/views/systemmanage/user/UserUpdate.vue'
import UserAdd from '@/views/systemmanage/user/UserAdd.vue'

export default defineComponent({
  components: {
    LeftLabel,
    UserUpdate,
    UserAdd,
  },
  setup() {
    const {proxy}: any = getCurrentInstance();
    const store = useStore()
    const router = useRouter()
    const route = useRoute()

    const searchParam: UserGetParam = reactive({
      pageParam: new PageParam(1, 5),
      orderBys: new Array<OrderBy>(),
      username: '',
      realname: '',
      sex: null,
      tel: '',
      idNumber: '',
      startCreateTime: '',
      endCreateTime: '',
      roleId: null,
      departmentId: null,
      state: null,
    })
    const item = ref<TableColumnsType>([
      {title: "用户名", dataIndex: "username", key: "username", resizable: true},
      {title: "角色", dataIndex: "rolename", key: "rolename", resizable: true},
      {title: "头像", dataIndex: "avatar", key: "avatar", resizable: true},
      {title: "状态", dataIndex: "state", key: "state", resizable: true},
      {title: "邮箱", dataIndex: "email", key: "email", resizable: true},
      {title: "电话", dataIndex: "tel", key: "tel", resizable: true},
      {title: "真名", dataIndex: "realname", key: "realname", resizable: true},
      {title: "性别", dataIndex: "sex", key: "sex", resizable: true},
      {title: "身份证号", dataIndex: "idNumber", key: "idNumber", resizable: true},
      {title: "出生日期", dataIndex: "birth", key: "birth", resizable: true, sorter: {multiple: 1}},
      {title: "创建时间", dataIndex: "createTime", key: "createTime", resizable: true, sorter: {multiple: 2}},
      {title: "创建人姓名", dataIndex: "createUserRealname", key: "createUserRealname", resizable: true},

      {title: "所属部门", dataIndex: "departmentname", key: "departmentname", resizable: true},
      {title: "密码", dataIndex: "password", key: "password", resizable: true},
      {title: "是否开启邮件提醒", dataIndex: "emailNotice", key: "emailNotice", resizable: true},
      {title: "操作", key: "operation", fixed: "right",},
    ]);
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

      proxy.$api.user.getUsers(searchParam)
          .then((response: any) => {
            console.log('&&&&&&&&&&&', response.data)
            data.lines = response.data.data.userVos
            data.total = response.data.data.total
          })
    }

    onMounted(() => {
      search()
      proxy.$api.role.getAllRole()
          .then((response: any) => {
            roles.value = response.data.data
          })
      proxy.$api.department.getAllDepartmentName()
          .then((response: any) => {
            departments.value = response.data.data
          })
    })

    const selectRowKeys = ref<Array<string>>([])//选中的数据
    const rowSelection: TableProps['rowSelection'] = {
      fixed: true,
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        selectRowKeys.value = selectedRowKeys
      },
      getCheckboxProps: (record: any) => ({//不可选择的筛选
        disabled: [-1].indexOf(record.state) != -1,
        name: record.name,
      }),
    };
    const createTimeLimit = ref<any>();//类型暂时改成ang,不然RangeValue会报找不到$d属性
    let departments = ref<Array<DepartmentName>>([])
    let roles = ref<Array<RoleName>>([])


    let visibleUpdate = ref(false)
    let visibleAdd = ref(false)
    let user = ref({})

    function deletes(): void {
      //删除选中用户
      proxy.$api.user.deletes(selectRowKeys.value)
          .then((response: any) => {
            if (response.data.state == proxy.$state.SUCCESS) {
              search()
            }
          })
    }

    function reviewUser(state: number, userId: string): void {
      //点击审核账号
      proxy.$api.user.reviewUser(state, userId)
          .then((response: any) => {
            if (response.data.state == proxy.$state.SUCCESS) {
              search()
            }
          })
    }

    return {
      proxy,
      store,
      router,
      item,
      pagination,
      data,
      onChange,
      search,
      user,
      route,
      userStateMap,
      selectRowKeys,
      rowSelection,
      searchParam,
      createTimeLimit,
      moment,
      departments,
      roles,
      visibleUpdate,
      visibleAdd,
      deletes,
      reviewUser,
    }
  }
})
</script>
