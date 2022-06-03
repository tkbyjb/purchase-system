<template>

  <!--      <a-steps :current="0">-->
  <!--        <a-step title="选择明细" description="第一步"/>-->
  <!--        <a-step title="选择明细" description="第一步"/>-->
  <!--      </a-steps>-->


  <a-card style="width:100%;">
    <div class="flex align-items-center" style="margin-bottom:20px;">
      <SvgIcon iconName="search" :iconWidth="20" iconColor="#5c5c5c"/>
      <span style="color:#5c5c5c;">筛选搜索</span>
    </div>
    <div class="flex align-items-center flex-wrap">
      <a-input
          v-model:value="searchParam.detailname"
          style="width:250px;"
          addon-before="申明细名"
          addon-after
          allowClear
          :maxlength="30"
          size="default"
          class="search-param-item"
      />
      <a-input
          v-model:value="searchParam.applySerialNumber"
          style="width:250px;"
          addon-before="所属申请编号"
          addon-after
          allowClear
          :maxlength="30"
          size="default"
          class="search-param-item"
      />
      <span class="search-param-item flex">
                <LeftLabel label="所属部门" :width="80"/>
                <a-select
                    v-model:value="searchParam.departmentId"
                    size="default"
                    placeholder="选择状态"
                    style="width: 200px"
                    class="search-param-item"
                >
                    <a-select-option :key="-1" :value="null">全部</a-select-option>
                    <a-select-option
                        v-for="(item) in departments"
                        :key="item"
                        :value="item.departmentId"
                    >{{ item.departmentname }}</a-select-option>
                </a-select>
            </span>
      <span class="search-param-item flex">
                <LeftLabel label="类型" :width="50"/>
                <a-select
                    v-model:value="searchParam.spendingTypeId"
                    placeholder="选择类型"
                    style="width: 200px"
                    class="search-param-item"
                >
                  <a-select-option :key="-1" :value="null">全部</a-select-option>
                <a-select-option
                    v-for="(item) in spendingTypes"
                    :key="item"
                    :value="item.spendingTypeId"
                >{{ item.typename }}</a-select-option>
            </a-select>
            </span>
      <span class="search-param-item flex" style="margin-top:12px;">
                <a-button
                    type="primary"
                    class="flex align-items-center search-param-item"
                    @click="search"
                >
                    <SvgIcon iconName="search" :iconWidth="20" iconColor="white"/>搜索
                </a-button>
            </span>
    </div>
  </a-card>
  <div class="flex align-items-center">
    <el-card style="margin-right:5px;">
      <template #header>
        <a-tag color="#108ee9">待组合的明细</a-tag>
      </template>
      <draggable
          v-model="unCombDetails"
          group="detail"
          item-key="detailId"
          class="purchase-draggable"
      >
        <template #item="{ element }">
          <div class="purchase-draggable-item flex justify-content-around">
                <span style="overflow : hidden;white-space :nowrap;text-overflow :ellipsis;max-width: 200px;">{{
                    element.detailname
                  }}</span>
            <a-tag color="blue">{{ element.spendingType }}</a-tag>
            <span> <a-tag color="orange">预计单价: {{ element.predictUnitPrice }}</a-tag></span>
            <span><a-tag color="purple">数量: {{ element.count }}</a-tag></span>
            <!--                <el-button type="primary" size="small" @click="()=>{visibleDetail=true;}">-->
            <!--                  <SvgIcon icon-name="eye" :icon-width="18" icon-color="white"/>-->
            <!--                </el-button>-->
          </div>
        </template>
      </draggable>
    </el-card>
    <div>
      <SvgIcon icon-name="右箭头" :icon-width="30" icon-color="#3b82f6"/>
    </div>
    <el-card style="margin-right:5px;">
      <template #header>
        <a-tag color="#87d068">新组合</a-tag>
      </template>
      <draggable
          v-model="combDetails"
          group="detail"
          item-key="detailId"
          class="purchase-draggable"
      >
        <template #item="{ element }">
          <div class="purchase-draggable-item flex justify-content-around">
                <span style="overflow : hidden;white-space :nowrap;text-overflow :ellipsis;max-width: 200px;">{{
                    element.detailname
                  }}</span>
            <a-tag color="blue">{{ element.spendingType }}</a-tag>
            <span> <a-tag color="orange">预计单价: {{ element.predictUnitPrice }}</a-tag></span>
            <span><a-tag color="purple">数量: {{ element.count }}</a-tag></span>
            <!--                <el-button type="primary" size="small">-->
            <!--                  <SvgIcon icon-name="eye" :icon-width="18" icon-color="white"/>-->
            <!--                </el-button>-->
          </div>
        </template>
      </draggable>
    </el-card>
  </div>
  <div style="margin-top:50px;">
    <el-form>
      <el-form-item label="组合名">
        <el-input v-model="updateParam.combname"/>
      </el-form-item>
      <el-form-item label="备注">
        <MavonEditorCu ref="editor"/>
      </el-form-item>
    </el-form>
  </div>
  <div style="margin-left:15px;" class="flex justify-content-center">
    <a-button type="primary" size="large" @click="updateComb">保存修改</a-button>
  </div>
