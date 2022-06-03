<template>
  <el-card style="width:1000px;height:500px;">
    <template #header>
      <div class="flex justify-content-between align-items-center">
        <a-range-picker v-model:value="timelimit"/>

        <a-select
            v-model:value="searchParam.departmentId"
            placeholder="选择部门"
            style="width:150px;"
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

        <a-select
            v-model:value="searchParam.step"
            placeholder="选择形式"
            style="width:100px;"
            @click.once="getDepartments"
        >
          <a-select-option :key="1" :value="'day'">按天</a-select-option>
          <a-select-option :key="2" :value="'month'">按月</a-select-option>
          <a-select-option :key="3" :value="'year'">按年</a-select-option>
        </a-select>

        <el-button @click="getData">
          <SvgIcon :icon-width="22" icon-color="#3b82f6" icon-name="reload"/>
        </el-button>
      </div>
    </template>
    <div ref="chart1" style="width:900px;height:400px;"></div>
  </el-card>
</template>


<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, reactive, ref} from "vue";

import * as echarts from 'echarts';
import SvgIcon from "@/components/SvgIcon.vue";
import moment from "moment";
import {SpendingTypePercentageGetParam} from "@/type/statistics";
import {DepartmentName} from "@/type/department";
import {AxiosResponse} from "axios";

export default defineComponent({
  components: {SvgIcon},
  setup() {
    const {proxy}: any = getCurrentInstance()
    let chart1 = ref<HTMLElement>()
    let timelimit = ref<any>()
    const searchParam = reactive<SpendingTypePercentageGetParam>({
      startTime: null,
      endTime: null,
      departmentId: null,
      step: 'day',
    })
    let data = ref([])

    async function getData(): Promise<void> {
      if (timelimit.value && timelimit.value.length > 1) {
        searchParam.startTime = moment(timelimit.value[0].$d).format('yyyy-MM-DD HH:mm:ss')
        searchParam.endTime = moment(timelimit.value[1].$d).format('yyyy-MM-DD HH:mm:ss')
      } else {
        searchParam.startTime = null
        searchParam.endTime = null
      }
      await proxy.$api.statistics.getApplyCount(searchParam)
          .then((response: any) => {
            data.value = response.data.data
            let time = new Array<any>()
            let count = new Array<any>()
            let amount = new Array<any>()
            data.value.forEach((p: any)=>{
              time.push(p.date)
              count.push(p.count)
              amount.push(p.amount)
            })
            setChart(time, count, amount)
          })
    }

    function setChart(time: Array<any>,count: Array<any>,amount: Array<any>): void {
      let myChart1 = echarts.init(chart1.value!);
      let option = {
            title: {
              text: '申请数量/金额'
            },
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data: ['申请数量', '金额']
            },
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              containLabel: true
            },
            toolbox: {
              feature: {
                saveAsImage: {}
              }
            },
            xAxis: {
              type: 'category',
              boundaryGap: false,
              data: time,
            },
            yAxis: [
              {
                name: "个",
              }
              ,
              {
                name: "元",
              }
              ,
            ],
            series: [
              {
                name: '申请数量',
                type: 'line',
                stack: 'Total',
                data: count,
                yAxisIndex: 0,
                smooth: true,
              },
              {
                name: '金额',
                type: 'line',
                stack: 'Total',
                data: amount,
                yAxisIndex: 1,
                smooth: true,
              },
            ]
          }
      ;
      myChart1.setOption(option);
    }

    onMounted(async () => {
      await getData()
    })
    const departments = ref<Array<DepartmentName>>([])
    function getDepartments(): void {
      proxy.$api.department.getAllDepartmentName()
          .then((response: AxiosResponse) => {
            console.log(response.data)
            departments.value = response.data.data
          })
    }
    return {
      chart1,
      getData,
      searchParam,
      timelimit,
      data,
      setChart,
      departments,
      getDepartments,
    }
  }
})
</script>
