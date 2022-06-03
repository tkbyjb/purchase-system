<template>
  <el-descriptions :column="column" border size="large" title="申请详情">
    <template #extra>
    </template>
    <el-descriptions-item :span="column">
      <template #label>申请编号</template>
      {{ reviewInfo.applyUnreviewVo.serialNumber }}
    </el-descriptions-item>
    <el-descriptions-item :span="column">
      <template #label>申请名</template>
      {{ reviewInfo.applyUnreviewVo.applyname }}
    </el-descriptions-item>
    <el-descriptions-item :span="1">
      <template #label>申请人</template>
      {{ reviewInfo.applyUnreviewVo.applyUsername }}
    </el-descriptions-item>
    <el-descriptions-item :span="1">
      <template #label>申请学院</template>
      {{ reviewInfo.applyUnreviewVo.applyDepartmentname }}
    </el-descriptions-item>
    <el-descriptions-item :span="2">
      <template #label>申请时间</template>
      {{ reviewInfo.applyUnreviewVo.applyTime }}
    </el-descriptions-item>
    <el-descriptions-item :span="2">
      <template #label>当前状态</template>
      <a-tag
          :key="reviewInfo.applyUnreviewVo.state"
          :color="applyStateMap.get(reviewInfo.applyUnreviewVo.state)?.tagColor"
      >{{ applyStateMap.get(reviewInfo.applyUnreviewVo.state)?.mess }}
      </a-tag>
    </el-descriptions-item>
    <el-descriptions-item :span="1">
      <template #label>是否下一年计划</template>
      <a-tag
          :key="reviewInfo.applyUnreviewVo.putoff"
          :color="reviewInfo.applyUnreviewVo.putoff === 0 ? 'blue' : 'red'"
      >{{ reviewInfo.applyUnreviewVo.putoff == 0 ? '否' : '是' }}
      </a-tag>
    </el-descriptions-item>
    <el-descriptions-item :span="1">
      <template #label>附件</template>
      <el-button size="small" type="text">下载附件</el-button>
    </el-descriptions-item>
    <el-descriptions-item :span="column">
      <template #label>明细</template>
      <el-table :data="reviewInfo.detailUnreviewVos" style="width: 100%">
        <el-table-column label="明细名" prop="detailname" width="180"/>
        <el-table-column label="类型" prop="spendingType" width="180"/>
        <el-table-column label="预估单价" prop="predictUnitPrice"/>
        <el-table-column label="数量" prop="count"/>
        <el-table-column label="预估总价" prop="predictTotalPrice"/>
        <el-table-column label="单位" prop="unit"/>
        <el-table-column label="备注信息" width="120">
          <template #default="scoped">
            <el-button size="small" type="text"
                       @click="()=>{visibleDetailNote=true;viewDetailNote=scoped.row.note;}">明细备注
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-descriptions-item>
    <el-descriptions-item :span="4">
      <template #label>申请备注</template>
      <NoteView
          v-if="reviewInfo.applyUnreviewVo.note != null"
          :view-note="reviewInfo.applyUnreviewVo.note"
      />
    </el-descriptions-item>
    <el-descriptions-item :span="4">
      <template #label>审核信息</template>
      <el-table :data="reviewInfo.reviewUnreviewVos" style="width: 100%">
        <el-table-column label="审核部门" prop="reviewType" width="180">
          <template #default="scope">
            {{
              scope.row.reviewType == 1 ? '部门管理' : scope.row.reviewType == 2 ? '财务部' : scope.row.reviewType == 3 ? '财务部' : '资产部管理员'
            }}
          </template>
        </el-table-column>
        <el-table-column label="审核人" prop="reviewUserRealname" width="180"/>
        <el-table-column label="审核结果" prop="result">
          <template #default="scope">
            {{ scope.row.result == 1 ? '通过' : '未通过' }}
          </template>
        </el-table-column>
        <el-table-column label="审核时间" prop="createTime"/>
        <el-table-column label="审核意见" width="120">
          <template #default="scoped">
            <el-button size="small" type="text"
                       @click="()=>{visibleReviewNote=true;viewReviewNote=scoped.row.opinion;}">查看审核意见
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-descriptions-item>
  </el-descriptions>


  <a-modal
      v-model:visible="visibleDetailNote"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
      title="明细备注"
  >
    <NoteView :view-note="viewDetailNote"/>
  </a-modal>


  <a-modal
      v-model:visible="visibleReviewNote"
      :closable="true"
      :destroyOnClose="true"
      :footer="null"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
      title="明细备注"
  >
    <NoteView :view-note="viewReviewNote"/>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {ReviewInfo} from '@/type/apply'
import {applyStateMap} from '@/util/state'
import NoteView from '@/components/NoteView.vue'
import {ReviewParam} from '@/type/review'

export default defineComponent({
  components: {
    NoteView,
  },
  props: ['applyId', 'level'],
  emits: ['click-close', 'refresh'],
  setup(props, context) {
    let levelC = ref(props.level)
    const {proxy}: any = getCurrentInstance()
    const reviewInfo = ref<ReviewInfo>({
      applyUnreviewVo: {
        applyId: '',
        serialNumber: '',
        applyname: '',
        applyUsername: '',
        applyDepartmentname: '',
        applyTime: '',
        note: null,
        attachment: null,
        state: 0,
        putoff: 0,
      },
      detailUnreviewVos: [],
      reviewUnreviewVos: [],
    })
    onMounted(() => {
      proxy.$api.apply.getReviewInfo(levelC.value, props.applyId)
          .then((response: any) => {
            reviewInfo.value = response.data.data
            reviewParam.applyId = reviewInfo.value.applyUnreviewVo.applyId
          })
    })
    let column = ref(4)
    const reviewParam: ReviewParam = reactive({
      applyId: '',
      result: 1,
      opinion: '',
      assignUserId: '',
    })
    let editor = ref()
    let visibleDetailNote = ref(false)
    let viewDetailNote = ref('')

    let visibleReviewNote = ref(false)
    let viewReviewNote = ref(false)


    return {
      proxy,
      reviewInfo,
      column,
      applyStateMap,
      reviewParam,
      editor,
      visibleDetailNote,
      viewDetailNote,
      levelC,
      visibleReviewNote,
      viewReviewNote,
    }
  }
})
</script>
