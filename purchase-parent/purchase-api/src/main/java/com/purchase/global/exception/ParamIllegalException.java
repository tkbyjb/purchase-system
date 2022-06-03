package com.purchase.global.exception;

public class ParamIllegalException extends Exception{
    public ParamIllegalException(){}

    public ParamIllegalException(String mess){
        super(mess);
    }
}
