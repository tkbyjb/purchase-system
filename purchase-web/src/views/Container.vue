<template>
    <keep-alive>
        <Navbar id="parchase-2navbar" />
    </keep-alive>
    <div id="purchase-menu-parent">
        <keep-alive>
            <Menu id="purchase-menu" />
        </keep-alive>
        <div class="purchase-router-view" :class="{ 'isCollapse': store.state.isCollapse }">
            <keep-alive>
                <router-view />
            </keep-alive>
        </div>
    </div>
</template>


<script lang="ts">
import { defineComponent } from 'vue'
import Navbar from '@/components/Navbar.vue'
import Menu from '@/components/Menu.vue'
import { useStore } from 'vuex'

export default defineComponent({
    components: {
        Navbar,
        Menu,
    },
    setup() {
        const store = useStore()
        return {
            store,
        }
    }
})
</script>

<style lang="scss" scoped>
// #parchase-2navbar{
//     position: fixed !important;
//     top:0px;
// }
#purchase-menu-parent {
    background-color: #e9ecef;
}
#purchase-menu {
    background-color: #f5f5f5ff;
    height: 100vh;
    position: fixed;
    bottom: 0px;
    top: 45px;
    // scroll
    overflow-y: auto;
}
.purchase-router-view {
    padding-top: 44px;
    padding-left: 15%;
}
.isCollapse {
    padding-left: 60px !important;
}
</style>
<style lang="scss">
.el-menu::-webkit-scrollbar {
    width: 4px;
    height: 10px;
    background: white; /*可以设置轨道颜色*/
    padding-right: 2px;
}
.el-menu::-webkit-scrollbar-thumb {
    background: #e2e3e5;
    border-radius: 10px;
}
</style>