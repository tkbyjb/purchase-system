package com.purchase.global.exception;

public class NoBalanceException extends Exception{
    public NoBalanceException(){}

    public NoBalanceException(String mess){
        super(mess);
    }
}