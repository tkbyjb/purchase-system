package com.purchase.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purchase.dao.mapper.SettingMapper;
import com.purchase.dao.po.Setting;
import com.purchase.service.SettingService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SettingServiceImpl implements SettingService {
    @Resource
    private SettingMapper settingMapper;

    @Override
//    @Cacheable(cacheNames = "deadline")
    public Date getDeadline() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("keyc", "deadline");
        Setting setting = settingMapper.selectOne(queryWrapper);
        Date date = DateUtil.parse(setting.getValue());
        return date;
    }

    @Override
    public Boolean setDeadline(String deadline) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("keyc", "deadline");
        Setting setting = settingMapper.selectOne(queryWrapper);
        setting.setValue(deadline);
        settingMapper.updateById(setting);
        return true;
    }

    @Override
    public String getDeadlineString() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("keyc", "deadline");
        Setting setting = settingMapper.selectOne(queryWrapper);
        return setting.getValue();
    }
}
