<template>
    <a-card style="width:100%;">
        <div class="flex align-items-center" style="margin-bottom:20px;">
            <SvgIcon iconName="search" :iconWidth="20" iconColor="#5c5c5c" />
            <span style="color:#5c5c5c;">筛选搜索</span>
        </div>
        <div class="flex align-items-center flex-wrap">
            <a-input
                v-model:value="searchParam.departmentname"
                style="width:250px;"
                addon-before="部门名"
                addon-after
                allowClear
                :maxlength="30"
                size="default"
                class="search-param-item"
            />
            <a-button
                type="primary"
                class="flex align-items-center search-param-item"
                @click="search"
                style="margin-left:10px;"
            >
                <SvgIcon iconName="search" :iconWidth="20" iconColor="white" />搜索
            </a-button>
        </div>
    </a-card>
    <a-card style="width:100%;">
        <div class="flex align-items-center justify-content-between" style="margin-bottom:20px;">
            <spna class="flex align-items-center">
                <SvgIcon iconName="清单" :iconWidth="22" iconColor="#5c5c5c" />
                <span style="color:#5c5c5c;">数据</span>
                <a-button type="primary" size="small" style="margin-left:15px;font-size:70%;" 
                    class="flex align-items-center" @click="search">
                    <SvgIcon iconName="刷新" :iconWidth="15" iconColor="white" />
                    刷新数据</a-button>
            </spna>
            <span class="flex">
                <el-button
                    type="success"
                    class="flex align-items-center search-param-item"
                    size="small"
                    style="font-size:80%;margin-right:15px;"
                    @click="() => { visibleAdd = true; }"
                >
                    <SvgIcon iconName="添加" :iconWidth="15" iconColor="white" />添加部门
                </el-button>
                <el-popconfirm
                    confirm-button-text="删除"
                    cancel-button-text="取消"
                    icon-color="#626AEF"
                    title="确定删除选中数据?"
                    @confirm="deletes"
                    @cancel="() => { }"
                >
                    <template #reference>
                        <a-button
                            type="primary"
                            class="flex align-items-center search-param-item"
                            size="small"
                            style="font-size:80%;"
                        >
                            <SvgIcon iconName="回收站" :iconWidth="15" iconColor="white" />删除选中
                        </a-button>
                    </template>
                </el-popconfirm>
            </span>
        </div>
        <div>
            <a-table
                :row-selection="rowSelection"
                :columns="item"
                :data-source="data.lines"
                :pagination="pagination"
                @change="onChange"
                :scroll="{ x: 'max-content' }"
                rowKey="departmentId"
                @resizeColumn="(w: any, col: any) => { col.width = w; }"
            >
                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'type'">
                        <a-tag
                            :key="record.type"
                            :color="record.type == 0 ? 'green' : 'blue'"
                        >{{ record.type == 0 ? '学院' : '部门' }}</a-tag>
                    </template>
                    <template v-if="column.key === 'operation'">
                        <el-button
                            type="primary"
                            size="small"
                            @click="() => { visibleUpdate = true; department = record }"
                        >
                            <SvgIcon
                                iconName="edit"
                                :iconWidth="15"
                                iconColor="white"
                                @click="search"
                            />修改
                        </el-button>
                    </template>
                </template>
            </a-table>
        </div>
    </a-card>

    <a-modal
        v-model:visible="visibleUpdate"
        :destroyOnClose="true"
        title="部门修改"
        :footer="null"
        :closable="true"
        :keyboard="false"
        :maskClosable="false"
        okText="添加"
        style="width:800px;"
    >
        <DepartmentUpdate
            :department="department"
            @refresh="search"
            @click-close="() => { visibleUpdate = false; }"
        />
    </a-modal>

    <a-modal
        v-model:visible="visibleAdd"
        :destroyOnClose="true"
        title="部门添加"
        :footer="null"
        :closable="true"
        :keyboard="false"
        :maskClosable="false"
        okText="添加"
        style="width:800px;"
    >
        <DepartmentAdd @refresh="search" @click-close="() => { visibleAdd = false; }" />
    </a-modal>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, ref, reactive, computed, onMounted } from "vue";
import type { TableColumnsType } from 'ant-design-vue';
import { PageParam } from '@/type/public'
import type { TableProps, TableColumnType } from 'ant-design-vue';
import { DepartmentGetParam } from '@/type/department'
import DepartmentUpdate from '@/views/systemmanage/department/DepartmentUpdate.vue'
import DepartmentAdd from '@/views/systemmanage/department/DepartmentAdd.vue'
import { ElMessage } from "element-plus";
export default defineComponent({
    components: {
        DepartmentUpdate,
        DepartmentAdd,
    },
    setup() {
        const { proxy }: any = getCurrentInstance();

        const data = reactive({
            lines: [],
            total: 0,
        })
        const searchParam: DepartmentGetParam = reactive({
            departmentname: '',
            pageParam: new PageParam(1, 5),
        });
        const item = ref<TableColumnsType>([
            { title: "部门名称", dataIndex: "departmentname", key: "departmentname", resizable: true },
            { title: "部门类型", dataIndex: "type", key: "type", resizable: true },
            { title: "剩余经费", dataIndex: "balance", key: "balance", resizable: true },
            { title: "备注", dataIndex: "note", key: "note", resizable: true },
            { title: "操作", key: "operation", fixed: "right", },
        ]);
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
            search()
        }
        function search(): void {
            proxy.$api.department.get(searchParam)
                .then((response: any) => {
                    data.lines = response.data.data.departmentVos;
                    data.total = response.data.data.total;
                })
        }
        const selectRowKeys = ref<Array<string>>([])//选中的数据
        const rowSelection: TableProps['rowSelection'] = {
            fixed: true,
            onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
                selectRowKeys.value = selectedRowKeys
            },
            // getCheckboxProps: (record: any) => ({//不可选择的筛选
            //     disabled: [1, 20, 21].indexOf(record.state) == -1,
            //     name: record.name,
            // }),
        };
        onMounted(() => {
            search()
        })
        function deletes(): void {
            //点击删除选中按钮
            if(selectRowKeys.value.length < 1){
                ElMessage({
                    type: 'warning',
                    message: '请先选中要删除的数据',
                })
                return
            }
            proxy.$api.department.deleteDepartments(selectRowKeys.value)
                .then((response: any) => {
                    //删除成功刷新表数据
                    if (response.data.state == proxy.$state.SUCCESS) {
                        search()
                    }
                })
        }
        let visibleUpdate = ref(false)
        const department = reactive({})
        let visibleAdd = ref(false)
        return {
            proxy,
            item,
            pagination,
            data,
            search,
            onChange,
            rowSelection,
            searchParam,
            deletes,
            visibleUpdate,
            department,
            visibleAdd,
        }
    }
})
</script>