package com.purchase.vo.statistics.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseParam {
    private String startTime;
    private String endTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long departmentId;
    private String step = "day";//默认按天
}
