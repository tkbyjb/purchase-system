<template>
    <a-tabs
        v-model:activeKey="store.state.tabValue"
        hide-add
        type="editable-card"
        @change="changeTab"
        :closable="true"
        style="margin:10px;"
        @edit="editTab"
        size="small"
    >
        <a-tab-pane
            v-for="item in store.state.tabs"
            :key="item.name"
            :tab="item.title"
            :closable="true"
            style="min-height: 86vh;background-color: white;padding:15px;"
        >
            <!-- 这个写法不能缓存到 -->
            <!-- <keep-alive>
                <router-view :name="item.name" ></router-view>
            </keep-alive>-->
            <router-view v-slot="{ Component, route }" :name="item.name">
                <keep-alive>
                    <component :is="Component" :key="route.name" />
                </keep-alive>
            </router-view>
        </a-tab-pane>
    </a-tabs>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";


export default defineComponent({
    setup() {
        const store = useStore()
        const router = useRouter()
        function changeTab(tabname: string): void {
            router.push({
                name: tabname
            })
        }
        function editTab(targetName: string, action: string): void {
            // alert("eidt")
            if (action == 'remove') {
                store.commit('removeTab', targetName)
                router.push({
                    name: store.state.tabValue
                })
            }
        }
        return {
            store,
            changeTab,
            router,
            editTab,
        }
    }
})
</script>
