package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.purchase.dao.bo.DetailBo;
import com.purchase.dao.mapper.*;
import com.purchase.dao.po.*;
import com.purchase.global.DefaultData;
import com.purchase.global.exception.NoBalanceException;
import com.purchase.global.exception.ParamIllegalException;
import com.purchase.global.exception.UploadFileNotLegalException;
import com.purchase.service.FileService;
import com.purchase.service.ProcessService;
import com.purchase.util.SerialNumberGenerator;
import com.purchase.vo.param.process.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {
    @Resource
    private PurchaseMapper purchaseMapper;
    @Resource
    private SupplierMapper supplierMapper;
    @Resource
    private DetailMapper detailMapper;
    @Resource
    private FileService fileService;
    @Resource
    private DefaultData defaultData;
    @Resource
    private SpendingMapper spendingMapper;
    @Resource
    private DetailCombMapper detailCombMapper;
    @Resource
    private ApplyMapper applyMapper;
    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public Integer process1(Process1Param param) throws ParamIllegalException {
        Purchase p = purchaseMapper.selectById(param.getPurchaseId());
        Supplier s =  supplierMapper.selectById(param.getSupplierId());
        if(p == null || s == null) {
            throw new ParamIllegalException();
        }
        if(p.getPurchaseWay() == s.getAgreement()) {//要求的采购方式和供应商类型不匹配
            return -1;
        }
        if(p.getState() != 1 && p.getState() != 20) {//当前采购不在待选择供应商或已取消,不能修改
            return 0;
        }
        Purchase purchase = new Purchase();
        purchase .setPurchaseId(param.getPurchaseId());
        purchase.setSupplierId(param.getSupplierId());
        purchase.setState(2);
        purchaseMapper.updateById(purchase);
        return 1;
    }

    @Override
    @Transactional
    public Integer process2(Process2Param param) throws ParamIllegalException {
        Purchase p = purchaseMapper.selectById(param.getPurchaseId());
        if(p.getState() != 2) return 0;

        for (Process2DetailParam detailDeal : param.getDetailDeals()) {
            if(detailDeal.getDealTotalPrice() == null || detailDeal.getDealUnitPrice() == null || detailDeal.getDealUnitPrice() < 0 || detailDeal.getDealTotalPrice() < 0) {
                throw new ParamIllegalException();
            }
            Detail detail = new Detail();
            detail.setDetailId(detailDeal.getDetailId());
            detail.setDealUnitPrice(detailDeal.getDealUnitPrice());
            detail.setDealTotalPrice(detailDeal.getDealTotalPrice());
            detailMapper.updateById(detail);
        }

        Purchase purchase = new Purchase();
        purchase .setPurchaseId(param.getPurchaseId());
        purchase.setState(3);
        purchaseMapper.updateById(purchase);
        return 1;
    }

    @Override
    @Transactional
    public Integer process3(Process3Param param) throws ParamIllegalException {
        Purchase p = purchaseMapper.selectById(param.getPurchaseId());
        if(p.getState() != 3) return 0;

        Purchase purchase = new Purchase();
        purchase .setPurchaseId(param.getPurchaseId());
        if(param.getResult() == 1) {
            purchase.setState(4);//通过
        }else if(param.getResult() == 2){
            purchase.setState(2);//状态变为待供应商重新给价格,不更新旧给价
        }else if(param.getResult() == 3) {
            purchase.setState(1);//重新选供应商,更新明细给价为null

            List<DetailBo> detailBos = detailMapper.getCombDetails(p.getCombId());
            List<Long> detailIds = new ArrayList<>();
            for (DetailBo detailBo : detailBos) {
                detailIds.add(detailBo.getDetailId());
            }
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.in("detailId", detailIds);
            updateWrapper.set("dealUnitPrice", null);
            updateWrapper.set("dealTotalPrice", null);
            detailMapper.update(null, updateWrapper);
        }else{
            throw new ParamIllegalException();
        }
        purchaseMapper.updateById(purchase);
        return 1;
    }

    @Override
    @Transactional
    public Integer process4(Process4Param param) throws UploadFileNotLegalException, IOException {
        Purchase p = purchaseMapper.selectById(param.getPurchaseId());
        if(p.getState() != 4) return 0;

        String filename = null;
        if(param.getFiles().length < 1) return -1;//没上传合同
        for (MultipartFile file : param.getFiles()) {
            if(!fileService.isFileLegal(file)){
                throw new UploadFileNotLegalException();
            }
            filename = fileService.saveFile(file, defaultData.CONTRACT_UPLOAD_BASE_PATH+"/"+p.getPurchaseId());
        }

        Purchase purchase = new Purchase();
        purchase.setPurchaseId(param.getPurchaseId());
        purchase.setContract(filename);
        purchase.setState(5);
        purchaseMapper.updateById(purchase);
        return 1;
    }

    @Override
    @Transactional
    public Integer process5(Process4Param param) throws UploadFileNotLegalException, IOException {
        Purchase p = purchaseMapper.selectById(param.getPurchaseId());
        if(p.getState() != 5) return 0;

        String filename = null;
        if(param.getFiles().length < 1) return -1;//没上传合同
        for (MultipartFile file : param.getFiles()) {
            if(!fileService.isFileLegal(file)){
                throw new UploadFileNotLegalException();
            }
            filename = fileService.saveFile(file, defaultData.CONTRACT_UPLOAD_BASE_PATH+"/"+p.getPurchaseId());
        }

        Purchase purchase = new Purchase();
        purchase.setPurchaseId(param.getPurchaseId());
        purchase.setContract(filename);//更新合同为操作员新上传的,暂时还没删除旧的合同
        purchase.setState(6);
        purchaseMapper.updateById(purchase);
        return 1;
    }

    @Override
    public Integer process6(Long purchaseId) {
        Purchase p = purchaseMapper.selectById(purchaseId);
        if(p.getState() != 6) return 0;

        Purchase purchase = new Purchase();
        purchase.setPurchaseId(purchaseId);
        purchase.setState(7);
        purchaseMapper.updateById(purchase);
        return 1;
    }

    @Override
    public Integer process7(Long purchaseId, Long payUserId) {
        Purchase p = purchaseMapper.selectById(purchaseId);
        if(p.getState() != 7) return 0;

        Purchase purchase = new Purchase();
        purchase.setPurchaseId(purchaseId);
        purchase.setState(8);
        purchase.setSignUserId(payUserId);
        purchaseMapper.updateById(purchase);
        return 1;
    }

    @Override
    @Transactional
    public Integer process8(Long purchaseId, Long userId) throws NoBalanceException {
        /**
         * 1.获取采购对应组合的明细
         * 2.每个明细生成一条支付记录,并修改明细状态
         * 3.明细部门扣费
         * 4.修改采购状态
         * 5.检查明细对应的申请是不是采购完了,采购完了更新申请状态
         */
        Purchase p = purchaseMapper.selectById(purchaseId);
        if(p.getState() != 8) return 0;
        if(p.getSignUserId().longValue() != userId.longValue()) return -1;

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("combId", p.getCombId());
        List<DetailComb> detailCombs = detailCombMapper.selectList(queryWrapper);
        List<Long> detailIds = new ArrayList<>();
        for (DetailComb detailComb : detailCombs) {
            detailIds.add(detailComb.getDetailId());
        }
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.in("detailId", detailIds);
        List<Detail> details = detailMapper.selectList(queryWrapper1);

        for (Detail detail : details) {
            Spending spending = new Spending();
            spending.setSerialNumber(SerialNumberGenerator.generator("PA"));
            spending.setAmount(detail.getDealTotalPrice());
            Apply apply = applyMapper.selectById(detail.getApplyId());
            spending.setDepartmentId(apply.getApplyDepartmentId());
            spending.setPayUserId(userId);
            spending.setCreateTime(LocalDateTime.now());
            spending.setSpendingTypeId(detail.getSpendingTypeId());
            spending.setPurchaseId(purchaseId);
            spending.setState(1);
            spending.setIsDelete(0);
            spendingMapper.insert(spending);

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("detailId", detail.getDetailId());
            updateWrapper.set("state", 4);
            detailMapper.update(null, updateWrapper);

            Department department = departmentMapper.selectById(apply.getApplyDepartmentId());
            UpdateWrapper updateWrapper1 = new UpdateWrapper();
            updateWrapper1.eq("departmentId", department.getDepartmentId());
            if(department.getBalance()-detail.getDealTotalPrice() < 0){
                throw new NoBalanceException();
            }
            updateWrapper1.set("balance", department.getBalance()-detail.getDealTotalPrice());
            departmentMapper.update(null, updateWrapper1);

            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("applyId", apply.getApplyId());
            List<Detail> detailList = detailMapper.selectList(queryWrapper2);
            boolean flag = true;
            for (Detail detail1 : detailList) {
                if(detail1.getState() != 4) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                UpdateWrapper updateWrapper2 = new UpdateWrapper();
                updateWrapper2.eq("applyId", apply.getApplyId());
                updateWrapper2.set("state", 6);
                applyMapper.update(null, updateWrapper2);
            }
        }

        Purchase purchase = new Purchase();
        purchase.setPurchaseId(purchaseId);
        purchase.setState(9);
        purchaseMapper.updateById(purchase);
        return 1;
    }
}
