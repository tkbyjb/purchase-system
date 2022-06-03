package com.purchase.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.purchase.global.OrderBy;
import com.purchase.global.PageParam;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApplyGetParam {
    private PageParam pageParam = null;
    private List<OrderBy> orderBys = null;
    private String serialNumber = null;
    private String applyname = null;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startCreateTime = null;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String endCreateTime = null;
    private String note = null;
    private Integer state = 0;
    private Integer putoff;
    private Long applyUserId;
}
