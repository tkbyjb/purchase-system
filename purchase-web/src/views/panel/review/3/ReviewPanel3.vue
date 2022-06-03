<template>
    <div>
        <a-card style="width:100%;">
            <div class="flex align-items-center" style="margin-bottom:20px;">
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
                >
                    <template #bodyCell="{ column, record }">
                        <template v-if="column.key === 'state'">
                            <span>
                                <a-tag
                                    :key="record.state"
                                    :color="applyStateMap.get(record.state)?.tagColor"
                                >{{ applyStateMap.get(record.state)?.mess }}</a-tag>
                            </span>
                        </template>
                        <template v-if="column.key === 'operation'">
                            <div class="flex align-items-center">
                                <a-button
                                    type="primary"
                                    size="small"
                                    @click="toDo(record.applyId)"
                                >去处理</a-button>
                            </div>
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
                </a-table>
            </div>
            <div class="flex align-items-center flex-wrap"></div>
        </a-card>
    </div>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, reactive, computed, onMounted, ref } from "vue";
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { PageParam, OrderBy } from '@/type/public'
import type { TableColumnsType } from 'ant-design-vue';
import { applyStateMap } from '@/util/state'
import { useRoute } from "vue-router";

export default defineComponent({
    setup() {
        const { proxy }: any = getCurrentInstance();
        const store = useStore()
        const router = useRouter()
        const route = useRoute()

        let level = ref(route.meta.level)
        const searchParam = reactive({
            pageParam: new PageParam(1, 5),
            orderBys: new Array<OrderBy>(),
        })
        const item = ref<TableColumnsType>([
            { title: "申请编号", dataIndex: "serialNumber", key: "serialNumber", resizable: true },
            { title: "申请名", dataIndex: "applyname", key: "applyname", resizable: true },
            { title: "当前状态", dataIndex: "state", key: "state", resizable: true },
            { title: "是否下一年计划", dataIndex: "putoff", key: "putoff", resizable: true },
            { title: "申请人", dataIndex: "applyUsername", key: "applyUsername", resizable: true },
            { title: "申请部门", dataIndex: "applyDepartmentname", key: "applyDepartmentname", resizable: true },
            { title: "提交审核时间", dataIndex: "applyTime", key: "applyTime", resizable: true, sorter: { multiple: 2 } },
            { title: "操作", key: "operation", fixed: "right", },
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
            console.log(searchParam)
            proxy.$api.apply.getUnreview3(searchParam)
                .then((response: any) => {
                    data.lines = response.data.data.applyUnreviewVos
                    data.total = response.data.data.total
                })
        }
        onMounted(() => {
            search()
        })
        function toDo(applyId: string): void {
            //点击去审核
            router.push({
                name: 'Review3',
                query: {
                    applyId: applyId,
                }
            })
            const tab = {
                title: '采购申请审核',
                name: 'Review3',
                content: 'Review3',
            }
            store.commit('addTab', tab)
            router.push({
                name: 'Review3',
                query: {
                    applyId: applyId,
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
            applyStateMap,
            route,
            level,
            toDo,
        }
    }
})
</script>
