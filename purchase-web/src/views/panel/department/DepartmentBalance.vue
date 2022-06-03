<template>
    <div>
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
                <span class="search-param-item flex" style="margin-top:11px;">
                    <a-button
                        type="primary"
                        class="flex align-items-center search-param-item"
                        @click="search"
                    >
                        <SvgIcon iconName="search" :iconWidth="20" iconColor="white" />搜索
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
                    <SvgIcon iconName="清单" :iconWidth="22" iconColor="#5c5c5c" />
                    <span style="color:#5c5c5c;">数据</span>
                    <a-button
                        type="primary"
                        size="small"
                        style="margin-left:15px;font-size:70%;"
                        class="flex align-items-center"
                        @click="search"
                    >
                        <SvgIcon iconName="刷新" :iconWidth="15" iconColor="white" />刷新数据
                    </a-button>
                </spna>
            </div>
            <div>
                <a-table
                    :columns="item"
                    :data-source="data.lines"
                    :pagination="pagination"
                    @change="onChange"
                    :scroll="{ x: 'max-content' }"
                    rowKey="applyId"
                    @resizeColumn="(w: any, col: any) => { col.width = w; }"
                ></a-table>
            </div>
        </a-card>
    </div>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, reactive, computed, ref, onMounted } from "vue";
import type { TableProps, TableColumnType } from 'ant-design-vue';
import type { TableColumnsType } from 'ant-design-vue';
import { PageParam, OrderBy } from '@/type/public'
import { DepartmentGetParam } from '@/type/department'

export default defineComponent({
    setup() {
        const { proxy }: any = getCurrentInstance();

        const item = ref<TableColumnsType>([
            { title: "部门名", dataIndex: "departmentname", key: "departmentname", resizable: true },
            { title: "经费余额", dataIndex: "balance", key: "balance", resizable: true },
            // { title: "操作", key: "operation", fixed: "right", },
        ]);
        const data = reactive({
            lines: [],
            total: 0,
        })
        const searchParam: DepartmentGetParam = reactive({
            pageParam: new PageParam(1, 5),
            departmentname: '',
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
            search()
        }
        function search(): void {
            proxy.$api.department.getBalance(searchParam)
                .then((response: any) => {
                    console.log(response.data)
                    data.lines = response.data.data.departmentBalanceVos
                    data.total = response.data.data.total
                })
        }
        onMounted(() => {
            search()
        })
        return {
            data,
            searchParam,
            proxy,
            item,
            onChange,
            pagination,
            search,
        }
    }
})
</script>
