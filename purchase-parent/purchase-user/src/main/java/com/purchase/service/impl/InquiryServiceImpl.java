package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.InquiryBo;
import com.purchase.dao.bo.PurchaseBo;
import com.purchase.dao.mapper.*;
import com.purchase.dao.po.*;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.service.InquiryService;
import com.purchase.vo.InquiryDetailVo;
import com.purchase.vo.InquiryListVo;
import com.purchase.vo.InquiryViewVo;
import com.purchase.vo.InquiryVo;
import com.purchase.vo.param.InquiryGetParam;
import com.purchase.vo.param.process.InquiryPriceParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class InquiryServiceImpl implements InquiryService {
    @Resource
    private InquiryMapper inquiryMapper;
    @Resource
    private PurchaseMapper purchaseMapper;
    @Resource
    private DetailCombMapper detailCombMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CombMapper combMapper;
    @Resource
    private DetailMapper detailMapper;
    @Resource
    private SupplierMapper supplierMapper;

    @Override
    @Transactional
    public Integer addInquiryByPruchase(Long purchaseId, Long supplierId) {
        /**
         * 不能一个采购重复给一个供应商询价(多次那种),倒是可以采用修改那种
         */
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        if(purchase.getState() != 1 && purchase.getState() != 20)return 0;//不能询价

        QueryWrapper queryWrapper3 = new QueryWrapper();
        queryWrapper3.eq("purchaseId", purchaseId);
        queryWrapper3.eq("supplierId", supplierId);
        List<Inquiry> inquiries = inquiryMapper.selectList(queryWrapper3);
        if(inquiries.size() > 0){
            return -1;
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("combId", purchase.getCombId());
        List<DetailComb> detailCombs = detailCombMapper.selectList(queryWrapper);
        for (DetailComb detailComb : detailCombs) {
            Inquiry inquiry = new Inquiry();
            inquiry.setIsDelete(0);
            inquiry.setState(0);
            inquiry.setPurchaseId(purchaseId);
            inquiry.setDetailId(detailComb.getDetailId());
            inquiry.setSupplierId(supplierId);
            inquiryMapper.insert(inquiry);
        }
        return 1;
    }

    @Override
    public InquiryListVo getInquiryListVo(InquiryGetParam param) {
        Page<InquiryBo> page = new Page<>(param.getPageParam().getPageIndex(), param.getPageParam().getPageSize());
        inquiryMapper.getInquiryBos(page, param);

        InquiryListVo inquiryListVo = new InquiryListVo();
        inquiryListVo.setTotal((int)page.getTotal());
        inquiryListVo.setInquiryVos(new ArrayList<>());

        for (InquiryBo bo : page.getRecords()) {
            InquiryVo vo = new InquiryVo();
            BeanUtils.copyProperties(bo, vo);
            User user = userMapper.selectById(bo.getPurchase().getOperatorUserId());
            vo.setOperatorUsername(user.getRealname());
            vo.setTel(user.getTel());
            vo.setPurchaseWay(bo.getPurchase().getPurchaseWay());
            Comb comb = combMapper.selectById(bo.getPurchase().getCombId());
            vo.setCombname(comb.getCombname());
            inquiryListVo.getInquiryVos().add(vo);
        }
        return inquiryListVo;
    }

    @Override
    public InquiryListVo getInquiryListVo2(InquiryGetParam param) {
        List<Long> purchaseIds = inquiryMapper.getPurchaseIds(param.getSupplierId());
        Page<PurchaseBo> page = new Page<>(param.getPageParam().getPageIndex(), param.getPageParam().getPageSize());
        purchaseMapper.getPurchaseBoInPurchaseIds(page, purchaseIds);

        InquiryListVo inquiryListVo = new InquiryListVo();
        inquiryListVo.setTotal((int) page.getTotal());
        inquiryListVo.setInquiryVos(new ArrayList<>());

        for (PurchaseBo bo : page.getRecords()) {
            InquiryVo vo = new InquiryVo();
            vo.setCombname(bo.getComb().getCombname());
            vo.setCombId(bo.getComb().getCombId());
            vo.setTel(bo.getOperatorUser().getTel());
            vo.setOperatorUsername(bo.getOperatorUser().getRealname());
            vo.setPurchaseWay(bo.getPurchase().getPurchaseWay());
            vo.setPurchaseId(bo.getPurchaseId());

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("purchaseId", bo.getPurchaseId());
            List<Inquiry> inquiries = inquiryMapper.selectList(queryWrapper);
            vo.setCreateTime(inquiries.get(0).getCreateTime());
            vo.setState(inquiries.get(0).getState());
            inquiryListVo.getInquiryVos().add(vo);
        }
        return inquiryListVo;
    }

    @Override
    public List<InquiryDetailVo> getInquiryDetailVo(Long purchaseId, Long supplierId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("purchaseId", purchaseId);
        queryWrapper.eq("supplierId", supplierId);
        List<Inquiry> inquiries = inquiryMapper.selectList(queryWrapper);

        List<InquiryDetailVo> inquiryDetailVos = new ArrayList<>();
        for (Inquiry inquiry : inquiries) {
            InquiryDetailVo vo = new InquiryDetailVo();
            vo.setInquiryId(inquiry.getInquiryId());
            vo.setUnitPrice(inquiry.getUnitPrice());
            vo.setTotalPrice(inquiry.getTotalPrice());
            Detail detail = detailMapper.selectById(inquiry.getDetailId());
            vo.setCount(detail.getCount());
            vo.setPredictTotalPrice(detail.getPredictTotalPrice());
            vo.setPredictUnitPrice(detail.getPredictUnitPrice());
            vo.setUnit(detail.getUnit());
            vo.setDetailname(detail.getDetailname());
            vo.setNote(detail.getNote());
            inquiryDetailVos.add(vo);
        }
        return inquiryDetailVos;
    }

    @Override
    @Transactional
    public Boolean setInquiryPrice(List<InquiryPriceParam> params) throws CusNoPermissionException {
        for (InquiryPriceParam param : params) {
            if(param.getTotalPrice() == null || param.getUnitPrice() == null || param.getUnitPrice() <0 || param.getTotalPrice()<0) {
                throw new CusNoPermissionException();
            }
            Inquiry inquiry = new Inquiry();
            inquiry.setInquiryId(param.getInquiryId());
            inquiry.setUnitPrice(param.getUnitPrice());
            inquiry.setTotalPrice(param.getTotalPrice());
            inquiry.setState(1);
            inquiryMapper.updateById(inquiry);
        }
        return true;
    }

    @Override
    public List<InquiryViewVo> getInquirysByPurchase(Long purchaseId) {
        List<InquiryViewVo> inquiryViewVos = new ArrayList<>();
        List<Long> supplierIds = inquiryMapper.getSupplierIds(purchaseId);

        for (Long supplierId : supplierIds) {
            InquiryViewVo vo = new InquiryViewVo();
            Supplier supplier = supplierMapper.selectById(supplierId);
            vo.setSuppliername(supplier.getSuppliername());
            vo.setInquiryDetailVos(getInquiryDetailVo(purchaseId, supplierId));
            inquiryViewVos.add(vo);
        }
        return inquiryViewVos;
    }


}
