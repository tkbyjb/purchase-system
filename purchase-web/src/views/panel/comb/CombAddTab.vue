<template>
  <el-tabs v-model="tabname">
    <el-tab-pane label="添加协议采购" name="first">
      <CombAdd :way="0" />
    </el-tab-pane>
    <el-tab-pane label="添加其他采购" name="second">
      <CombAdd :way="1" />
    </el-tab-pane>
  </el-tabs>
</template>

<script lang="ts">
import {defineComponent, ref} from "vue";
import CombAdd from "@/views/panel/comb/CombAdd.vue";
export  default  defineComponent({
  components: {
    CombAdd,
  },
  setup(){
    let tabname= ref('first')
    return{
      tabname,
    }
  }
})
</script>
