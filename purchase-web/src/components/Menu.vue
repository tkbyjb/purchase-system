<template>
  <el-menu
      :collapse="store.state.isCollapse"
      :default-openeds="[]"
      :router="false"
      class="el-menu-vertical-demo"
      default-active="2"
  >
    <el-sub-menu v-for="(item, index) in apis" :key="item" :index="index+''">
      <template #title>
        <SvgIcon :iconName="item.icon" :iconWidth="20" iconColor="#3b82f6"/>
        <span>{{ item.menuname }}</span>
      </template>
      <!-- 匹配到route了的才显示,因为有些api是不需要单独一个页面的 -->
      <el-menu-item
          v-for="(item2,index2) in item.childs.filter((i: any) => { return i.route })"
          :key="item2"
          :index="index+'-'+index2"
          @click="addTab(item2)"
      >
        <SvgIcon
            v-if="item2.icon != null"
            :iconName="item2.icon"
            :iconWidth="22"
            iconColor="#3b82f6"
        />
        <span>{{ item2.menuname }}</span>
      </el-menu-item>
      <el-sub-menu
          v-for="(item2,index2) in item.childs.filter((i: any) => { return i.permission == null })"
          :key="item2"
          :index="index+'-'+index2"

      >
        <template #title>
          <SvgIcon
              v-if="item2.icon != null"
              :iconName="item2.icon"
              :iconWidth="22"
              iconColor="#3b82f6"
          />
          <span>{{ item2.menuname }}</span></template>
        <el-menu-item v-for="(item3,index3) in item2.childs.filter((i: any) => { return i.route })"
                      :key="item3"
                      :index="index+'-'+index2+'-'+index3"
                      @click="addTab(item3)"
        >{{ item3.menuname }}
        </el-menu-item>
      </el-sub-menu>
    </el-sub-menu>
  </el-menu>
</template>

<script lang="ts">
import {Menu} from '@/type/menu'
import {defineComponent, reactive} from 'vue'
import {useStore} from 'vuex'
import {useRoute, useRouter} from 'vue-router'

export default defineComponent({
  setup() {
    const store = useStore()
    const router = useRouter()
    const apis: Array<Menu> = reactive(JSON.parse(localStorage.getItem('permission') as string))

    const route = useRoute()

    function addTab(menu: Menu): void {
      console.log('*******', route.matched)
      //点击菜单添加一个tab到store
      const tab = {
        title: menu.menuname,
        name: menu.route.name,
        content: menu.route.name,
      }
      store.commit('addTab', tab)
      router.push({
        name: menu.route.name,
      })
    }

    return {
      store,
      apis,
      addTab,
      router,
      route,
    }
  }
})
</script>
<style lang="scss" scoped>
.el-menu-vertical-demo {
  width: 15%;
}

.el-menu--collapse {
  width: 60px;
}

.routerlink {
  text-decoration: none;
  color: #3b82f6;
}
</style>