</template>

<script lang="ts">
import draggable from 'vuedraggable'
import {defineComponent, getCurrentInstance, ref, reactive, onMounted} from "vue";
import SvgIcon from "@/components/SvgIcon.vue";
import {DetailUncombGetParam} from "@/type/detail";
import {ElMessage} from "element-plus";
import {DepartmentName} from "@/type/department";
import {SpendingType} from "@/type/spending";
import MavonEditorCu from "@/components/MavonEditorCu.vue";
import {useRoute} from "vue-router";
import {CombUpdateParam} from "@/type/comb";

export default defineComponent({
  components: {
    SvgIcon,
    draggable,
    MavonEditorCu,
  },
  setup() {
    const {proxy}: any = getCurrentInstance();
    const route = useRoute()
    let combId = ref(route.query.combId)
    let unCombDetails = ref([])
    let combDetails = ref([])
    let searchParam: DetailUncombGetParam = reactive({
      applySerialNumber: '',
      detailname: '',
      departmentId: null,
      spendingTypeId: null,
      purchaseWay: parseInt(route.query.way as string),
    })
    const data = reactive({
      lines: [],
      total: 0,
    })
    function search(): void {
      proxy.$api.detail.getUncombDetails(searchParam)
          .then((response: any) => {
            data.lines = response.data.data
            unCombDetails.value = response.data.data
          })
    }
    onMounted(() => {
      search()
      proxy.$api.department.getAllDepartmentName()
          .then((response: any) => {
            departments.value = response.data.data
          })
      proxy.$api.spending.getSpendingTypes()
          .then((response: any) => {
            spendingTypes.value = response.data.data
          })
    })
    let visibleDetail = ref(false)
    function updateComb(): void {
      //点击添加组合
      if (combDetails.value.length < 1) {
        ElMessage({
          type: 'warning',
          message: '请先添加明细',
        })
        return
      }
      if(updateParam.combname.replace(' ', '') == ''){
        ElMessage({
          type: 'warning',
          message: '组合名不能为空',
        })
        return
      }
      updateParam.note = editor.value.getMd()
      updateParam.detailIds.length = 0
      combDetails.value.forEach((c: any)=>{
        updateParam.detailIds.push(c.detailId)
      })
      proxy.$api.comb.updateComb(updateParam)
          .then((response: any)=>{
            if(response.data.state == proxy.$state.SUCCESS) {
              combDetails.value.length = 0
              search();
            }
          })
    }

    let departments = ref<Array<DepartmentName>>([])
    let spendingTypes = ref<Array<SpendingType>>([])
    let updateParam = reactive<CombUpdateParam>({
      combId: route.query.combId as string,
      combname: '',
      note: '',
      detailIds: [],
    })
    let editor = ref()
    return {
      proxy,
      unCombDetails,
      combDetails,
      searchParam,
      search,
      visibleDetail,
      updateComb,
      departments,
      spendingTypes,
      updateParam,
      editor,
      route,
      combId,
    }
  }
})
</script>

<style lang="scss" scoped>
.purchase-draggable {
  width: 33vw;
  min-height: 400px;
  border: 1px solid #108ee9;
  amrgin: 10px;
}

.purchase-draggable-item {
  border: 1px solid #108ee9;
  padding: 10px;
}
</style>
