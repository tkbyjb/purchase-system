package com.purchase.service;

import com.purchase.dao.po.Review;
import com.purchase.vo.ReviewHistoryListVo;
import com.purchase.vo.ReviewHistoryVo;
import com.purchase.vo.ReviewUnreviewVo;
import com.purchase.vo.param.Review3Param;
import com.purchase.vo.param.ReviewCombParam;
import com.purchase.vo.param.ReviewHistoryGetParam;
import com.purchase.vo.param.ReviewParam;

import java.util.List;

public interface ReviewService {
    Integer deletesByApplyId(Long applyId);
    Boolean addReview(ReviewParam reviewParam, Long userId, Integer state, Integer level);
    ReviewHistoryListVo getReviewHistoryListVo(ReviewHistoryGetParam reviewHistoryGetParam, Long userId);
    String getReturnOpinion(Long applyId);
    Boolean addReview3(Review3Param review3Param, Long userId, Integer state);
    Boolean addReview4(ReviewCombParam reviewParam, Long userId, Integer state);
    Integer setReviewStateInvalid(Long applyId);
    ReviewHistoryListVo getCombReviewHistoryListVo(ReviewHistoryGetParam reviewHistoryGetParam, Long userId);
}
