<template>
  <a-config-provider :locale="zhCN">
    <router-view />
  </a-config-provider>
</template>

<script lang="ts">
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import { defineComponent } from 'vue';

export default defineComponent({
  setup() {
    return {
      zhCN
    }
  }
})
</script>

<style lang="scss">
body {
  margin: 0;
  background-color: #e9ecef;
}
.search-param-item {
    margin-right: 10px;
    margin-bottom: 10px;
}
</style>
