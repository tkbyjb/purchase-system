package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CombVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long combId;
    private String serialNumber;
    private String combname;
    private String note;
    private Integer state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    private String creatUsername;
    private Integer purchaseWay;
    private String confirmUsername;//负责采购的人,管理员审核后会指定
}
