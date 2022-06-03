package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purchase.dao.bo.DetailBo;
import com.purchase.dao.mapper.ApplyMapper;
import com.purchase.dao.mapper.DetailMapper;
import com.purchase.dao.po.Apply;
import com.purchase.dao.po.Detail;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.global.exception.ParamIllegalException;
import com.purchase.global.state.State;
import com.purchase.service.DetailService;
import com.purchase.service.SpendingTypeService;
import com.purchase.util.SerialNumberGenerator;
import com.purchase.vo.DetailUnreviewVo;
import com.purchase.vo.DetailUpdateVo;
import com.purchase.vo.DetailVo;
import com.purchase.vo.param.DetailAddParam;
import com.purchase.vo.param.DetailUncombGetParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DetailServiceImpl implements DetailService {
    @Resource
    private DetailMapper detailMapper;
    @Resource
    private SpendingTypeService spendingTypeService;
    @Resource
    private ApplyMapper applyMapper;

    @Override
    public List<Detail> getDetailByApplyId(Long applyId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("applyId", applyId);
        return detailMapper.selectList(queryWrapper);
    }

    @Override
    public List<DetailVo> getDetailVoByApplyId(Long applyId) {
        List<Detail> details = getDetailByApplyId(applyId);
        List<DetailVo> detailVos = new ArrayList<>();
        details.forEach(detail -> {
            DetailVo detailVo = new DetailVo();
            BeanUtils.copyProperties(detail, detailVo);
            detailVo.setSpendingType(spendingTypeService.getById(detail.getSpendingTypeId()).getTypename());
            detailVos.add(detailVo);
        });
        return detailVos;
    }

    @Override
    public Boolean addDetail(DetailAddParam detailAddParam) {
        Detail detail = new Detail();
        BeanUtils.copyProperties(detailAddParam, detail);
        detail.setSerialNumber(SerialNumberGenerator.generator("DE"));
        detail.setState(1);
        detail.setIsDelete(0);
        return detailMapper.insert(detail) > 0 ? true : false;
    }

    @Override
    public Integer deleteByApplyId(Long applyId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("applyId", applyId);
        return detailMapper.delete(queryWrapper);
    }

    @Override
    public Integer deleteByDetailId(Long detailId) throws ParamIllegalException, CusNoPermissionException {
        Detail detail = detailMapper.selectById(detailId);
        if (detail == null) {
            throw new ParamIllegalException();
        }
        Apply apply = applyMapper.selectById(detail.getApplyId());
        if(apply == null || !Arrays.asList(State.APPLY_CAN_EDIT).contains(apply.getState())){
            throw new CusNoPermissionException();
        }
        return detailMapper.deleteById(detailId);
    }

    @Override
    public DetailUpdateVo getByDetailId(Long detailId) {
        Detail detail = detailMapper.selectById(detailId);
        DetailUpdateVo detailUpdateVo = new DetailUpdateVo();
        BeanUtils.copyProperties(detail, detailUpdateVo);
        return detailUpdateVo;
    }

    @Override
    public Integer updateDetail(DetailUpdateVo detailUpdateVo) throws ParamIllegalException, CusNoPermissionException {
        Detail detail = detailMapper.selectById(detailUpdateVo.getDetailId());
        if (detail == null) {
            throw new ParamIllegalException();
        }
        Apply apply = applyMapper.selectById(detail.getApplyId());
        if(apply == null || !Arrays.asList(State.APPLY_CAN_EDIT).contains(apply.getState())){
            throw new CusNoPermissionException();
        }
        Detail detail1 = new Detail();
        BeanUtils.copyProperties(detailUpdateVo, detail1);
        return detailMapper.updateById(detail1);
    }

    @Override
    public List<DetailUnreviewVo> getUncombDetails(DetailUncombGetParam param) {
        List<DetailBo> details = detailMapper.getUncombDetails(param);
        List<DetailUnreviewVo> detailUnreviewVos = new ArrayList<>();
        for (DetailBo detail : details) {
            DetailUnreviewVo vo = new DetailUnreviewVo();
            BeanUtils.copyProperties(detail.getDetail(), vo);
            vo.setSpendingType(detail.getSpendingType().getTypename());
            detailUnreviewVos.add(vo);
        }
        return detailUnreviewVos;
    }

    @Override
    public List<DetailUnreviewVo> getCombDetails(Long combId) {
        List<DetailBo> details = detailMapper.getCombDetails(combId);
        List<DetailUnreviewVo> detailUnreviewVos = new ArrayList<>();
        for (DetailBo detail : details) {
            DetailUnreviewVo vo = new DetailUnreviewVo();
            BeanUtils.copyProperties(detail.getDetail(), vo);
            vo.setSpendingType(detail.getSpendingType().getTypename());
            detailUnreviewVos.add(vo);
        }
        return detailUnreviewVos;
    }
}
