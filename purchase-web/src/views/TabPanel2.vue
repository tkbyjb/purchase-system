<template>
    <el-tabs
        v-model="store.state.tabValue"
        type="border-card"
        closable
        @tab-remove="(targetName: string) => store.commit('removeTab', targetName)"
        @tab-change="changeTab"
        style="background-color: white;margin:10px;"
    >
        <el-tab-pane
            v-for="item in store.state.tabs"
            :key="item.name"
            :label="item.title"
            :name="item.name"
            style="min-height: 80vh;"
        >
            <!-- <keep-alive>
                <router-view :name="item.name"></router-view>
            </keep-alive> -->
        </el-tab-pane>
    </el-tabs>
</template>
<script lang="ts">
import { defineComponent } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

// interface Tab {
//     title: string;
//     name: string;
//     content: string;
// }

export default defineComponent({
    // components: {
    //     ApplyPanel,
    // },
    setup() {
        const store = useStore()
        const router = useRouter()
        // const tabs: Array<Tab> = reactive([])
        // let tabValue = ref('0') //当前活动面板
        // const addTab = (compname: string, tabname: string) => {
        //     for (let tab of tabs) {
        //         if (tab.name == tabname) {
        //             tabValue.value = tab.name
        //             return
        //         }
        //     }
        //     tabs.push({
        //         title: tabname,
        //         name: tabname,
        //         content: compname,
        //     })
        //     tabValue.value = tabname
        // }
        // const removeTab = (targetName: string) => {
        //     tabs.forEach((tab, index) => {
        //         if (tab.name == targetName) {
        //             const nextTab = tabs[index + 1] || tabs[index - 1]
        //             tabs.splice(index, 1)
        //             if (nextTab) {
        //                 tabValue.value = nextTab.name
        //             }
        //         }
        //     })
        // }
        function changeTab(tabname: string): void{
            router.push({
                name: tabname
            })
        }
        return {
            store,
            // addTab,
            // removeTab,
            // tabs,
            // tabValue,
            changeTab,
            router,
        }
    }
})
</script>

<style lang="scss" >
// /deep/[id^="tab-"]{
//     background-color: red;
// }
// /deep/.el-icon.is-icon-plus{
//     display: none;
// }
</style>