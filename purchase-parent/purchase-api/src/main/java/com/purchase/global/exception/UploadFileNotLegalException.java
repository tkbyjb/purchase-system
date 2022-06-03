package com.purchase.global.exception;

/**
 * 上传的文件不合法异常类
 */
public class UploadFileNotLegalException extends Exception{
    public UploadFileNotLegalException(){}

    public UploadFileNotLegalException(String mess){
        super(mess);
    }
}
