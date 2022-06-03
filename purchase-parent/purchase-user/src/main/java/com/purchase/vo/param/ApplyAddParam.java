package com.purchase.vo.param;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;

@Data
public class ApplyAddParam {
    @Pattern(regexp = "[\\s\\S\\u4e00-\\u9fa5]{2,128}")
    private String applyname;
    private String note;
    private String attachment = null;
    private MultipartFile[] files = null;
}
