package com.purchase.service;

import com.purchase.global.exception.NoBalanceException;
import com.purchase.global.exception.ParamIllegalException;
import com.purchase.global.exception.UploadFileNotLegalException;
import com.purchase.vo.param.process.Process1Param;
import com.purchase.vo.param.process.Process2Param;
import com.purchase.vo.param.process.Process3Param;
import com.purchase.vo.param.process.Process4Param;

import java.io.IOException;

public interface ProcessService {
    Integer process1(Process1Param param) throws ParamIllegalException;
    Integer process2(Process2Param param) throws ParamIllegalException;
    Integer process3(Process3Param param) throws ParamIllegalException;
    Integer process4(Process4Param param) throws UploadFileNotLegalException, IOException;
    Integer process5(Process4Param param) throws UploadFileNotLegalException, IOException;
    Integer process6(Long purchaseId);
    Integer process7(Long purchaseId, Long payUserId);
    Integer process8(Long purchaseId, Long userId) throws NoBalanceException;
}
