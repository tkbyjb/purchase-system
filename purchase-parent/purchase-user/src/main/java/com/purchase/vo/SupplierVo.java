package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SupplierVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long supplierId;
    private String suppliername;
    private String tel;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    private String introduce;
    private Integer agreement;
    private Integer state;
}
