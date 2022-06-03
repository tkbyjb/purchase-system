<template>
    <div>
        <a-card style="width:100%;">
            <div class="flex align-items-center" style="margin-bottom:20px;">
                <SvgIcon iconName="search" :iconWidth="20" iconColor="#5c5c5c" />
                <span style="color:#5c5c5c;">筛选搜索</span>
            </div>
            <div class="flex align-items-center flex-wrap">
                <span class="search-param-item flex">
                    <LeftLabel label="结果" :width="50" />
                    <a-select
                        v-model:value="searchParam.result"
                        size="default"
                        placeholder="选择状态"
                        style="width: 100px"
                        class="search-param-item"
                    >
                        <a-select-option :key="-1" :value="null">全部</a-select-option>
                        <a-select-option :key="1" :value="1">通过</a-select-option>
                        <a-select-option :key="0" :value="0">不通过</a-select-option>
                    </a-select>
                </span>
                <span class="search-param-item flex">
                    <LeftLabel label="审核时间" :width="80" />
                    <a-range-picker v-model:value="createTimeLimit" format="YYYY-MM-DD" />
                </span>
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
                        <template v-if="column.key === 'reviewType'">
                            <span>
                                <a-tag
                                    :key="record.reviewType"
                                    :color="record.reviewType == 1 ? 'blue' : record.reviewType == 2 ? 'purple' : record.reviewType == 2 ? 'orange' : 'cyan'"
                                >{{ record.reviewType == 1 ? '部门管理审核' : record.reviewType == 2 ? '财务部审核' : record.reviewType == 2 ? '资产部审核' : '资产部管理员审核' }}</a-tag>
                            </span>
                        </template>
                        <template v-if="column.key === 'result'">
                            <span>
                                <a-tag
                                    :key="record.result"
                                    :color="record.result == 1 ? 'green' : 'red'"
                                >{{ record.result == 1 ? '通过' : '不通过' }}</a-tag>
                            </span>
                        </template>
                        <template v-if="column.key === 'operation'">
                            <span>
                                <el-button size="small" type="primary" @click="()=>{visibleNote=true;viewNote=record.opinion;}">
                                    <SvgIcon iconName="eye" :iconWidth="22" iconColor="white" />审核意见
                                </el-button>
                            </span>
                        </template>
                    </template>
                </a-table>
            </div>
        </a-card>
    </div>

    <a-modal
        v-model:visible="visibleNote"
        :destroyOnClose="true"
        title="审核意见"
        :footer="null"
        :closable="true"
        :keyboard="false"
        :maskClosable="false"
        okText="添加"
        style="width:1000px;"
    >
        <NoteView :view-note="viewNote" />
    </a-modal>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, reactive, computed, onMounted, ref } from "vue";
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { PageParam, OrderBy } from '@/type/public'
import type { TableColumnsType } from 'ant-design-vue';
import { applyStateMap } from '@/util/state'
import { useRoute } from "vue-router";
import {ReviewHistoryGetParam} from '@/type/review'
import NoteView from '@/components/NoteView.vue'
import type { Dayjs } from 'dayjs';
type RangeValue = [Dayjs, Dayjs];
import moment from 'moment'

export default defineComponent({
    components: {
        NoteView,
    },
    setup() {
        const { proxy }: any = getCurrentInstance();
        const store = useStore()
        const router = useRouter()
        const route = useRoute()
        let level = ref(route.meta.level)
        const searchParam: ReviewHistoryGetParam = reactive({
            pageParam: new PageParam(1, 5),
            orderBys: new Array<OrderBy>(),
            startCreateTime: '',
            endCreateTime: '',
            result: null,
        })
        const item = ref<TableColumnsType>([
            { title: "审核人", dataIndex: "reviewUserRealname", key: "reviewUserRealname", resizable: true },
            { title: "审核类型", dataIndex: "reviewType", key: "reviewType", resizable: true },
            { title: "审核结果", dataIndex: "result", key: "result", resizable: true },
            { title: "审核时间", dataIndex: "createTime", key: "createTime", resizable: true , sorter: { multiple: 1 } },
            { title: "审核的组合编号", dataIndex: "serialNumber", key: "serialNumber", resizable: true },
            { title: "组合名", dataIndex: "applyname", key: "applyname", resizable: true },
            { title: "创建人", dataIndex: "applyUsername", key: "applyUsername", resizable: true},
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
            if (createTimeLimit.value && createTimeLimit.value.length > 1) {
                searchParam.startCreateTime = moment(createTimeLimit.value[0].$d).format('yyyy-MM-DD HH:mm:ss')
                searchParam.endCreateTime = moment(createTimeLimit.value[1].$d).format('yyyy-MM-DD HH:mm:ss')
            } else {
                searchParam.startCreateTime = ''
                searchParam.endCreateTime = ''
            }
            proxy.$api.review.getReviewHistory(level.value, searchParam)
                .then((response: any) => {
                    data.lines = response.data.data.reviewHistoryVos
                    data.total = response.data.data.total
                })
        }
        onMounted(() => {
            search()
        })
        let visibleNote = ref(false)
        let viewNote = ref('')

        const createTimeLimit = ref<any>();
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
            viewNote,
            visibleNote,
            searchParam,
            createTimeLimit,
            moment,
        }
    }
})
</script>
