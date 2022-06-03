package com.purchase.global;

import lombok.Data;

@Data
public class RespResult {
    private Integer state;  //自定义的状态码
    private String mess = null; //状态码对应的提示信息
    private Object data = null;  //响应的数据对象

    public RespResult(ResultState resultState, Object object) {
        //使用系统固定mess+返回数据
        this.state = resultState.getState();
        this.mess = resultState.getMess();
        this.data = object;
    }

    public RespResult(ResultState resultState) {
        //使用系统固定mess+不返回数据
        this.state = resultState.getState();
        this.mess = resultState.getMess();
        this.data = null;
    }

    public RespResult(ResultState resultState, String mess, Object object) {
        //自定义mess+返回/不返回数据
        this.state = resultState.getState();
        this.mess = mess;
        this.data = object;
    }
}
