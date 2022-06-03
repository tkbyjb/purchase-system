package com.purchase.global.exception;

/**
 * 自定义没有权限异常类,比如想删除不可编辑的申请等
 */
public class CusNoPermissionException extends Exception{
    public CusNoPermissionException(){}

    public CusNoPermissionException(String mess){
        super(mess);
    }
}
