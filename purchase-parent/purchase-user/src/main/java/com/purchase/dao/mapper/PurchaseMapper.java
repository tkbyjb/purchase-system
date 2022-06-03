package com.purchase.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.PurchaseBo;
import com.purchase.dao.po.Purchase;
import com.purchase.vo.param.PurchaseGetParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PurchaseMapper extends BaseMapper<Purchase> {
    Page<PurchaseBo> getPurchaseBo(Page<PurchaseBo> page, PurchaseGetParam param);
    Page<PurchaseBo> getPurchaseBoInPurchaseIds(Page<PurchaseBo> page, List<Long> purchaseIds);
}
