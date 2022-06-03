package com.purchase.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import java.util.Date;

public class SerialNumberGenerator {
    public static String generator(String prefix) {
        //生成流水号,随机的,不按顺序
        return prefix + DateUtil.format(new Date(), "yyyyMMdd")  + IdUtil.simpleUUID();
    }
}
