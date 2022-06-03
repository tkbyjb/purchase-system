<template>
  <div class="flex justify-content-center grid">
    <el-form
        :model="registerParam"
        class="sm:col-8 md:col-5 lg:col-4 xl:col-3 purchase-register-form"
        label-position="top"
        label-width="120px"
        :rules="rules"
        ref="ruleFormRef"
    >
      <div class="flex justify-content-between">
                <span
                    style="font-weight: bold;font-size: 130%;color:#7d7d7d;margin-bottom: 30px;"
                >供应商注册</span>
        <span>
                    <router-link
                        :to="{
                            path: '/public/user/register',
                        }"
                        style="color: #3b82f6"
                    >用户注册</router-link>
                </span>
      </div>
      <el-form-item label="供应商名" prop="suppliername">
        <el-input v-model="registerParam.suppliername">
          <template #prefix>
            <SvgIcon :iconWidth="20" iconColor="#3b82f6" iconName="user"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="注册邮箱" prop="email">
        <el-input v-model="registerParam.email">
          <template #prefix>
            <SvgIcon :iconWidth="20" iconColor="#3b82f6" iconName="email"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="登陆用账号名" prop="loginname">
        <el-input v-model="registerParam.loginname">
          <template #prefix>
            <SvgIcon :iconWidth="20" iconColor="#3b82f6" iconName="realname"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="registerParam.password" show-password type="password">
          <template #prefix>
            <SvgIcon :iconWidth="20" iconColor="#3b82f6" iconName="password"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="电话号码" prop="tel">
        <el-input v-model="registerParam.tel">
          <template #prefix>
            <SvgIcon :iconWidth="20" iconColor="#3b82f6" iconName="tel"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="是否学校合作供应商">
        <el-switch
            v-model="registerParam.agreement"
            :active-value="1"
            :inactive-value="0"
            active-text="是"
            inactive-text="否"
            size="large"
        />
      </el-form-item>
      <el-form-item label="验证码">
        <div class="flex">
          <el-input v-model="registerParam.verifyCode"/>
          <el-button
              v-if="canGetVerifyCode"
              style="margin-left:5px;"
              type="primary"
              @click="clickSendVerifyCode"
          >获取验证码
          </el-button>
          <el-button
              v-else
              disabled
              style="margin-left:5px;"
              type="primary"
          >{{ seconds }} 秒后重试
          </el-button>
        </div>
      </el-form-item>
      <div>
        <router-link
            :to="{
                        path: '/public/supplier/login',
                    }"
            style="color: #3b82f6"
        >已有账号?去登录
        </router-link>
      </div>
      <div style="text-align: center;margin-top:40px;">
        <el-button size="large" type="primary" @click="submitForm(ruleFormRef)">注册账号</el-button>
      </div>
    </el-form>
  </div>
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, reactive, ref} from 'vue'
import {SupplierRegisterParam} from '@/type/supplier'
import {DepartmentName} from '@/type/department'
import {ElMessage, FormInstance} from "element-plus";
import {AxiosResponse} from "axios";


export default defineComponent({
  setup() {
    const { proxy }: any = getCurrentInstance()

    const registerParam: SupplierRegisterParam = reactive({
      loginname: '',
      email: '',
      password: '',
      tel: '',
      verifyCode: '',
      agreement: null,
      suppliername: '',
    })
    const departments: Array<DepartmentName> = reactive([])
    let canGetVerifyCode = ref(true)
    let seconds = ref(60);

    function clickSendVerifyCode(): void {
      //点击发送验证码
      if (!/^[A-Za-z0-9\u4e00-\u9fa5]{1,64}@[a-zA-Z0-9_-]{1,64}(\.[a-zA-Z0-9_-]{1,64}){1,64}$/.test(registerParam.email)) {
        ElMessage({
          message: '请输入正确邮箱',
          type: 'warning',
        })
        return;
      }
      proxy.$api.supplier.getRegisterVerifyCode(registerParam.email)
          .then((response: AxiosResponse) => {
            if (response.data.state == proxy.$state.SUCCESS) {
              //验证码发送成功
              canGetVerifyCode.value = false;
              const interval = setInterval(function () {
                seconds.value--;
                if (seconds.value < 0) {
                  clearInterval(interval)
                  canGetVerifyCode.value = true
                  seconds.value = 60
                }
              }, 1000)
            }
          })
    }
    const rules = reactive({
      loginname: [
        { required: true, pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]{2,128}$/,message: '请输入2-128个数字/大小写字母/下划线/中文字符', trigger: 'blur' },
      ],
      suppliername:[
        { required: true, pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]{2,128}$/,message: '请输入2-128个数字/大小写字母/下划线/中文字符', trigger: 'blur' },
      ],
      email: [
        { required: true,pattern: /^[A-Za-z0-9\u4e00-\u9fa5]{1,64}@[a-zA-Z0-9_-]{1,64}(\.[a-zA-Z0-9_-]{1,64}){1,64}$/, message: '请输入正确邮箱', trigger: 'blur' },
      ],
      password: [
        { required: true, pattern: /^[a-zA-Z0-9_]{6,128}$/,message: '请输入6-128个数字/大小写字母/下划线', trigger: 'blur' },
      ],
      tel: [
        { required: true,pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/, message: '请输入11位正确手机号', trigger: 'blur' },
      ],
      verifyCode: [{
        required: true, message: '请输入验证码', trigger: 'blur'
      }]
    })
    const submitForm = async (formEl: FormInstance | undefined) => {
      //点击注册账号,注册成功跳转到登录界面
      if (!formEl) return
      await formEl.validate((valid, fields) => {
        if (valid) {
          //通过验证
          registerParam.password = proxy.$md5(registerParam.password)
          proxy.$api.supplier.register(registerParam)
              .then((response: any)=>{
                if(response.data.state==proxy.$state.SUCCESS){
                  //注册且登录成功,清空输入
                  location.reload()
                }
              })
        } else {
          ElMessage({
            message: '请检查输入是否合法',
            type: 'warning',
          })
        }
      })
    }
    const ruleFormRef = ref<FormInstance>()
    return {
      registerParam,
      departments,
      canGetVerifyCode,
      clickSendVerifyCode,
      seconds,
      rules,
      submitForm,
      ruleFormRef,
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
  margin-bottom: 90px;
}

.grid {
  margin: 0 !important;
}
</style>
