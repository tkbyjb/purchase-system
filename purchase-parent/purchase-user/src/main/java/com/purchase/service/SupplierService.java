package com.purchase.service;

import com.purchase.vo.SupplierListVo;
import com.purchase.vo.param.SupplierGetParam;

public interface SupplierService {
    SupplierListVo getSupplierListVo(SupplierGetParam param);
}
