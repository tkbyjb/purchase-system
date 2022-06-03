package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplyVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long applyId;
    private String serialNumber;
    private String applyname;
    private String applyUsername;
    private String applyDepartmentname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime applyTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastUpdateTime;
    private String note;
    private String attachment;
    private Integer state;
    private Integer putoff;
}
