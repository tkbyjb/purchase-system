import { createRouter, createWebHashHistory, RouteRecordRaw, createWebHistory } from 'vue-router'


const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: () => import('@/views/Container.vue'),
    name: 'home',
    meta: {
      title: ['首页'],
      permission: '',
    },
    children: [
      {
        path: 'tabs',
        name: 'tabs',
        component: () => import('@/views/TabPanelAntd.vue'),
        meta: {
          title: '',
          permission: '',
        },
        children: [
          //需要tabs的路由panel
          {
            path: 'statistics',
            name: 'statistics',
            component: () => import('@/views/account/Register.vue'),
            meta: {
              title: ['统计'],
              permission: '',
            },
          },
          // 物管员路由  /////////////////////////////////////////////////////////////////////
          {
            path: 'apply/panel',
            name: 'ApplyPanel',
            components: {
              ApplyPanel: () => import('@/views/panel/apply/ApplyPanel.vue'),
            },
            meta: {
              title: ['采购申请','查看申请'],
              permission: 'apply-get',
            },
          },
          {
            path: 'apply/update',
            name: 'ApplyUpdate',
            components: {
              ApplyUpdate: () => import('@/views/panel/apply/ApplyUpdate.vue'),
            },
            meta: {
              title: ['修改申请'],
              permission: '',//不显示到菜单上
            },
          },
          {
            path: 'apply/add',
            name: 'ApplyAdd',
            components: {
              ApplyAdd: () => import('@/views/panel/apply/ApplyAdd.vue'),
            },
            meta: {
              title: ['采购申请','添加申请'],
              permission: 'apply-add',
            },
          },
          //  部门管理员路由/////////////////////////////////////////////////////////////////////
          {
            path: 'review/panel',
            name: 'ReviewPanel',
            components: {
              ReviewPanel: () => import('@/views/panel/review/ReviewPanel.vue'),
            },
            meta: {
              title: ['采购审批','部门管理待审核'],
              permission: 'apply-unreview-1-get',
              level: 1,
            },
          },
          {
            path: 'review/history',
            name: 'ReviewHistory',
            components: {
              ReviewHistory: () => import('@/views/panel/review/ReviewHistory.vue'),
            },
            meta: {
              title: ['采购审批','部门管理审核记录'],
              permission: 'apply-review-1-get',
              level: 1,
            },
          },
          //财务部路由 /////////////////////////////////////////////////////////////////////
          {
            path: 'department/balance',
            name: 'DepartmentBalance',
            components: {
              DepartmentBalance: () => import('@/views/panel/department/DepartmentBalance.vue'),
            },
            meta: {
              title: ['财务管理','部门经费'],
              permission: 'department-balance',
            },
          },
          {
            path: 'pay/panel',
            name: 'PayPanel',
            components: {
              PayPanel: () => import('@/views/panel/pay/PayPanel.vue'),
            },
            meta: {
              title: ['财务管理','待支付账单'],
              permission: 'pay',
            },
          },
          {
            path: 'review/panel2',
            name: 'ReviewPanel2',
            components: {
              ReviewPanel2: () => import('@/views/panel/review/ReviewPanel.vue'),
            },
            meta: {
              title: ['采购审批','财务部待审核'],
              permission: 'apply-unreview-2-get',
              level: 2,
            },
          },
          {
            path: 'review/history/2',
            name: 'ReviewHistory2',
            components: {
              ReviewHistory2: () => import('@/views/panel/review/ReviewHistory.vue'),
            },
            meta: {
              title: ['采购审批','财务部审核记录'],
              permission: 'apply-review-2-get',
              level: 2,
            },
          },
          //资产部路由/////////////////////////////////////////////////////////////////////
          {
            path: 'review/panel3',
            name: 'ReviewPanel3',
            components: {
              ReviewPanel3: () => import('@/views/panel/review/3/ReviewPanel3.vue'),
            },
            meta: {
              title: ['采购审批','资产部待审核'],
              permission: 'apply-unreview-3-get',
              level: 3,
            },
          },
          {
            path: 'review/history/3',
            name: 'ReviewHistory3',
            components: {
              ReviewHistory3: () => import('@/views/panel/review/ReviewHistory.vue'),
            },
            meta: {
              title: ['采购审批','资产部审核记录'],
              permission: 'apply-review-3-get',
              level: 3,
            },
          },
          {
            path: 'review/3',
            name: 'Review3',
            components: {
              Review3: () => import('@/views/panel/review/3/ReviewInfo3.vue'),
            },
            meta: {
              title: ['采购申请审核'],
              permission: '',
              level: 3,
            },
          },
          {
            path: 'comb/get',
            name: 'CombGet',
            components: {
              CombGet: () => import('@/views/panel/comb/CombPanel.vue'),
            },
            meta: {
              title: ['采购计划','查看采购组合'],
              permission: 'comb-get',
            },
          },
          {
            path: 'comb/add',
            name: 'CombAdd',
            components: {
              CombAdd: () => import('@/views/panel/comb/CombAddTab.vue'),
            },
            meta: {
              title: ['采购计划','添加组合'],
              permission: 'comb-add',
            },
          },
          {
            path: 'comb/update',
            name: 'CombUpdate',
            components: {
              CombUpdate: () => import('@/views/panel/comb/CombUpdate.vue'),
            },
            meta: {
              title: ['编辑采购组合'],
              permission: '',
            },
          },
          {
            path: 'apply/get/reviewd',
            name: 'ApplyGetReviewd',
            components: {
              ApplyGetReviewd: () => import('@/views/panel/apply/ApplyGetReviewd.vue'),
            },
            meta: {
              title: ['采购申请','查看所有申请'],
              permission: 'apply-get-reviewd',
            },
          },
            //资产部管理员路由
          {
            path: 'review/4',
            name: 'CombReviewPanel',
            components: {
              CombReviewPanel: () => import('@/views/panel/comb/review4/CombReviewPanel.vue'),
            },
            meta: {
              title: ['采购审批','资产部管理待审核'],
              permission: 'comb-unreview-get',
            },
          },
          {
            path: 'review/history/4',
            name: 'CombReviewHistory',
            components: {
              CombReviewHistory: () => import('@/views/panel/comb/review4/ReviewHistory4.vue'),
            },
            meta: {
              title: ['采购审批','资产部管理审核记录'],
              permission: 'comb-review-get',
              level: 4,
            },
          },
          {
            path: 'setting/deadline',
            name: 'SettingDeadline',
            components: {
              SettingDeadline: () => import('@/views/panel/setting/DeadlineSetting.vue'),
            },
            meta: {
              title: ['系统设置','设置申请截止时间'],
              permission: 'setting-deadline',
            },
          },
            //资产部操作员路由
          {
            path: 'comb/get/undeal',
            name: 'CombUndealPanel',
            components: {
              CombUndealPanel: () => import('@/views/panel/confirm/ConfirmPanel.vue'),
            },
            meta: {
              title: ['采购计划','待申领组合'],
              permission: 'comb-undeal-get',
            },
          },
          {
            path: 'purchase/get',
            name: 'PurchasePanel',
            components: {
              PurchasePanel: () => import('@/views/panel/purchase/PurchasePanel.vue'),
            },
            meta: {
              title: ['采购计划','采购中'],
              permission: 'purchase-get',
            },
          },
          {
            path: 'purchase/process',
            name: 'PurchaseProcess',
            components: {
              PurchaseProcess: () => import('@/views/panel/purchase/process/ProcessParent.vue'),
            },
            meta: {
              title: ['采购'],
              permission: '',
            },
          },
            //查看供应商路由
          {
            path: 'supplier/get',
            name: 'SupplierPanel',
            components: {
              SupplierPanel: () => import('@/views/panel/supplier/SupplierPanel.vue'),
            },
            meta: {
              title: ['供应商','查看供应商'],
              permission: 'supplier-get',
            },
          },

            //供应商的路由
          {
            path: 'supplier/purchase/get',
            name: 'SupplierPurchasePanel',
            components: {
              SupplierPurchasePanel: () => import('@/views/panel/supplier/SupplierPurchasePanel.vue'),
            },
            meta: {
              title: ['供应商系统','采购订单处理'],
              permission: 'purchase-todo',
            },
          },
          {
            path: 'supplier/purchase/process',
            name: 'SupplierPurchaseProcess',
            components: {
              SupplierPurchaseProcess: () => import('@/views/panel/supplier/SupplierProcessParent.vue'),
            },
            meta: {
              title: ['供应商系统','处理采购单'],
              permission: '',
            },
          },
          {
            path: 'supplier/inquiry/panel',
            name: 'SupplierInquiryPanel',
            components: {
              SupplierInquiryPanel: () => import('@/views/panel/supplier/SupplierInquiryPanel.vue'),
            },
            meta: {
              title: ['供应商系统','处理询价'],
              permission: 'purchase-inquiry',
            },
          },

            //统计路由
          {
            path: 'statistics/pay',
            name: 'StatisticsPay',
            components: {
              StatisticsPay: () => import('@/views/panel/statistics/StatisticsPay.vue'),
            },
            meta: {
              title: ['数据统计','支出统计'],
              permission: 'statistics-pay',
            },
          },
          //系统管理路由  /////////////////////////////////////////////////////////////////////
          // {
          //   path: 'systemmanage',
          //   name: 'SystemManage',
          //   redirect: {
          //     name: 'DepartmentGet',
          //   },
          //   meta: {
          //     title: '系统管理',
          //     permission: '',
          //   },
          //   components: {
          //     DepartmentGet: () => import('@/views/account/Login.vue'),
          //   },
          //   children: [
          //     {
          //       path: 'department',
          //       name: 'DepartmentManage',
          //       components: {
          //         DepartmentManage: () => import('@/views/systemmanage/department/DepartmentGet.vue'),
          //       },
          //       meta: {
          //         title: '部门管理',
          //         permission: 'department-manage',
          //       },
          //     },
          //   ]
          // },
          {
            path: 'systemmanage/department',
            name: 'DepartmentManage',
            components: {
              DepartmentManage: () => import('@/views/systemmanage/department/DepartmentGet.vue'),
            },
            meta: {
              title: ['系统管理','部门管理'],
              permission: 'department-manage',
            },
          },
          {
            path: 'systemmanage/user',
            name: 'UserManage',
            components: {
              UserManage: () => import('@/views/systemmanage/user/UserManage.vue'),
            },
            meta: {
              title: ['系统管理','用户管理'],
              permission: 'user-manage',
            },
          },
          {
            path: 'systemmanage/role',
            name: 'RoleManage',
            components: {
              RoleManage: () => import('@/views/systemmanage/role/RoleManage.vue'),
            },
            meta: {
              title: ['系统管理','角色管理'],
              permission: 'role-manage',
            },
          }
        ]
      },
      {
        name: 'AccountSetting',
        path: '/account/setting',
        component: () => import('@/views/account/AccountSetting.vue'),
        meta: {
          title: '',
          permission: '',
        },
      },
      {
        // name: 'AccountSetting',
        path: '/account/supplier/setting',
        component: () => import('@/views/account/SupplierAccountSetting.vue'),
        meta: {
          title: '',
          permission: '',
        },
      }
      //不需要经过tabs的路由
    ]
  },
  {
    path: '/public/user/register',
    name: 'userRegister',
    component: () => import('@/views/account/Register.vue'),
    meta: {
      title: '用户注册',
      permission: '',
    },
  },
  {
    path: '/public/user/login',
    name: 'userLogin',
    component: () => import('@/views/account/Login.vue'),
    meta: {
      title: '用户登录',
      permission: '',
    },
  },
  {
    path: '/public/supplier/register',
    name: 'supplierRegister',
    component: () => import('@/views/account/SupplierRegister.vue'),
    meta: {
      title: '供应商注册',
      permission: '',
    },
  },
  {
    path: '/public/supplier/login',
    name: 'supplierLogin',
    component: () => import('@/views/account/SupplierLogin.vue'),
    meta: {
      title: '供应商登录',
      permission: '',
    },
  },
]


const router = createRouter({
  history: createWebHistory(),//createWebHashHistory(),
  routes
})
router.afterEach(function (to) {
  // document.title = to.meta.tip as string;
  document.title = to.meta.title as string;
})
export default router
