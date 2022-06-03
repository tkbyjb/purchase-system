<template>
  <div class="flex justify-content-center grid">
    <el-form
        :model="loginParam"
        label-width="120px"
        label-position="top"
        class="sm:col-8 md:col-5 lg:col-4 xl:col-3 purchase-register-form"
    >
      <div class="flex justify-content-between align-items-center">
        <div
            style="font-weight: bold;font-size: 130%;color:#7d7d7d;margin-bottom: 30px;"
        >供应商登录</div>
        <div>
          <router-link
              :to="{
                            path: '/public/user/login',
                        }"
              style="color: #3b82f6"
          >用户登录</router-link>
        </div>
      </div>
      <el-form-item label="登录名">
        <el-input v-model="loginParam.userUniqueId">
          <template #prefix>
            <SvgIcon iconName="user" :iconWidth="20" iconColor="#3b82f6" />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="loginParam.password" type="password" show-password>
          <template #prefix>
            <SvgIcon iconName="password" :iconWidth="20" iconColor="#3b82f6" />
          </template>
        </el-input>
      </el-form-item>
      <div>
        <router-link
            :to="{
                        path: '/public/supplier/register',
                    }"
            style="color: #3b82f6"
        >还没有账号?去注册</router-link>
      </div>
      <div style="text-align: center;margin-top:40px;">
        <el-button type="primary" size="large" @click="login">登录</el-button>
      </div>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, getCurrentInstance, onMounted } from 'vue'
import { LoginParam } from '@/type/user'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { Menu } from '@/type/menu'

export default defineComponent({
  name: 'Login',
  setup() {
    const { proxy }: any = getCurrentInstance()
    const router = useRouter()

    const loginParam: LoginParam = reactive({
      userUniqueId: '',
      password: '',
    })
    function login(): void {
      //点击登录
      if (loginParam.password == '' && loginParam.userUniqueId == '') {
        ElMessage({
          message: '请输入账号和密码',
          type: 'warning',
        })
        return;
      }
      loginParam.password = proxy.$md5(loginParam.password)
      proxy.$api.supplier.login(loginParam)
          .then((response: any) => {
            console.log(response.data)
            if (response.data.state == proxy.$state.SUCCESS) {
              //登录成功,写入数据到localstorage
              localStorage.setItem("username", response.data.data.username)
              localStorage.setItem("avatar", response.data.data.avatar)
              localStorage.setItem("Authorization", response.data.data.token)
              localStorage.setItem("type", response.data.data.type)
              localStorage.setItem("id", response.data.data.id)
              for (let route of router.options.routes) {
                traverseRoutes(route, response.data.data.menuVos)
              }
              (response.data.data.menuVos)
              localStorage.setItem("permission", JSON.stringify(response.data.data.menuVos))
              router.push({
                path: '/'
              })
            }
          })
    }
    function traverseApis(menu: Menu, route: any): void {
      //遍历menu,找出menu和route匹配的route name ,然后把route加入到权限属性里
      if(route.meta.permission == menu.permission) {
        // route.meta.title.push(menu.menuname)//路由的title实际没用,只是占个位置,还是动态获取的

        menu.route = route
        return
      }
      // route.meta.tip = menu.menuname//动态设置tip浏览器标签页的title
      menu.childs.forEach((item: Menu) => {
        traverseApis(item, route)
      })
    }
    function traverseRoutes(route: any, menus: Array<Menu>): void {
      //遍历routes获取api对应route name
      menus.forEach((item: Menu)=>{
        traverseApis(item, route)
      })
      if (route.children) {
        for (let r of route.children) {
          traverseRoutes(r, menus)
        }
      }
    }
    onMounted(() => {
      // document.onkeydown = (e) => {
      //     if (e.keyCode == 13) {
      //         login()
      //     }
      // };
    })
    return {
      proxy,
      loginParam,
      login,
      router,
      traverseRoutes,
      traverseApis,
    }
  }
})
</script>

<style lang="scss" scoped>
.purchase-register-form {
  background-color: white;
  padding: 40px;
  border-radius: 15px;
  border: 2px solid #9132c9;
  margin-top: 50px;
}
</style>
