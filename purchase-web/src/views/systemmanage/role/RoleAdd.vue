<template>
    <el-form>
        <el-form-item label="角色名">
            <el-input v-model="addParam.rolename"/>
        </el-form-item>
        <el-form-item label="备注">
            <el-input type="textarea" :rows="5"  v-model="addParam.note"/>
        </el-form-item>
        <el-form-item label="权限">
            <el-tree
                :data="menuTree"
                show-checkbox
                node-key="menuId"
                :props="{
                    children: 'childs',
                    label: 'menuname',
                }"
                :default-expand-all="true"
                :highlight-current="true"
                :check-on-click-node="true"
                ref="tree"
            />
        </el-form-item>
        <div class="flex justify-content-center">
            <el-button type="primary" @click="clickUpdate">添加角色</el-button>
        </div>
    </el-form>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, getCurrentInstance, onMounted } from "vue";
import { RoleAddParam } from '@/type/role'


export default defineComponent({
    emits: ['click-close', 'refresh'],
    setup(props, context) {
        const { proxy }: any = getCurrentInstance()
        const addParam: RoleAddParam = reactive({
            rolename: '',
            note: '',
            menuIds: [],
        })
        let menuTree = ref([])
        onMounted(() => {
            proxy.$api.menu.getMenuTree()
                .then((response: any) => {
                    menuTree.value = response.data.data
                })
        })
        const tree = ref()
        function clickUpdate(): void {
            //点击修改按钮

            for(let m of tree.value.getCheckedKeys()) {
                addParam.menuIds.push(m)
            }
            for(let m of tree.value.getHalfCheckedKeys()) {
                addParam.menuIds.push(m)
            }
            console.log(addParam)
            proxy.$api.role.addRole(addParam)
            .then((response: any)=>{
                if(response.data.state == proxy.$state.SUCCESS) {
                    context.emit('refresh')
                    context.emit('click-close')
                }
            })
        }
        return {
            proxy,
            addParam,
            menuTree,
            tree,
            clickUpdate,
        }
    }
})
</script>