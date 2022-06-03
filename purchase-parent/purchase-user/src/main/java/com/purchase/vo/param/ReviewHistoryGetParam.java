package com.purchase.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.purchase.global.OrderBy;
import com.purchase.global.PageParam;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewHistoryGetParam {
    private PageParam pageParam;
    private List<OrderBy> orderBys;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startCreateTime = null;//开始审核时间
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String endCreateTime = null;//结束审核时间
    private Integer result;//是否通过
//    private String serialNumber;//申请编号
    private Integer reviewType;//审核层级
    private Integer state;
}
