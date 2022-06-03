package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.CombBo;
import com.purchase.dao.mapper.CombMapper;
import com.purchase.dao.mapper.DetailCombMapper;
import com.purchase.dao.mapper.DetailMapper;
import com.purchase.dao.po.Comb;
import com.purchase.dao.po.DetailComb;
import com.purchase.dao.po.User;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.global.state.State;
import com.purchase.service.CombService;
import com.purchase.util.SerialNumberGenerator;
import com.purchase.vo.CombListVo;
import com.purchase.vo.CombVo;
import com.purchase.vo.param.CombAddParam;
import com.purchase.vo.param.CombGetParam;
import com.purchase.vo.param.CombUpdateParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CombServiceImpl implements CombService {
    @Resource
    private CombMapper combMapper;
    @Resource
    private DetailCombMapper detailCombMapper;
    @Resource
    private DetailMapper detailMapper;

    @Override
    @Transactional
    public Boolean addComb(CombAddParam combAddParam, User user) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("detailId", combAddParam.getDetailIds());
        List<DetailComb> detailCombs = detailCombMapper.selectList(queryWrapper);
        if (detailCombs != null && detailCombs.size() > 0) {//明细已经在组合中了
            return false;
        }
        Comb comb = new Comb();
        BeanUtils.copyProperties(combAddParam, comb);
        comb.setCreateTime(LocalDateTime.now());
        comb.setIsDelete(0);
        comb.setState(1);
        comb.setSerialNumber(SerialNumberGenerator.generator("CO"));
        comb.setCreateUserId(user.getUserId());
        comb.setPurchaseWay(detailMapper.selectById(combAddParam.getDetailIds().get(0)).getPurchaseWay());
        combMapper.insert(comb);
        for (Long detailId : combAddParam.getDetailIds()) {
            DetailComb detailComb = new DetailComb();
            detailComb.setCombId(comb.getCombId());
            detailComb.setDetailId(detailId);
            detailCombMapper.insert(detailComb);
        }
        return true;
    }

    @Override
    public CombListVo getCombVo(CombGetParam param) {
        Page<CombBo> page = new Page<>(param.getPageParam().getPageIndex(), param.getPageParam().getPageSize());
        combMapper.getCombs(page, param);
        CombListVo combListVo = new CombListVo();
        combListVo.setTotal((int) page.getTotal());
        List<CombVo> combVos = new ArrayList<>();
        for (CombBo combBo : page.getRecords()) {
            CombVo combVo = new CombVo();
            BeanUtils.copyProperties(combBo.getComb(), combVo);
            combVo.setCreatUsername(combBo.getCreateUser().getRealname());
            combVo.setConfirmUsername(combBo.getConfirmUser() != null ? combBo.getConfirmUser().getRealname() : null);
            combVos.add(combVo);
        }
        combListVo.setCombVos(combVos);
        return combListVo;
    }

    @Override
    public CombListVo getUnreviewCombVo(CombGetParam param) {
        param.setState(2);
        return getCombVo(param);
    }

    @Override
    public Boolean updateCombToReview(Long combId, Long assignUserId) throws CusNoPermissionException {
        Comb comb1 = combMapper.selectById(combId);
        if (comb1 == null || !Arrays.asList(State.COMB_CAN_DEIT).contains(comb1.getState())) {
            throw new CusNoPermissionException();
        }
        Comb comb = new Comb();
        comb.setCombId(combId);
        comb.setState(2);
        comb.setConfirmUserId(assignUserId);
        return combMapper.updateById(comb) > 0 ? true : false;
    }

    @Override
    public CombListVo getUnconfirmCombVo(CombGetParam param) {
        param.setState(3);
        return getCombVo(param);
    }

    @Override
    @Transactional
    public Boolean deletes(List<Long> combIds) {
        for(Long combId: combIds) {
            Comb comb = combMapper.selectById(combId);
            if(!Arrays.asList(State.COMB_CAN_DEIT).contains(comb.getState())) {
                return false;
            }
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.in("combId", combIds);
        combMapper.delete(updateWrapper);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateComb(CombUpdateParam param) {
        Comb comb = combMapper.selectById(param.getCombId());
        if(!Arrays.asList(State.COMB_CAN_DEIT).contains(comb.getState())) {
            return false;
        }
        Comb comb1 = new Comb();
        comb.setCombId(param.getCombId());
        comb.setNote(param.getNote());
        comb.setCombname(param.getCombname());
        combMapper.updateById(comb1);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("combId", param.getCombId());
        detailCombMapper.delete(queryWrapper);

        for (Long detailId : param.getDetailIds()) {
            DetailComb detailComb = new DetailComb();
            detailComb.setDetailId(detailId);
            detailComb.setCombId(param.getCombId());
            detailCombMapper.insert(detailComb);
        }
        return true;
    }

}
