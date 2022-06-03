package com.purchase.service;

import com.purchase.dao.po.SpendingType;

import java.util.List;

public interface SpendingTypeService {
    SpendingType getById(Long id);
    List<SpendingType> getAll();
}
