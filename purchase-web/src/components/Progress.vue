<template>
    <el-progress :percentage="50" :indeterminate="true" />
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import {ElProgress} from 'element-plus'

export default defineComponent({
    components:{
        ElProgress,
    },
    setup() {
        return {

        }
    }
})
</script>