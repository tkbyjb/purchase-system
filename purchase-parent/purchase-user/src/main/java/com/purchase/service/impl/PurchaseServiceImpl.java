package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.PurchaseBo;
import com.purchase.dao.mapper.*;
import com.purchase.dao.po.Comb;
import com.purchase.dao.po.DetailComb;
import com.purchase.dao.po.Purchase;
import com.purchase.service.PurchaseService;
import com.purchase.util.SerialNumberGenerator;
import com.purchase.vo.CombVo;
import com.purchase.vo.PurchaseListVo;
import com.purchase.vo.PurchaseVo;
import com.purchase.vo.param.ApplyGetUnreviewParam;
import com.purchase.vo.param.PurchaseGetParam;
import com.purchase.vo.param.process.SupplierPurchaseGetParam;
import com.purchase.vo.process.SupplierPurchaseListVo;
import com.purchase.vo.process.SupplierPurchaseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Resource
    private CombMapper combMapper;
    @Resource
    private PurchaseMapper purchaseMapper;
    @Resource
    private DetailMapper detailMapper;
    @Resource
    private DetailCombMapper detailCombMapper;
    @Resource
    private SupplierMapper supplierMapper;

    @Override
    @Transactional
    public Boolean addPurchase(Long combId, Long userId) {
        Purchase purchase = new Purchase();
        purchase.setCombId(combId);
        purchase.setSerialNumber(SerialNumberGenerator.generator("PU"));
        purchase.setOperatorUserId(userId);
        purchase.setIsDelete(0);
        purchase.setState(1);
        Comb comb = combMapper.selectById(combId);
        purchase.setPurchaseWay(comb.getPurchaseWay());
        purchase.setCreateTime(LocalDateTime.now());
        purchaseMapper.insert(purchase);

        UpdateWrapper<Comb> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("combId", combId);
        updateWrapper.set("state", 4);
        combMapper.update(null, updateWrapper);

        QueryWrapper<DetailComb> queryWrapper = new QueryWrapper();
        queryWrapper.eq("combId", combId);
        List<DetailComb> detailCombs = detailCombMapper.selectList(queryWrapper);
        List<Long> detailIds = new ArrayList<>();
        for (DetailComb detailComb : detailCombs) {
            detailIds.add(detailComb.getDetailId());
        }
        UpdateWrapper updateWrapper1 = new UpdateWrapper();
        updateWrapper1.in("detailId", detailIds);
        updateWrapper1.set("state", 3);
        detailMapper.update(null, updateWrapper1);
        return true;
    }

    @Override
    public PurchaseListVo getPurchaseListVo(PurchaseGetParam param) {
        Page<PurchaseBo> page = new Page<>(param.getPageParam().getPageIndex(), param.getPageParam().getPageSize());
        purchaseMapper.getPurchaseBo(page, param);

        PurchaseListVo purchaseListVo = new PurchaseListVo();
        purchaseListVo.setTotal((int) page.getTotal());
        purchaseListVo.setPurchaseVos(new ArrayList<>());

        for (PurchaseBo purchaseBo : page.getRecords()) {
            PurchaseVo vo = new PurchaseVo();
            vo.setCombVo(new CombVo());

            BeanUtils.copyProperties(purchaseBo.getPurchase(), vo);
            BeanUtils.copyProperties(purchaseBo.getComb(), vo.getCombVo());
            vo.setSignUsername(purchaseBo.getSupplier() != null ? purchaseBo.getSupplier().getSuppliername() : null);
            vo.setOperatorUsername(purchaseBo.getOperatorUser() != null ? purchaseBo.getOperatorUser().getRealname() : null);
            vo.setSignUsername(purchaseBo.getSignUser() != null ? purchaseBo.getSignUser().getRealname() : null);
            vo.setState(purchaseBo.getPurchase().getState());
            vo.setSuppliername(purchaseBo.getSupplier() != null?purchaseBo.getSupplier().getSuppliername(): null);
            purchaseListVo.getPurchaseVos().add(vo);
        }
        return purchaseListVo;
    }

    @Override
    public SupplierPurchaseListVo getSupplierPurchaseListVo(SupplierPurchaseGetParam param, Long supplierId) {
        PurchaseGetParam purchaseGetParam = new PurchaseGetParam();
        BeanUtils.copyProperties(param, purchaseGetParam);
        purchaseGetParam.setSupplierId(supplierId);
        Page<PurchaseBo> page = new Page(param.getPageParam().getPageIndex(), param.getPageParam().getPageSize());
        purchaseMapper.getPurchaseBo(page, purchaseGetParam);

        SupplierPurchaseListVo supplierPurchaseListVo = new SupplierPurchaseListVo();
        supplierPurchaseListVo.setTotal((int)page.getTotal());
        supplierPurchaseListVo.setSupplierPurchaseVos(new ArrayList<>());

        for (PurchaseBo purchaseBo : page.getRecords()) {
            SupplierPurchaseVo vo = new SupplierPurchaseVo();
            BeanUtils.copyProperties(purchaseBo.getPurchase(), vo);
            vo.setOperatorUsername(purchaseBo.getOperatorUser().getRealname());
            vo.setOperatorUserTel(purchaseBo.getOperatorUser().getTel());
            vo.setCombname(purchaseBo.getComb().getCombname());
            vo.setCombId(purchaseBo.getComb().getCombId());
            supplierPurchaseListVo.getSupplierPurchaseVos().add(vo);
        }
        return supplierPurchaseListVo;
    }

    @Override
    public PurchaseListVo getUnpayPurchaseListVo(PurchaseGetParam param, Long userId) {
        param.setSignUserId(userId);
        param.setState(8);
        return getPurchaseListVo(param);
    }


}
