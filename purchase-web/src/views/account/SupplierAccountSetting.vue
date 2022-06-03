<template>
  <div style="margin:20px;" >
    <el-card >
      <template #header>账号设置</template>
      <el-form ref="ruleFormRef" :model="updateParam" :rules="rules" label-width="120px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="updateParam.username" style="width:250px;"/>
        </el-form-item>
        <el-form-item label="用户头像(需要修改时才重新上传)">
          <a-upload
              v-model:file-list="updateParam.fileList"
              :beforeUpload="beforeUpload"
              :maxCount="1"
              action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
              list-type="picture-card"
          >
            <div v-if="updateParam.fileList && updateParam.fileList.length < 1">
              <plus-outlined/>
              <div style="margin-top: 8px">上传图片</div>
            </div>
          </a-upload>
        </el-form-item>
        <el-form-item label="注册邮箱" prop="email">
          <el-input v-model="updateParam.email" style="width:250px;"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="updateParam.password" show-password type="password" style="width:250px;"/>
        </el-form-item>
        <el-form-item label="电话号码" prop="tel">
          <el-input v-model="updateParam.tel" style="width:250px;"/>
        </el-form-item>
        <el-form-item label="供应商详情">
          <MavonEditorCu />
        </el-form-item>
        <div style="margin-top:40px;margin-left:100px;">
          <a-button type="primary" @click="submitForm(ruleFormRef)">保存修改</a-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts">
import {defineComponent, getCurrentInstance, reactive, ref, toRaw} from 'vue'
import {AccountSettingParam} from '@/type/user'
import type {FormInstance} from 'element-plus'
import {ElMessage} from 'element-plus'
import type {UploadProps} from 'ant-design-vue';
import MavonEditorCu from "@/components/MavonEditorCu.vue";

export default defineComponent({
  components: {MavonEditorCu},
  setup() {
    const { proxy }: any = getCurrentInstance()
    let ruleFormRef = ref()
    const updateParam: AccountSettingParam = reactive({
      userId: '',
      username: '',
      email: '',
      password: '',
      tel: '',
      emailNotice: 1,
      fileList: [
        {
          uid: '-1',
          name: 'image.png',
          status: 'done',
          url: '',
        },
      ],
    })
    const oldPassword = ref('')
    const oldAvatar = ref('')
    const rules = reactive({
      username: [
        { required: true, pattern: /^[a-zA-Z0-9_\u4e00-\u9fa5]{2,128}$/, message: '请输入2-128个数字/大小写字母/下划线/中文字符', trigger: 'blur' },
      ],
      email: [
        { required: true, pattern: /^[A-Za-z0-9\u4e00-\u9fa5]{1,64}@[a-zA-Z0-9_-]{1,64}(\.[a-zA-Z0-9_-]{1,64}){1,64}$/, message: '请输入正确邮箱', trigger: 'blur' },
      ],
      password: [
        { required: true, pattern: /^[a-zA-Z0-9_]{6,128}$/, message: '请输入6-128个数字/大小写字母/下划线', trigger: 'blur' },
      ],
      tel: [
        { required: true, pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/, message: '请输入11位正确手机号', trigger: 'blur' },
      ],
    })
    const submitForm = async (formEl: FormInstance | undefined) => {
      if (!formEl) return
      await formEl.validate((valid, fields) => {
        if (valid) {
          let formData = new FormData()
          if (oldPassword.value != updateParam.password) {//改了密码才加密
            updateParam.password = proxy.$md5(updateParam.password)
          }
          //判断头像是否是重新上传的
          if (updateParam.fileList && updateParam.fileList?.length > 0) {
            if (updateParam.fileList[0].url === oldAvatar.value) {//没改头像
              // updateParam.fileList = undefined;
            } else {
              for (let file of updateParam.fileList) {
                formData.append('files', file.originFileObj as Blob);
              }
            }
          }
          for (let e of Object.entries(toRaw(updateParam))) {
            if (e[0] != 'files' && e[0] != 'fileList') {
              formData.append(e[0], e[1])
            }
          }
          proxy.$api.user.updateUser(formData)
        } else {
          ElMessage({
            message: '请检查输入是否合法',
            type: 'warning',
          })
          return
        }
      })
    }
    const beforeUpload: UploadProps['beforeUpload'] = (file: any) => {
      if (file.size > 4000000) {
        ElMessage({
          message: '文件不大于4MB',
          type: 'warning',
        })
        return false;
      }
      if (file.type.indexOf('image') == -1) {
        ElMessage({
          message: '只能上传图片类型文件',
          type: 'warning',
        })
        return false;
      }
      updateParam.fileList = [file];
      return false;
    };
    return {
      updateParam,
      rules,
      ruleFormRef,
      submitForm,
      proxy,
      beforeUpload,
    }
  }
})
</script>
