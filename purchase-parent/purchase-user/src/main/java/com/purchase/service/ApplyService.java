package com.purchase.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.ApplyBo;
import com.purchase.dao.po.User;
import com.purchase.global.PageParam;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.global.exception.UploadFileNotLegalException;
import com.purchase.vo.ApplyListVo;
import com.purchase.vo.ApplyUnreviewListVo;
import com.purchase.vo.ApplyVo;
import com.purchase.vo.ReviewInfoVo;
import com.purchase.vo.param.ApplyAddParam;
import com.purchase.vo.param.ApplyGetParam;
import com.purchase.vo.param.ApplyGetUnreviewParam;
import com.purchase.vo.param.ApplyUpdateParam;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;

public interface ApplyService {
    Boolean addApply(ApplyAddParam applyAddParam, User user) throws UploadFileNotLegalException, IOException;
    Page<ApplyBo> getApplyBo(ApplyGetParam applyGetParam);
    ApplyListVo getApplyVo(ApplyGetParam applyGetParam);
    Boolean deletes(List<Long> applyIds) throws CusNoPermissionException;
    Integer updateApplyPart(ApplyUpdateParam applyUpdateParam, User user) throws UploadFileNotLegalException, IOException, CusNoPermissionException;
    Integer updateState(Integer state, Long applyId);
    Boolean updateToReview(Long applyId, Long assignUserId) throws CusNoPermissionException;
    Page<ApplyBo> getApplyBoByUnreview(ApplyGetUnreviewParam param, Integer state, Long userId);
    ApplyUnreviewListVo getApplyUnreviewListVo(ApplyGetUnreviewParam param, Integer state, Long userId);
    ReviewInfoVo getReviewInfoVo(Long applyId, Integer state) throws CusNoPermissionException;
    ApplyListVo getReviewdApplyVo(ApplyGetParam applyGetParam);
}
