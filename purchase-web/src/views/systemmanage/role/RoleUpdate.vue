<template>
    <el-form>
        <el-form-item label="角色名">
            <el-input v-model="updateParam.rolename"/>
        </el-form-item>
        <el-form-item label="备注">
            <el-input type="textarea" :rows="5"  v-model="updateParam.note"/>
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
                :default-checked-keys="choosedMenus"
                :highlight-current="true"
                :check-on-click-node="true"
                ref="tree"
            />
        </el-form-item>
        <div class="flex justify-content-center">
            <el-button type="primary" @click="clickUpdate">保存修改</el-button>
        </div>
    </el-form>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, getCurrentInstance, onMounted } from "vue";
import { MenuName } from '@/type/menu'
import { RoleUpdateParam } from '@/type/role'


export default defineComponent({
    props: ['role'],
    emits: ['click-close', 'refresh'],
    setup(props, context) {
        const { proxy }: any = getCurrentInstance()
        const updateParam: RoleUpdateParam = reactive({
            roleId: props.role.roleId,
            rolename: props.role.rolename,
            note: props.role.note,
            menuIds: [],
        })
        let menuTree = ref([])
        onMounted(() => {
            proxy.$api.menu.getMenuTree()
                .then((response: any) => {
                    menuTree.value = response.data.data
                })
        })
        const choosedMenus = reactive<Array<string>>([])
        function getChild(m: MenuName): void {//这里有个坑啊,父组件只要选中子节点就都选中,要排除父节点,只保留叶子节点
            if(m.childs == null || m.childs.length < 1) {
                choosedMenus.push(m.menuId)
            }
            for (let m2 of m.childs) {
                getChild(m2)
            }
        }
        for (let m of props.role.menus) {
            getChild(m)
        }
        console.log(choosedMenus)
        const tree = ref()
        function clickUpdate(): void {
            //点击修改按钮
            for(let m of tree.value.getCheckedKeys()) {
                updateParam.menuIds.push(m)
            }
            for(let m of tree.value.getHalfCheckedKeys()) {
                updateParam.menuIds.push(m)
            }
            proxy.$api.role.updateRole(updateParam)
            .then((response: any)=>{
                if(response.data.state == proxy.$state.SUCCESS) {
                    context.emit('refresh')
                    context.emit('click-close')
                }
            })
        }
        return {
            proxy,
            updateParam,
            menuTree,
            choosedMenus,
            tree,
            clickUpdate,
        }
    }
})
</script>