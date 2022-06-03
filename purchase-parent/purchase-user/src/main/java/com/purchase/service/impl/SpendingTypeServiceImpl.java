package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purchase.dao.mapper.SpendingTypeMapper;
import com.purchase.dao.po.SpendingType;
import com.purchase.service.SpendingTypeService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SpendingTypeServiceImpl implements SpendingTypeService {
    @Resource
    private SpendingTypeMapper spendingTypeMapper;

    @Override
    @Cacheable(cacheNames = "spendingType", unless = "#result == null")
    public SpendingType getById(Long id) {
        return spendingTypeMapper.selectById(id);
    }

    @Override
    public List<SpendingType> getAll() {
        return spendingTypeMapper.selectList(new QueryWrapper<>());
    }
}
