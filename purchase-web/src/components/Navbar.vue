<template>
  <div class="grid purchase-navbar">
    <div class="flex align-items-center col-10">
      <SvgIcon
          :iconWidth="30"
          class="navbar-item"
          iconColor="#3b82f6"
          iconName="openclosemenu"
          style="cursor:pointer;"
          @click="store.commit('editIsCollapse', !store.state.isCollapse)"
      />
      <img class="navbar-item" src="@/assets/logo.svg"/>
      <span class="navbar-item" style="color:#3b82f6;font-weight: bold;font-size: 120%;">用户中心</span>
      <el-breadcrumb class="navbar-item" separator="/">
        <!-- <el-breadcrumb-item
            v-for="(item) in route.matched.filter(i=>{return i.meta.title!=''})"
            :key="item"
            :to="{ name: item.meta.permission!=null?item.name: undefined }"
        >{{ item.meta.title }}</el-breadcrumb-item>-->
        <el-breadcrumb-item v-for="(item) in titles" :key="item">{{ item }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="col-2 flex align-items-center">
      <a href="#" @click.prevent="isVisible = !isVisible">
        <SvgIcon
            :iconWidth="30"
            iconColor="#3b82f6"
            iconName="chat"
            style="margin-right: 20px;"
        />
      </a>
      <a href="#" @click.prevent>
        <SvgIcon
            :iconWidth="30"
            iconColor="#3b82f6"
            iconName="hint"
            style="margin-right: 20px;"
        />
      </a>
      <el-dropdown>
        <div class="flex align-items-center">
          <el-avatar
              :size="40"
              :src="avatar"
              style="border:2px solid #3b82f6;margin-bottom: 3px;margin-top:4px;"
          />
          <span style="margin-left:5px;font-weight:bold;">{{ username }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>
              <router-link
                  :to="{
                                    path: accountType==0?'/account/setting':'/account/supplier/setting',
                                }"
                  class="routerlinks"
              >账号设置
              </router-link>
            </el-dropdown-item>
            <el-dropdown-item>
              <span class="routerlinks" @click="logout">退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
  <div id="headerProgress"></div>
  <PrimeDialog
      v-model:visible="isVisible"
      :maximizable="true"
      :style="{ width: '600px', borderRadius: '0px' }"
      class="purchase-chat-dialog"
  >
    <Chat/>
  </PrimeDialog>
</template>

<script lang="ts">
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {useStore} from 'vuex'
import Chat from '@/components/Chat.vue'


export default defineComponent({
  components: {
    Chat,
  },
  setup() {
    const {proxy}: any = getCurrentInstance()
    const route = useRoute()
    const router = useRouter()
    const store = useStore()
    let isVisible = ref(false)
    onMounted(() => {
      //
    })
    let titles = computed(() => {
      let ts = reactive<Array<string>>([])
      for (let r of route.matched.filter(i => {
        return i.meta.title != null;
      })) {
        let g: any = r.meta;
        for (let s of g.title) {
          ts.push(s)
        }
      }
      return ts
    })
    const avatar = localStorage.getItem('avatar')
    const username = localStorage.getItem('username')

    function logout() {
      //退出登录
      proxy.$api.user.logout()
          .then((response: any) => {
            if (response.data.state == proxy.$state.SUCCESS) {
              localStorage.removeItem("username")
              localStorage.removeItem("Authorization")
              localStorage.removeItem("permission")
              localStorage.removeItem("avatar")
              router.push({
                path: '/public/user/login'
              })
            }
          })
    }

    let accountType = localStorage.getItem("type")
    return {
      route,
      store,
      isVisible,
      titles,
      avatar,
      logout,
      proxy,
      router,
      username,
      accountType,
    }
  }
})
</script>

<style lang="scss" scoped>
.navbar-item {
  margin-left: 20px;
  margin-right: 10px;
}

.purchase-navbar {
  background-color: white;
  box-shadow: 0px 0px 10px rgba(212, 212, 212, 0.51);
  z-index: 999;
  position: fixed !important;
  top: 0px;
  width: 100%;
}

.grid {
  margin: 0 !important;
}

.col-10 {
  padding: 0px;
}

.col-2 {
  padding: 0px;
}

.routerlinks {
  text-decoration: none;
  color: #3b82f6;
}

.purchase-chat-dialog-content {
  width: 100%;
  height: 400px;
  background-color: #f5f5f5ff;
}
</style>
<style lang="scss">
.p-dialog.purchase-chat-dialog {
  //
}

.purchase-chat-dialog {
  .p-dialog-header {
    background-color: #dededeff;
    padding: 0;
    border-radius: 0;
  }

  .p-dialog-content {
    background-color: #dededeff;
    padding: 0;
  }

  .p-dialog-content::-webkit-scrollbar {
    width: 4px;
    height: 10px;
    background: white; /*设置轨道颜色*/
    padding-right: 2px;
  }

  .p-dialog-content::-webkit-scrollbar-thumb {
    background: #e2e3e5;
    border-radius: 10px;
  }
}
</style>
