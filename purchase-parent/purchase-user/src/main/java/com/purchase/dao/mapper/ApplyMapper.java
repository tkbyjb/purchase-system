package com.purchase.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.ApplyBo;
import com.purchase.dao.po.Apply;
import com.purchase.vo.param.ApplyGetParam;
import com.purchase.vo.param.ApplyGetUnreviewParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ApplyMapper extends BaseMapper<Apply> {
    Page<ApplyBo> getApplyBo(Page<ApplyBo> page, @Param("applyGetParam") ApplyGetParam applyGetParam);
    Integer updateState(Integer state, Long applyId);
    Integer updateStateApplyTime(Integer state, Long applyId, Long assignUserId);
//    查询某状态申请，用于查询各层未审核申请
    Page<ApplyBo> getApplyBoByUnreview(Page<ApplyBo> page, @Param("param") ApplyGetUnreviewParam param,
                                       @Param("state") Integer state, @Param("userId") Long userId);
    ApplyBo getApplyBoByApplyId(Long applyId);
    Integer updateCurrentAssignUserId(Long applyId, Long assignUserId);
    ApplyBo getApplyBoByApplyIdWithDeleted(Long applyId);
    Page<ApplyBo> getReviewdApplyBo(Page<ApplyBo> page, @Param("applyGetParam") ApplyGetParam applyGetParam);
}
