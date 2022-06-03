package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 查询未审核的申请返回的实体类
 */
@Data
public class ApplyUnreviewVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long applyId;
    private String serialNumber;
    private String applyname;
    private String applyUsername;
    private String applyDepartmentname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime applyTime;
    private String note;
    private String attachment;
    private Integer state;
    private Integer putoff;
}
