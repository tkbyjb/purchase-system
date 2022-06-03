//和state相关的数据匹配
interface AState {
    value: number;
    mess: string; //state对应的描述
    tagColor: string;//state对应的标签颜色
}
//申请状态state
const applyStateMap = new Map<number, AState>()
applyStateMap.set(1, {value: 1, mess: '未提交审核', tagColor: 'pink',} as AState)
applyStateMap.set(2, {value: 2, mess: '待部门管理审核', tagColor: 'orange',} as AState)
applyStateMap.set(3, {value: 3, mess: '待财务部审核', tagColor: 'orange',} as AState)
applyStateMap.set(4, {value: 4, mess: '待资产部审核', tagColor: 'orange',} as AState)
applyStateMap.set(5, {value: 5, mess: '明细购买中', tagColor: 'orange',} as AState)
applyStateMap.set(6, {value: 6, mess: '全部明细购买完成', tagColor: 'cyan',} as AState)
applyStateMap.set(20, {value: 20, mess: '审核未通过', tagColor: 'red',} as AState)
applyStateMap.set(21, {value: 21, mess: '已被取消', tagColor: 'blue',} as AState)
applyStateMap.set(30, {value: 30, mess: '待下一年购买', tagColor: 'green',} as AState)
const applyCanEditStateList = [1, 20, 21]; //能被编辑的state范围

//明细状态state
const detailStateMap = new Map<number, AState>()
detailStateMap.set(1, {value: 1, mess: '待申请审核通过', tagColor: 'cyan',} as AState)
detailStateMap.set(2, {value: 2, mess: '待购买', tagColor: 'blue',} as AState)
detailStateMap.set(3, {value: 3, mess: '购买中', tagColor: 'orange',} as AState)
detailStateMap.set(4, {value: 4, mess: '购买完成', tagColor: 'green',} as AState)
detailStateMap.set(5, {value: 5, mess: '已被取消', tagColor: 'red',} as AState)
const detailCanEditStateList = [1,5];

//审核层级reviewType
const reviewTypeStateMap = new Map<number, AState>()
reviewTypeStateMap.set(1, {value: 1, mess: '部门管理审核', tagColor: 'cyan',} as AState)
reviewTypeStateMap.set(2, {value: 2, mess: '财务部审核', tagColor: 'cyan',} as AState)
reviewTypeStateMap.set(3, {value: 3, mess: '资产部审核', tagColor: 'cyan',} as AState)
reviewTypeStateMap.set(4, {value: 4, mess: '资产部管理员审核', tagColor: 'cyan',} as AState)

//用户状态state
const userStateMap = new Map<number, AState>()
userStateMap.set(1, {value: 1, mess: '待审核', tagColor: 'orange',} as AState)
userStateMap.set(2, {value: 2, mess: '正常', tagColor: 'green',} as AState)
userStateMap.set(3, {value: 3, mess: '审核未通过', tagColor: 'cyan',} as AState)
userStateMap.set(4, {value: 4, mess: '未分配角色', tagColor: 'red',} as AState)
const userCanEditStateList = [2,4]

//组合状态state
const combStateMap = new Map<number, AState>()
combStateMap.set(1, {value: 1, mess: '未提交审核', tagColor: 'blue',} as AState)
combStateMap.set(2, {value: 2, mess: '待管理员审核', tagColor: 'orange',} as AState)
combStateMap.set(3, {value: 3, mess: '待操作员未申领', tagColor: 'cyan',} as AState)
combStateMap.set(4, {value: 4, mess: '已申领', tagColor: 'green',} as AState)
combStateMap.set(10, {value: 10, mess: '审核未通过', tagColor: 'red',} as AState)
combStateMap.set(11, {value: 11, mess: '已被取消', tagColor: 'pink',} as AState)
const combCanEditStateList = [1, 10, 11];

//供应商state
const supplierStateMap = new Map<number, AState>()
supplierStateMap.set(1, {value: 1, mess: '待审核', tagColor: 'orange',} as AState)
supplierStateMap.set(2, {value: 2, mess: '正常', tagColor: 'green',} as AState)
supplierStateMap.set(3, {value: 3, mess: '审核未通过', tagColor: 'cyan',} as AState)
const supplierCanEditStateList = [2];

//采购方式
const purchaseWayStateMap = new Map<number, AState>()
purchaseWayStateMap.set(0, {value: 0, mess: '协议采购', tagColor: 'orange',} as AState)
purchaseWayStateMap.set(1, {value: 1, mess: '其他采购', tagColor: 'blue',} as AState)

//采购state
const purchaseStateMap = new Map<number, AState>()
purchaseStateMap.set(1, {value: 1, mess: '待选择供应商', tagColor: 'orange',} as AState)
purchaseStateMap.set(2, {value: 2, mess: '待供应商给出价格', tagColor: 'orange',} as AState)
purchaseStateMap.set(3, {value: 3, mess: '待操作员确认价格', tagColor: 'orange',} as AState)
purchaseStateMap.set(4, {value: 4, mess: '待供应商给出合同', tagColor: 'orange',} as AState)
purchaseStateMap.set(5, {value: 5, mess: '待学校签署合同', tagColor: 'orange',} as AState)
purchaseStateMap.set(6, {value: 6, mess: '待发货', tagColor: 'orange',} as AState)
purchaseStateMap.set(7, {value: 7, mess: '待签收', tagColor: 'orange',} as AState)
purchaseStateMap.set(8, {value: 8, mess: '待支付', tagColor: 'orange',} as AState)
purchaseStateMap.set(9, {value: 9, mess: '采购完成', tagColor: 'green',} as AState)
purchaseStateMap.set(10, {value: 10, mess: '采购完成', tagColor: 'orange',} as AState)
purchaseStateMap.set(20, {value: 20, mess: '已被取消', tagColor: 'orange',} as AState)

//采购state对应的步骤step,操作员的
const purchaseStateToStepMap = new Map<string, number>()
purchaseStateToStepMap.set('1',0)
purchaseStateToStepMap.set('3',1)
purchaseStateToStepMap.set('5',2)
purchaseStateToStepMap.set('7',3)
purchaseStateToStepMap.set('8',4)
purchaseStateToStepMap.set('9',5)
//采购state对应的步骤step,供应商的
const supplierStateToStepMap = new Map<string, number>()
supplierStateToStepMap.set('2',0)
supplierStateToStepMap.set('4',1)
supplierStateToStepMap.set('6',2)
supplierStateToStepMap.set('9',3)

//询价state
const inquiryStateMap = new Map<number, AState>()
inquiryStateMap.set(0, {value: 0, mess: '未给出报价', tagColor: 'orange',} as AState)
inquiryStateMap.set(1, {value: 1, mess: '已给出报价', tagColor: 'green',} as AState)
inquiryStateMap.set(2, {value: 2, mess: '拒绝', tagColor: 'blue',} as AState)

export {
    applyStateMap,
    applyCanEditStateList,
    detailStateMap,
    userStateMap,
    combStateMap,
    combCanEditStateList,
    detailCanEditStateList,
    userCanEditStateList,
    reviewTypeStateMap,
    supplierStateMap,
    supplierCanEditStateList,
    purchaseWayStateMap,
    purchaseStateMap,
    purchaseStateToStepMap,
    supplierStateToStepMap,
    inquiryStateMap,
}
