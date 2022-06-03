package com.purchase.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用于审核获取之前审核信息的数据
 */
@Data
public class ReviewUnreviewVo {
    private String reviewUserRealname;//审核用户的真名
    private Integer reviewType;
    private Integer result;
    private String opinion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
