<template>
  <div class="flex justify-content-center" style="font-size:120%;">
    当前截止时间: {{ time }}
  </div>
  <div class="flex justify-content-center">
    <LeftLabel label="设置今年采购截止时间" width="180"/>
    <a-date-picker format="YYYY-MM-DD HH:mm:ss" placeholder="选择申请采购截止时间"
                   show-time style="width:250px;" @change="onChange" />
  </div>
  <div class="flex justify-content-center" style="margin:40px;">
    <a-button type="primary" @click="modifyTime">确认修改</a-button>
  </div>

</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, onMounted, ref} from "vue";
import LeftLabel from "@/components/LeftLabel.vue";
import {Dayjs} from "dayjs";

export default defineComponent({
  components: {LeftLabel},
  setup() {
    const {proxy}: any = getCurrentInstance()
    let time = ref()

    let timeValue = ref<Dayjs>()
    function modifyTime(): void {
      //修改时间
      proxy.$api.setting.setDeadline(time.value)
    }

    onMounted(() => {
      proxy.$api.setting.getDeadline()
          .then((response: any) => {
            time.value = response.data.data
          })
    })
    const onChange = (value: Dayjs, dateString: string) => {
      time.value = dateString;
    };
    return {
      modifyTime,
      time,
      proxy,
      onChange,
      timeValue,
    }
  }
})
</script>
