<template>
  <el-descriptions title="申请详情" :column="column" size="large" border>
    <template #extra>
      <!-- <el-button type="primary">Operation</el-button> -->
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
      <el-button type="text" size="small"><a :href="reviewInfo.applyUnreviewVo.attachment" target="_blank">下载附件</a></el-button>
    </el-descriptions-item>
    <el-descriptions-item :span="column">
      <template #label>明细</template>
      <el-table :data="reviewInfo.detailUnreviewVos" style="width: 100%">
        <el-table-column prop="detailname" label="明细名" width="180"/>
        <el-table-column prop="spendingType" label="类型" width="180"/>
        <el-table-column prop="predictUnitPrice" label="预估单价"/>
        <el-table-column prop="count" label="数量"/>
        <el-table-column prop="predictTotalPrice" label="预估总价"/>
        <el-table-column prop="unit" label="单位"/>
        <el-table-column label="备注信息" width="120">
          <template #default="scoped">
            <el-button
                type="text"
                size="small"
                @click="() => { visibleDetailNote = true; viewDetailNote = scoped.row.note; }"
            >明细备注
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-descriptions-item>
    <el-descriptions-item :span="4">
      <template #label>申请备注</template>
      <NoteView
          :view-note="reviewInfo.applyUnreviewVo.note"
          v-if="reviewInfo.applyUnreviewVo.note != null"
      />
    </el-descriptions-item>
    <el-descriptions-item :span="4">
      <template #label>审核信息</template>
      <el-table :data="reviewInfo.reviewUnreviewVos" style="width: 100%">
        <el-table-column prop="reviewType" label="审核部门" width="180">
          <template
              #default="scope"
          >{{
              scope.row.reviewType == 1 ? '部门管理' : scope.row.reviewType == 2 ? '财务部' : scope.row.reviewType == 3 ? '财务部' : '资产部管理员'
            }}
          </template>
        </el-table-column>
        <el-table-column prop="reviewUserRealname" label="审核人" width="180"/>
        <el-table-column prop="result" label="审核结果">
          <template #default="scope">{{ scope.row.result == 1 ? '通过' : '未通过' }}</template>
        </el-table-column>
        <el-table-column prop="createTime" label="审核时间"/>
        <el-table-column label="审核意见" width="120">
          <template #default="scoped">
            <el-button
                type="text"
                size="small"
                @click="() => { visibleReviewNote = true; viewReviewNote = scoped.row.opinion; }"
            >查看审核意见
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-descriptions-item>
  </el-descriptions>

  <el-card style="margin-top:40px;">
    <div style="font-size:120%;font-weight: bold;margin:20px;">我的审核</div>
    <el-form>
      <el-form-item label="审核意见">
        <MavonEditorCuVue ref="editor"/>
      </el-form-item>
      <el-form-item label="审核结果">
        <el-radio :label="1" size="large" border v-model="reviewParam.result">通过</el-radio>
        <el-radio :label="0" size="large" border v-model="reviewParam.result">不通过</el-radio>
      </el-form-item>
<!--      <el-form-item label="指定下一个审核人" v-if="reviewParam.result==1">-->
<!--        <a-select-->
<!--            v-model:value="reviewParam.assignUserId"-->
<!--            class="search-param-item"-->
<!--            placeholder="选择审核人"-->
<!--            size="default"-->
<!--            style="width: 200px"-->
<!--        >-->
<!--          <a-select-option v-for="(item) in assignUsers" :key="item" :value="item.userId">{{-->
<!--              item.realname-->
<!--            }}-->
<!--          </a-select-option>-->
<!--        </a-select>-->
<!--      </el-form-item>-->
      <el-form-item label="明细采购方式方式(拖动)" v-if="reviewParam.result==1">
        <div class="flex justify-content-between align-items-top">
          <el-card style="margin-right:5px;">
            <template #header>
              <a-tag color="#108ee9">协议采购</a-tag>
            </template>
            <draggable
                v-model="purchaseWay0"
                group="detail"
                item-key="detailId"
                class="purchase-draggable"
            >
              <template #item="{ element }">
                <div class="purchase-draggable-item">{{ element.detailname }}</div>
              </template>
            </draggable>
          </el-card>
          <el-card style="margin-right:5px;">
            <template #header>
              <a-tag color="#87d068">待分配采购方式的明细</a-tag>
            </template>
            <draggable
                v-model="details"
                group="detail"
                item-key="detailId"
                class="purchase-draggable"
            >
              <template #item="{ element }">
                <div class="purchase-draggable-item">{{ element.detailname }}</div>
              </template>
            </draggable>
          </el-card>
          <el-card>
            <template #header>
              <a-tag color="#108ee9">其他采购</a-tag>
            </template>
            <draggable
                v-model="purchaseWay1"
                group="detail"
                item-key="detailId"
                class="purchase-draggable"
            >
              <template #item="{ element }">
                <div class="purchase-draggable-item">{{ element.detailname }}</div>
              </template>
            </draggable>
          </el-card>
        </div>
      </el-form-item>
      <div class="flex justify-content-center">
        <el-button type="primary" size="large" @click="toReview">确认审核</el-button>
      </div>
    </el-form>
  </el-card>

  <a-modal
      v-model:visible="visibleDetailNote"
      :destroyOnClose="true"
      title="明细备注"
      :footer="null"
      :closable="true"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
  >
    <NoteView :view-note="viewDetailNote"/>
  </a-modal>

  <a-modal
      v-model:visible="visibleReviewNote"
      :destroyOnClose="true"
      title="明细备注"
      :footer="null"
      :closable="true"
      :keyboard="false"
      :maskClosable="false"
      okText="添加"
      style="width:1000px;"
  >
    <NoteView :view-note="viewReviewNote"/>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, getCurrentInstance, reactive, ref} from "vue";
