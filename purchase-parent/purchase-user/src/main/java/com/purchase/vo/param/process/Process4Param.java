package com.purchase.vo.param.process;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class Process4Param {
    private Long purchaseId;
    private MultipartFile[] files;
}
