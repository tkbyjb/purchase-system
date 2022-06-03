package com.purchase.vo.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;

@Data
public class ApplyUpdateParam {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long applyId;
    @Pattern(regexp = "[\\s\\S\\u4e00-\\u9fa5]{2,128}")
    private String applyname;
    private String attachment = null;
    private String note = null;
    private MultipartFile[] files = null;
}
