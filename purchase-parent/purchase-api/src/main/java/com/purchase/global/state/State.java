package com.purchase.global.state;

import java.util.List;

/**
 * 状态,主要是判断是否处于可编辑状态,方便改
 */
public class State {
    public static Integer[] APPLY_CAN_EDIT = {1,20,21};//申请可编辑的状态
    public static Integer[] USER_CAN_LOGIN = {2,4}; //用户可正常登录的状态
    public static Integer[] DETAIL_CAN_EDIT = {1,5};//明细可编辑状态
    public static Integer[] COMB_CAN_DEIT = {1,10,11};//组合可修改状态
    public static Integer[] SUPPLIER_CAN_LOGIN = {2};//供应商可登录的状态
//    public static Integer[] PURCHASE
}
