package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purchase.dao.bo.SpendingBo;
import com.purchase.dao.mapper.*;
import com.purchase.vo.statistics.param.BaseParam;
import com.purchase.vo.statistics.vo.CountVo;
import com.purchase.vo.statistics.vo.HeadinfoVo;
import com.purchase.vo.statistics.vo.InfoVo;
import com.purchase.vo.statistics.vo.PurchaseCountVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService {
    @Resource
    private SpendingMapper spendingMapper;
    @Resource
    private ApplyMapper applyMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private SupplierMapper supplierMapper;
    @Resource
    private PurchaseMapper purchaseMapper;

    public List<InfoVo> getSpendingTypePercentage(BaseParam param) {
        List<SpendingBo> spendingBos = spendingMapper.getSpendingTypePercentage(param);
        List<InfoVo> infoVos = new ArrayList<>();
        for (SpendingBo spendingBo : spendingBos) {
            InfoVo vo = new InfoVo();
            vo.setName(spendingBo.getSpendingType().getTypename());
            vo.setValue(spendingBo.getAmount());
            infoVos.add(vo);
        }
        return infoVos;
    }

    public List<PurchaseCountVo> getPurchaseCountVoByDay(BaseParam param) {
        return spendingMapper.getBuydPurchaseCountByDay(param);
    }

    public List<PurchaseCountVo> getPurchaseCountVoByMonth(BaseParam param) {
        return spendingMapper.getBuydPurchaseCountByMonth(param);
    }

    public List<PurchaseCountVo> getPurchaseCountVoByYear(BaseParam param) {
        return spendingMapper.getBuydPurchaseCountByYear(param);
    }

    public List<PurchaseCountVo> getApplyCountVoByDay(BaseParam param) {
        return spendingMapper.getApplyCountByDay(param);
    }

    public List<PurchaseCountVo> getApplyCountVoByMonth(BaseParam param) {
        return spendingMapper.getApplyCountByMonth(param);
    }

    public List<PurchaseCountVo> getApplyCountVoByYear(BaseParam param) {
        return spendingMapper.getApplyCountByYear(param);
    }

    public HeadinfoVo getHeadinfoVo() {
        HeadinfoVo headinfoVo = new HeadinfoVo();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("state", 2);
        headinfoVo.setUserCount(Math.toIntExact(userMapper.selectCount(queryWrapper)));
        headinfoVo.setSupplierCount(Math.toIntExact(supplierMapper.selectCount(queryWrapper)));
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("state", 9);
        headinfoVo.setBuydPurchaseCount(Math.toIntExact(purchaseMapper.selectCount(queryWrapper1)));
        QueryWrapper queryWrapper2 = new QueryWrapper();
        headinfoVo.setApplyCount(Math.toIntExact(applyMapper.selectCount(new QueryWrapper<>())));
        return headinfoVo;
    }
}