import {ReviewInfo} from '@/type/apply'
import {applyStateMap} from '@/util/state'
import NoteView from '@/components/NoteView.vue'
import MavonEditorCuVue from "@/components/MavonEditorCu.vue";
import {Review3Param} from '@/type/review'
import {useRoute} from "vue-router";
import draggable from 'vuedraggable'
import {ElMessage} from "element-plus";

export default defineComponent({
  components: {
    NoteView,
    MavonEditorCuVue,
    draggable,
  },
  setup() {
    const {proxy}: any = getCurrentInstance()
    const route = useRoute()

    let applyId = ref(route.query.applyId)
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
      proxy.$api.apply.getReviewInfo3(applyId.value)
          .then((response: any) => {
            reviewInfo.value = response.data.data
            reviewParam.applyId = reviewInfo.value.applyUnreviewVo.applyId
            details.value = response.data.data.detailUnreviewVos
          })
      // getAssignUsers()
    })
    let column = ref(4)
    const reviewParam: Review3Param = reactive({
      applyId: '',
      result: 1,
      opinion: '',
      purchaseWay0: [],
      purchaseWay1: [],
      assignUserId: '',
    })
    let editor = ref()
    let visibleDetailNote = ref(false)
    let viewDetailNote = ref('')

    function toReview(): void {
      //点击确认审核按钮
      if (reviewParam.result == 1) {//通过申请需要指定采购方式
        if (details.value.length > 0) {//最少是有一个明细的
          ElMessage({
            type: 'warning',
            message: '还有未分配采购方式的明细',
          })
          return
        }
        reviewParam.opinion = editor.value.getMd()
        reviewParam.purchaseWay0.length = 0
        purchaseWay0.value.forEach((p: any) => {
          reviewParam.purchaseWay0.push(p.detailId)
        })
        reviewParam.purchaseWay1.length = 0
        purchaseWay1.value.forEach((p: any) => {
          reviewParam.purchaseWay1.push(p.detailId)
        })
      }
      console.log(reviewParam)
      proxy.$api.review.reviewAdd3(reviewParam)
          .then((response: any) => {
            //添加审核成功，关闭此界面,刷新待审核申请列表
            console.log(response.data)
            if (response.data.state == proxy.$state.SUCCESS) {
              setTimeout(() => {
                //
              }, 1000)
            }
          })
    }

    let visibleReviewNote = ref(false)
    let viewReviewNote = ref(false)

    let purchaseWay0 = ref([]) //协议购买列表
    let purchaseWay1 = ref([]) //其他购买列表
    let details = ref([]) //复制的一份明细数组


    let assignUsers = ref<Array<any>>([])

    function getAssignUsers(): void {
      //获取可以审核的人列表
      proxy.$api.user.getAssignUsers(3)
          .then((response: any) => {
            assignUsers.value = response.data.data
          })
    }
    return {
      proxy,
      reviewInfo,
      column,
      applyStateMap,
      reviewParam,
      editor,
      visibleDetailNote,
      viewDetailNote,
      toReview,
      visibleReviewNote,
      viewReviewNote,
      route,
      applyId,
      purchaseWay0,
      purchaseWay1,
      details,
      assignUsers,
      getAssignUsers,
    }
  }
})
</script>

<style lang="scss" scoped>
.purchase-draggable {
  width: 18vw;
  border: 1px solid #108ee9;
  min-height: 300px;
}

.purchase-draggable-item {
  border: 1px solid #87d068;
  padding: 10px;
  //background-color: #fafafa;
}
</style>
