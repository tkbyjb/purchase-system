package com.purchase.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.po.Supplier;
import com.purchase.vo.param.SupplierGetParam;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
    Page<Supplier> getSupplier(Page<Supplier> page, SupplierGetParam param);
}
