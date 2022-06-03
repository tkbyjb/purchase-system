package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.ReviewBo;
import com.purchase.dao.mapper.ApplyMapper;
import com.purchase.dao.mapper.CombMapper;
import com.purchase.dao.mapper.DetailMapper;
import com.purchase.dao.mapper.ReviewMapper;
import com.purchase.dao.po.Apply;
import com.purchase.dao.po.Comb;
import com.purchase.dao.po.Detail;
import com.purchase.dao.po.Review;
import com.purchase.service.ApplyService;
import com.purchase.service.ReviewService;
import com.purchase.vo.ReviewHistoryListVo;
import com.purchase.vo.ReviewHistoryVo;
import com.purchase.vo.ReviewUnreviewVo;
import com.purchase.vo.param.Review3Param;
import com.purchase.vo.param.ReviewCombParam;
import com.purchase.vo.param.ReviewHistoryGetParam;
import com.purchase.vo.param.ReviewParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Resource
    private ReviewMapper reviewMapper;
    @Resource
    private ApplyService applyService;
    @Resource
    private ApplyMapper applyMapper;
    @Resource
    private DetailMapper detailMapper;
    @Resource
    private CombMapper combMapper;

    @Override
    public Integer deletesByApplyId(Long applyId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("applyId", applyId);
        return reviewMapper.delete(queryWrapper);
    }

    @Override
    public ReviewHistoryListVo getReviewHistoryListVo(ReviewHistoryGetParam reviewHistoryGetParam, Long userId) {
        Page<ReviewBo> page = new Page<>(reviewHistoryGetParam.getPageParam().getPageIndex(), reviewHistoryGetParam.getPageParam().getPageSize());
        reviewMapper.getReviewBosByHistoryParam(page, reviewHistoryGetParam, userId);

        ReviewHistoryListVo reviewHistoryListVo = new ReviewHistoryListVo();
        reviewHistoryListVo.setTotal((int)page.getTotal());
        List<ReviewHistoryVo> reviewHistoryVos = new ArrayList<>();
        for (ReviewBo record : page.getRecords()) {
            ReviewHistoryVo reviewHistoryVo = new ReviewHistoryVo();
            reviewHistoryVo.setSerialNumber(record.getReviewApplyBo().getApply().getSerialNumber());
            reviewHistoryVo.setApplyname(record.getReviewApplyBo().getApply().getApplyname());
            reviewHistoryVo.setApplyUsername(record.getReviewApplyBo().getApplyUser().getRealname());
            reviewHistoryVo.setApplyDepartmentname(record.getReviewApplyBo().getApplyDepartment().getDepartmentname());
            reviewHistoryVo.setReviewUserRealname(record.getReviewUser().getRealname());
            BeanUtils.copyProperties(record.getReview(),reviewHistoryVo);
            reviewHistoryVos.add(reviewHistoryVo);
        }
        reviewHistoryListVo.setReviewHistoryVos(reviewHistoryVos);
        return reviewHistoryListVo;
    }

    @Override
    public String getReturnOpinion(Long applyId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("applyId", applyId);
        Apply apply = applyMapper.selectOne(queryWrapper);
        if(apply == null || apply.getState() != 20) {//applyId不存在||不是被打回状态返回空
            return null;
        }
//        queryWrapper.eq("state", 1);
        queryWrapper.orderByDesc("createTime");
        List<Review> reviews = reviewMapper.selectList(queryWrapper);
        return reviews.get(0).getOpinion();
    }

    @Override
    @Transactional
    public Boolean addReview(ReviewParam reviewParam, Long userId, Integer state, Integer level) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("applyId", reviewParam.getApplyId());
        queryWrapper.eq("reviewType", level);
        queryWrapper.eq("state", 1);
        List<Review> reviews = reviewMapper.selectList(queryWrapper);//其实同层审核state为level的肯定同时只有一个,无效的倒是可以多个
        if(reviews != null && reviews.size() > 0){//已经审核过了
            return false;
        }
        if(reviewParam.getResult() == 0) {//如果是打回,改之前state为1的审核state为0
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("applyId", reviewParam.getApplyId());
            updateWrapper.eq("state", 1);
            updateWrapper.set("state", 0);
            reviewMapper.update(null, updateWrapper);
        }
        Review reviewAdd = new Review();
        reviewAdd.setUserId(userId);
        reviewAdd.setApplyId(reviewParam.getApplyId());
        reviewAdd.setResult(reviewParam.getResult());
        reviewAdd.setOpinion(reviewParam.getOpinion());
        reviewAdd.setCreateTime(LocalDateTime.now());
        reviewAdd.setIsDelete(0);
        if(reviewParam.getResult() == 0) {
            reviewAdd.setState(0);
        }else{
            reviewAdd.setState(1);
        }
        reviewAdd.setReviewType(level);
        reviewMapper.insert(reviewAdd);

        Apply apply = new Apply();
        apply.setApplyId(reviewParam.getApplyId());
        apply.setState(state);
        if(reviewParam.getResult()==1){//如果通过,修改下一个审核人id
            apply.setCurrentAssignUserId(reviewParam.getAssignUserId());
        }
        applyMapper.updateById(apply);
        return true;
    }

    @Override
    @Transactional
    public Boolean addReview3(Review3Param reviewParam, Long userId, Integer state) {
        ReviewParam param = new ReviewParam();
        BeanUtils.copyProperties(reviewParam, param);
        if(!addReview(param, userId, state, 3)){
            return false;
        }
        //修改明细state为购买中,并修改明细采购方式
        if(reviewParam.getResult() == 1){
            if(reviewParam.getPurchaseWay0().size() > 0){
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("applyId", reviewParam.getApplyId());
                updateWrapper.in("detailId", reviewParam.getPurchaseWay0());
                updateWrapper.set("state", 2);
                updateWrapper.set("purchaseWay", 0);
                detailMapper.update(null, updateWrapper);
            }
            if(reviewParam.getPurchaseWay1().size() > 0){
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("applyId", reviewParam.getApplyId());
                updateWrapper.in("detailId", reviewParam.getPurchaseWay1());
                updateWrapper.set("state", 2);
                updateWrapper.set("purchaseWay", 1);
                detailMapper.update(null, updateWrapper);
            }
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean addReview4(ReviewCombParam reviewParam, Long userId, Integer state) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("combId", reviewParam.getCombId());
        queryWrapper.eq("state", 1);
        List<Review> reviews = reviewMapper.selectList(queryWrapper);
        if(reviews == null && reviews.size() > 0){//已经审核过了
            return false;
        }
//        if(reviewParam.getResult() == 0) {//组合审核只有一层,不需要
//            for (Review review : reviews) {
//                reviewMapper.updateReviewState(review.getReviewId(), 0);
//            }
//        }
        Review reviewAdd = new Review();
        reviewAdd.setUserId(userId);
        reviewAdd.setCombId(reviewParam.getCombId());
        reviewAdd.setResult(reviewParam.getResult());
        reviewAdd.setOpinion(reviewParam.getOpinion());
        reviewAdd.setCreateTime(LocalDateTime.now());
        reviewAdd.setIsDelete(0);
        if(reviewParam.getResult() == 0){
            reviewAdd.setState(0);
        }else{
            reviewAdd.setState(1);
        }
        reviewAdd.setReviewType(4);
        reviewMapper.insert(reviewAdd);

        Comb comb = new Comb();
        comb.setCombId(reviewParam.getCombId());
        comb.setState(state);
        if(reviewParam.getResult()==1){
           comb.setConfirmUserId(reviewParam.getConfirmUserId());
        }
        combMapper.updateById(comb);
        return true;
    }

    @Override
    public Integer setReviewStateInvalid(Long applyId) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("applyId", applyId);
        updateWrapper.set("state", 0);
        return reviewMapper.update(null, updateWrapper);
    }

    @Override
    public ReviewHistoryListVo getCombReviewHistoryListVo(ReviewHistoryGetParam reviewHistoryGetParam, Long userId) {
        Page<ReviewBo> page = new Page<>(reviewHistoryGetParam.getPageParam().getPageIndex(), reviewHistoryGetParam.getPageParam().getPageSize());
        reviewMapper.getReviewBosByHistoryParam(page, reviewHistoryGetParam, userId);

        ReviewHistoryListVo reviewHistoryListVo = new ReviewHistoryListVo();
        reviewHistoryListVo.setTotal((int)page.getTotal());
        List<ReviewHistoryVo> reviewHistoryVos = new ArrayList<>();
        for (ReviewBo record : page.getRecords()) {
            ReviewHistoryVo reviewHistoryVo = new ReviewHistoryVo();
            reviewHistoryVo.setSerialNumber(record.getReviewCombBo().getComb().getSerialNumber());
            reviewHistoryVo.setApplyname(record.getReviewCombBo().getComb().getCombname());
            reviewHistoryVo.setApplyUsername(record.getReviewCombBo().getCreateUser().getRealname());
            reviewHistoryVo.setReviewUserRealname(record.getReviewUser().getRealname());
            BeanUtils.copyProperties(record.getReview(),reviewHistoryVo);
            reviewHistoryVos.add(reviewHistoryVo);
        }
        reviewHistoryListVo.setReviewHistoryVos(reviewHistoryVos);
        return reviewHistoryListVo;
    }

}

