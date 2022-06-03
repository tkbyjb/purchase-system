package com.purchase.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.ReviewBo;
import com.purchase.dao.po.Review;
import com.purchase.vo.param.ReviewHistoryGetParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
    Review getNewestReviewByApplyId(Long applyId);
    Page<ReviewBo> getReviewBosByHistoryParam(Page<ReviewBo> page, @Param("param") ReviewHistoryGetParam reviewHistoryGetParam, @Param("userId") Long userId);
    Integer updateReviewState(Long reviewId, Integer state);

}
