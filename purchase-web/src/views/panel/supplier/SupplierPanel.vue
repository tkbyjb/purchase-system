<template>
  <div>
    <a-card style="width:100%;">
      <div class="flex align-items-center" style="margin-bottom:20px;">
        <SvgIcon :iconWidth="20" iconColor="#5c5c5c" iconName="search"/>
        <span style="color:#5c5c5c;">筛选搜索</span>
      </div>
      <div class="flex align-items-center flex-wrap">
        <a-input
            v-model:value="searchParam.suppliername"
            :maxlength="30"
            addon-after
            addon-before="供应商名"
            allowClear
            class="search-param-item"
            size="default"
            style="width:250px;"
        />
        <a-input
            v-model:value="searchParam.introduce"
            :maxlength="30"
            addon-after
            addon-before="介绍"
            allowClear
            class="search-param-item"
            size="default"
            style="width:250px;"
        />
        <span class="search-param-item flex">
                    <LeftLabel :width="120" label="是否合作商"/>
                    <a-select
                        v-model:value="searchParam.agreement"
                        class="search-param-item"
                        placeholder="选择"
                        size="default"
                        style="width: 100px"
                    >
                        <a-select-option :key="-1" :value="null">全部</a-select-option>
                        <a-select-option :key="1" :value="1">是</a-select-option>
                        <a-select-option :key="0" :value="0">否</a-select-option>
                    </a-select>
                </span>
        <a-button
            class="flex align-items-center search-param-item"
            type="primary"
            @click="search"
        >
          <SvgIcon :iconWidth="20" iconColor="white" iconName="search"/>
          搜索
        </a-button>
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
      </div>
      <div>
        <a-table
            :columns="item"
            :data-source="data.lines"
            :pagination="pagination"
            :scroll="{ x: 'max-content' }"
            rowKey="supplierId"
            @change="onChange"
            @resizeColumn="(w: any, col: any) => { col.width = w; }"
        >
          <template #bodyCell="{ column, record }">
            <template v-if="column.key === 'agreement'">
              <a-tag v-if="record.agreement==1" color="green">
                是
              </a-tag>
              <a-tag v-if="record.agreement==0" color="blue">
                不是
              </a-tag>
            </template>
            <template v-if="column.key === 'operation'">
<!--              <el-button size="small" type="primary">去询价</el-button>-->
<!--              <el-button size="small" type="success" style="margin-left:10px;"-->
<!--              @click="chooseSupplier(record)">选择</el-button>-->
              <el-button size="small" type="warning" style="margin-left:10px;">发送消息</el-button>
            </template>
          </template>
          <template #expandedRowRender="{ record }">
            <NoteView :view-note="record.introduce"/>
          </template>
        </a-table>
      </div>
    </a-card>
  </div>
</template>

<script lang="ts">
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {OrderBy, PageParam} from "@/type/public";
import {useStore} from "vuex";
import {useRouter} from "vue-router";
import {TableColumnsType} from "ant-design-vue";
import {applyCanEditStateList, applyStateMap} from "@/util/state";
import {SupplierGetParam} from '@/type/supplier'
import NoteView from "@/components/NoteView.vue";


export default defineComponent({
  components: {
    NoteView,
  },
  setup() {

    const {proxy}: any = getCurrentInstance();
    const store = useStore()
    const router = useRouter()

    const item = ref<TableColumnsType>([
      {title: "供应商名", dataIndex: "suppliername", key: "suppliername", resizable: true},
      {title: "电话", dataIndex: "tel", key: "tel", resizable: true},
      {title: "是否合作商", dataIndex: "agreement", key: "agreement", resizable: true},
      {title: "加入时间", dataIndex: "createTime", key: "createTime", resizable: true},
      {title: "操作", key: "operation", fixed: "right",},
      // {title: "介绍", dataIndex: "introduce", key: "introduce", resizable: true},
    ]);
    const data = reactive({
      lines: [],
      total: 0,
    })
    const searchParam: SupplierGetParam = reactive({
      introduce: "",
      suppliername: "",
      agreement: null,
      state: null,
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
      proxy.$api.supplier.getSupplier(searchParam)
          .then((response: any) => {
            console.log(response.data)
            data.lines = response.data.data.supplierVos
            data.total = response.data.data.total
          })
    }

    onMounted(() => {
      search()
    })
    function chooseSupplier(record: any): void{
      //点击选择供应商,去更新父组件的数据

    }
    return {
      proxy,
      searchParam,
      item,
      data,
      pagination,
      onChange,
      search,
      applyStateMap,
      applyCanEditStateList,
      store,
      router,
      chooseSupplier,
    }
  }
})
</script>
