<template>
  <el-card style="width:550px;height:500px;">
    <template #header>
      <div class="flex justify-content-between align-items-center">
        <a-range-picker v-model:value="timelimit"/>

        <a-select
            v-model:value="searchParam.departmentId"
            placeholder="选择部门"
            style="width:100px;"
            @click.once="getDepartments"
        >
          <a-select-option :key="-100" :value="null">全部</a-select-option>
          <a-select-option
              v-for="item in departments"
              :key="item.departmentId"
              :value="item.departmentId"
          >{{ item.departmentname }}
          </a-select-option>
        </a-select>

        <el-button @click="getSpendingTypePercentage">
          <SvgIcon :icon-width="22" icon-color="#3b82f6" icon-name="reload"/>
        </el-button>
      </div>
    </template>
    <div ref="chart1" style="width:450px;height:400px;"></div>
  </el-card>
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";

import * as echarts from 'echarts';
import SvgIcon from "@/components/SvgIcon.vue";
import {DepartmentName} from "@/type/department";
import {AxiosResponse} from "axios";
import moment from "moment";
import {SpendingTypePercentageGetParam} from "@/type/statistics";

export default defineComponent({
  components: {SvgIcon},
  setup() {
    const {proxy}: any = getCurrentInstance()
    const departments = ref<Array<DepartmentName>>([])

    function getDepartments(): void {
      //获取部门列表
      proxy.$api.department.getAllDepartmentName()
          .then((response: AxiosResponse) => {
            console.log(response.data)
            departments.value = response.data.data
          })
    }

    let chart1 = ref<HTMLElement>()
    let timelimit = ref<any>()
    const searchParam = reactive<SpendingTypePercentageGetParam>({
      startTime: null,
      endTime: null,
      departmentId: null,
    })
    let data = ref([])


    async function getSpendingTypePercentage(): Promise<void> {
      //获取支出类型占比
      if (timelimit.value && timelimit.value.length > 1) {
        searchParam.startTime = moment(timelimit.value[0].$d).format('yyyy-MM-DD HH:mm:ss')
        searchParam.endTime = moment(timelimit.value[1].$d).format('yyyy-MM-DD HH:mm:ss')
      } else {
        searchParam.startTime = null
        searchParam.endTime = null
      }
      await proxy.$api.statistics.getSpendingTypePercentage(searchParam)
          .then((response: any) => {
            data.value = response.data.data
            setChart(data.value)
          })
    }

    function setChart(data: Array<any>): void {
      let myChart1 = echarts.init(chart1.value!);
      let option = {
        title: {
          text: '支出类型占比',
          subtext: '',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        series: [
          {
            name: '金额(元)',
            type: 'pie',
            radius: '50%',
            data: data,
            label: {
              normal: {
                show: true,
                formatter: '{b}:{c}元({d}%)'
              }
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      myChart1.setOption(option);
    }

    onMounted(async () => {
      await getSpendingTypePercentage()
    })
    return {
      chart1,
      getSpendingTypePercentage,
      getDepartments,
      departments,
      searchParam,
      timelimit,
      data,
      setChart,
    }
  }
})
</script>
