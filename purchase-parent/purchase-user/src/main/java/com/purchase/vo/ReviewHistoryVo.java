package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审核历史记录数据
 */
@Data
public class ReviewHistoryVo {
    private String serialNumber;
    private String applyname;
    private String applyUsername;
    private String applyDepartmentname;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private LocalDateTime applyTime;

    private String reviewUserRealname;
    private Integer reviewType;
    private Integer result;
    private String opinion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
