package com.purchase.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.ApplyBo;
import com.purchase.dao.mapper.ApplyMapper;
import com.purchase.dao.mapper.DetailMapper;
import com.purchase.dao.mapper.ReviewMapper;
import com.purchase.dao.mapper.UserMapper;
import com.purchase.dao.po.Apply;
import com.purchase.dao.po.Detail;
import com.purchase.dao.po.Review;
import com.purchase.dao.po.User;
import com.purchase.global.DefaultData;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.global.exception.UploadFileNotLegalException;
import com.purchase.global.state.State;
import com.purchase.service.*;
import com.purchase.util.SerialNumberGenerator;
import com.purchase.vo.*;
import com.purchase.vo.param.ApplyAddParam;
import com.purchase.vo.param.ApplyGetParam;
import com.purchase.vo.param.ApplyGetUnreviewParam;
import com.purchase.vo.param.ApplyUpdateParam;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ApplyServiceImpl implements ApplyService {
    @Resource
    private FileService fileService;
    @Resource
    private ApplyMapper applyMapper;
    @Resource
    private DefaultData defaultData;
    @Resource
    private DetailService detailService;
    @Resource
    private ReviewService reviewService;
    @Resource
    private ReviewMapper reviewMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private DetailMapper detailMapper;
    @Resource
    private SettingService settingService;

    @Override
    @Transactional
    public Boolean addApply(ApplyAddParam applyAddParam, User user) throws UploadFileNotLegalException, IOException {
        //暂时只能上传一个附件,后边再说
        String filename = null;
        if (applyAddParam.getFiles() != null && applyAddParam.getFiles().length > 0) {
            for (MultipartFile file : applyAddParam.getFiles()) {
                if (!fileService.isFileLegal(file)) {
                    throw new UploadFileNotLegalException();
                }
            }
            filename = fileService.saveFile(applyAddParam.getFiles()[0], defaultData.USER_UPLOAD_BASE_PATH + "/" + user.getUserId() + "/apply");
        }
        Apply apply = new Apply();
        apply.setApplyname(applyAddParam.getApplyname());
        apply.setNote(applyAddParam.getNote());
        apply.setAttachment(filename);
        apply.setCreateTime(LocalDateTime.now());
        apply.setSerialNumber(SerialNumberGenerator.generator("AP"));
        apply.setApplyUserId(user.getUserId());
        apply.setApplyDepartmentId(user.getDepartmentId());
        apply.setLastUpdateTime(apply.getCreateTime());
        apply.setState(1);
        apply.setIsDelete(0);
        if(new Date().before(settingService.getDeadline())) {
            apply.setPutoff(0);
        }else{
            apply.setPutoff(1);
        }
        return applyMapper.insert(apply) < 1 ? false : true;
    }

    @Override
    public Page<ApplyBo> getApplyBo(ApplyGetParam applyGetParam) {
        Page<ApplyBo> page = new Page<>(applyGetParam.getPageParam().getPageIndex(), applyGetParam.getPageParam().getPageSize());
        return applyMapper.getApplyBo(page, applyGetParam);
    }

    @Override
    public ApplyListVo getApplyVo(ApplyGetParam applyGetParam) {
        Page<ApplyBo> page = getApplyBo(applyGetParam);
        List<ApplyVo> applyVos = new ArrayList<>();
        page.getRecords().forEach(applyBo -> {
            ApplyVo applyVo = new ApplyVo();
            BeanUtils.copyProperties(applyBo.getApply(), applyVo);
            applyVo.setApplyUsername(applyBo.getApplyUser().getRealname());
            applyVo.setApplyDepartmentname(applyBo.getApplyDepartment().getDepartmentname());

            //附件名
            if(applyBo.getApply().getAttachment() != null && !applyBo.getApply().getAttachment().equals("")) {
                String fileUrl = defaultData.ATTACHMENT_ACCESS_BASE_URL+"/"+applyBo.getApply().getApplyUserId()+"/apply/"+applyBo.getApply().getAttachment();
                applyVo.setAttachment(fileUrl);
            }else{
                applyVo.setAttachment(null);
            }


            applyVos.add(applyVo);
        });
        ApplyListVo applyListVo = new ApplyListVo();
        applyListVo.setApplyVos(applyVos);
        applyListVo.setTotal((int) page.getTotal());
        return applyListVo;
    }

    @Override
    @Transactional
    public Boolean deletes(List<Long> applyIds) throws CusNoPermissionException {
        //有一个不存在或不能删除就都不进行删除
        for (Long applyId : applyIds) {
            Apply apply = applyMapper.selectById(applyId);
            if (apply != null && Arrays.asList(State.APPLY_CAN_EDIT).contains(apply.getState())) {//存在且可编辑
                detailService.deleteByApplyId(applyId);
                reviewService.setReviewStateInvalid(applyId);
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("applyId", applyId);
                applyMapper.delete(queryWrapper);
            } else {
                throw new CusNoPermissionException();
            }
        }
        return true;
    }

    @Override
    @Transactional
    public Integer updateApplyPart(ApplyUpdateParam applyUpdateParam, User user) throws UploadFileNotLegalException, IOException, CusNoPermissionException {
        //修改申请名,备注,附件名,上次更新时间,是否推迟
        if (!Arrays.asList(State.APPLY_CAN_EDIT).contains(applyMapper.selectById(applyUpdateParam.getApplyId()).getState())) {
            throw new CusNoPermissionException();
        }
        String filename = null;
        if (applyUpdateParam.getFiles() != null && applyUpdateParam.getFiles().length > 0) {
            for (MultipartFile file : applyUpdateParam.getFiles()) {
                if (!fileService.isFileLegal(file)) {
                    throw new UploadFileNotLegalException();
                }
            }
            filename = fileService.saveFile(applyUpdateParam.getFiles()[0], defaultData.USER_UPLOAD_BASE_PATH + "/" + user.getUserId() + "/apply");
        }
        applyUpdateParam.setAttachment(filename);
        Apply apply = new Apply();
        BeanUtils.copyProperties(applyUpdateParam, apply);
        if(new Date().before(settingService.getDeadline())) {
            apply.setPutoff(0);
        }else{
            apply.setPutoff(1);
        }
        return applyMapper.updateById(apply);
    }

    @Override
    public Integer updateState(Integer state, Long applyId) {
        return applyMapper.updateState(state, applyId);
    }

    @Override
    public Boolean updateToReview(Long applyId, Long assignUserId) throws CusNoPermissionException {
        Apply apply = applyMapper.selectById(applyId);
        if (apply != null && Arrays.asList(State.APPLY_CAN_EDIT).contains(apply.getState())) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("applyId", applyId);
            List<Detail> details = detailMapper.selectList(queryWrapper);
            if (details.size() < 1) {//申请没有明细
                return false;
            }
            Apply apply1 = new Apply();
            apply1.setApplyId(applyId);
            apply1.setApplyTime(LocalDateTime.now());
            apply1.setState(2);
            apply1.setCurrentAssignUserId(assignUserId);
            if(new Date().before(settingService.getDeadline())) {
                apply1.setPutoff(0);
            }else{
                apply1.setPutoff(1);
            }
            applyMapper.updateById(apply1);
            return true;
        } else {
            throw new CusNoPermissionException();
        }
    }

    @Override
    public Page<ApplyBo> getApplyBoByUnreview(ApplyGetUnreviewParam param, Integer state, Long userId) {
        Page<ApplyBo> page = new Page<>(param.getPageParam().getPageIndex(), param.getPageParam().getPageSize());
        return applyMapper.getApplyBoByUnreview(page, param, state, userId);
    }

    @Override
    public ApplyUnreviewListVo getApplyUnreviewListVo(ApplyGetUnreviewParam param, Integer state, Long userId) {
        Page<ApplyBo> page = getApplyBoByUnreview(param, state, userId);
        ApplyUnreviewListVo applyUnreviewListVo = new ApplyUnreviewListVo();
        applyUnreviewListVo.setTotal((int) page.getTotal());
        applyUnreviewListVo.setApplyUnreviewVos(new ArrayList<>());
        for (ApplyBo record : page.getRecords()) {
            ApplyUnreviewVo applyUnreviewVo = new ApplyUnreviewVo();
            BeanUtils.copyProperties(record.getApply(), applyUnreviewVo);
            applyUnreviewVo.setApplyUsername(record.getApplyUser().getRealname());
            applyUnreviewVo.setApplyDepartmentname(record.getApplyDepartment().getDepartmentname());
            applyUnreviewListVo.getApplyUnreviewVos().add(applyUnreviewVo);
        }
        return applyUnreviewListVo;
    }

    @Override
    public ReviewInfoVo getReviewInfoVo(Long applyId, Integer state) throws CusNoPermissionException {
        ApplyBo applyBo = applyMapper.getApplyBoByApplyId(applyId);
        if (applyBo.getApply().getState() != state) {//检查applyId对应状态是否是state
            throw new CusNoPermissionException();
        }
        //获取申请信息
        ReviewInfoVo reviewInfoVo = new ReviewInfoVo();
        ApplyUnreviewVo applyUnreviewVo = new ApplyUnreviewVo();
        BeanUtils.copyProperties(applyBo.getApply(), applyUnreviewVo);
        applyUnreviewVo.setApplyUsername(applyBo.getApplyUser().getRealname());
        applyUnreviewVo.setApplyDepartmentname(applyBo.getApplyDepartment().getDepartmentname());

        //附件名
        if(applyBo.getApply().getAttachment() != null && !applyBo.getApply().getAttachment().equals("")) {
            String fileUrl = defaultData.ATTACHMENT_ACCESS_BASE_URL+"/"+applyBo.getApply().getApplyUserId()+"/apply/"+applyBo.getApply().getAttachment();
            applyUnreviewVo.setAttachment(fileUrl);
        }else {
            applyUnreviewVo.setAttachment(null);
        }


        reviewInfoVo.setApplyUnreviewVo(applyUnreviewVo);
        //获取明细信息
        List<DetailUnreviewVo> detailUnreviewVos = new ArrayList<>();
        detailService.getDetailVoByApplyId(applyId).forEach(detailVo -> {
            DetailUnreviewVo detailUnreviewVo = new DetailUnreviewVo();
            BeanUtils.copyProperties(detailVo, detailUnreviewVo);
            detailUnreviewVos.add(detailUnreviewVo);
        });
        reviewInfoVo.setDetailUnreviewVos(detailUnreviewVos);
        //获取审核信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("applyId", applyId);
        queryWrapper.eq("state", 1);
        List<Review> reviews = reviewMapper.selectList(queryWrapper);
        List<ReviewUnreviewVo> reviewUnreviewVos = new ArrayList<>();
        for (Review review : reviews) {
            ReviewUnreviewVo reviewUnreviewVo = new ReviewUnreviewVo();
            User user = userMapper.selectById(review.getUserId());
            reviewUnreviewVo.setReviewUserRealname(user.getRealname());
            BeanUtils.copyProperties(review, reviewUnreviewVo);
            reviewUnreviewVos.add(reviewUnreviewVo);
        }
        reviewInfoVo.setReviewUnreviewVos(reviewUnreviewVos);
        return reviewInfoVo;
    }

    @Override
    public ApplyListVo getReviewdApplyVo(ApplyGetParam applyGetParam) {
        Page<ApplyBo> page = new Page<>(applyGetParam.getPageParam().getPageIndex(), applyGetParam.getPageParam().getPageSize());
        applyMapper.getReviewdApplyBo(page, applyGetParam);
        List<ApplyVo> applyVos = new ArrayList<>();
        page.getRecords().forEach(applyBo -> {
            ApplyVo applyVo = new ApplyVo();
            BeanUtils.copyProperties(applyBo.getApply(), applyVo);
            applyVo.setApplyUsername(applyBo.getApplyUser().getRealname());
            applyVo.setApplyDepartmentname(applyBo.getApplyDepartment().getDepartmentname());
            applyVos.add(applyVo);
        });
        ApplyListVo applyListVo = new ApplyListVo();
        applyListVo.setApplyVos(applyVos);
        applyListVo.setTotal((int) page.getTotal());
        return applyListVo;
    }
}
