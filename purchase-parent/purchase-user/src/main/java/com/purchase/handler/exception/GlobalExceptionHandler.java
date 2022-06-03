package com.purchase.handler.exception;


import cn.dev33.satoken.exception.NotPermissionException;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.global.exception.NoBalanceException;
import com.purchase.global.exception.ParamIllegalException;
import com.purchase.global.exception.UploadFileNotLegalException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.IOException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public RespResult handleException(Exception e) {
        /**
         *全局处理服务器异常导致的未知错误
         */
        return new RespResult(ResultState.SERVER_ERROR);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RespResult handleMissingParamException(MissingServletRequestParameterException e) {
        /**
         *  controller缺少参数
         */
        return new RespResult(ResultState.PARMA_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public RespResult handleViolationException(ConstraintViolationException e){
        /**
         *  单个参数校验失败
         */
        return new RespResult(ResultState.PARMA_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public RespResult handleBindException(BindException e) {
        /**
         *  get请求对象参数校验失败
         */
        return new RespResult(ResultState.PARMA_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RespResult handlePostParamException(MethodArgumentNotValidException e){
        /**
         *  post请求对象参数校验失败
         */
        return new RespResult(ResultState.PARMA_ERROR);
    }

    @ExceptionHandler(UploadFileNotLegalException.class)
    public RespResult handleFileLegalException(MethodArgumentNotValidException e){
        /**
         *  上传恶意文件异常
         */
        return new RespResult(ResultState.UPLOAD_FILE_NOT_LEGAL);
    }


    @ExceptionHandler(IOException.class)
    public RespResult handleIOException(IOException e){
        /**
         *  文件上传保存失败异常
         */
        return new RespResult(ResultState.SERVER_ERROR);
    }

    @ExceptionHandler(NotPermissionException.class)
    public RespResult handlePermissionException(NotPermissionException e){
        /**
         *  没有权限访问此api
         */
        return new RespResult(ResultState.NO_PERMISSION);
    }

    @ExceptionHandler(CusNoPermissionException.class)
    public RespResult handleCusNoPermissionExceptionException(CusNoPermissionException e){
        /**
         *  没有权限访问此api,比如删除等,自定义的
         */
        return new RespResult(ResultState.NO_PERMISSION);
    }

    @ExceptionHandler(ParamIllegalException.class)
    public RespResult handleParamIllegalException(ParamIllegalException e){
        /**
         *  请求参数不合法,是业务处理时的不合法(比如id不存在等,是没法在param那儿校验的
         */
        return new RespResult(ResultState.PARMA_ERROR);
    }

    @ExceptionHandler(NoBalanceException.class)
    public RespResult handleNoBalanceException(NoBalanceException e){
        /**
         *  请求参数不合法,是业务处理时的不合法(比如id不存在等,是没法在param那儿校验的
         */
        return new RespResult(ResultState.NO_BALANCE);
    }
}
