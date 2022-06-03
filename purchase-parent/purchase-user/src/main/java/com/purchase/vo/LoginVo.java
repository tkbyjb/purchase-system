package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class LoginVo {
    private String username;
    private String avatar;
    private String token;
    private List<MenuVo> menuVos;
    private Integer type;//0用户1供应商
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
}
