package com.purchase.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.mapper.SupplierMapper;
import com.purchase.dao.po.Supplier;
import com.purchase.service.SupplierService;
import com.purchase.vo.SupplierListVo;
import com.purchase.vo.SupplierVo;
import com.purchase.vo.param.SupplierGetParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Resource
    private SupplierMapper supplierMapper;

    @Override
    public SupplierListVo getSupplierListVo(SupplierGetParam param) {
        Page<Supplier> page = new Page<>(param.getPageParam().getPageIndex(), param.getPageParam().getPageSize());
        supplierMapper.getSupplier(page, param);
        SupplierListVo supplierListVo = new SupplierListVo();
        supplierListVo.setTotal((int)page.getTotal());
        supplierListVo.setSupplierVos(new ArrayList<>());
        for (Supplier supplier : page.getRecords()) {
            SupplierVo vo = new SupplierVo();
            BeanUtils.copyProperties(supplier, vo);
            supplierListVo.getSupplierVos().add(vo);
        }
        return supplierListVo;
    }
}
