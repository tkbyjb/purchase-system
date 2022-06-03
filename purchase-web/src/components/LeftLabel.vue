<template>
    <span class="ant-input-group-addon leftlabel  flex align-items-center align-content-between"
    :style="{width: width+'px'}">{{label}}</span>
</template>

<script lang="ts">
import { defineComponent } from "vue";
// 给不能添加右侧label的查询条件框用的
export default defineComponent({
    props: ['label','width'],
})
</script>

<style lang="scss" scoped>
.leftlabel {
    height: 32px;
    width:80px;
    border-radius: 0;
}
</style>