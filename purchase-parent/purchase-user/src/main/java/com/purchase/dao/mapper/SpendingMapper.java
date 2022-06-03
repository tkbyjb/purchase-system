package com.purchase.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.purchase.dao.bo.SpendingBo;
import com.purchase.dao.po.Purchase;
import com.purchase.dao.po.Spending;
import com.purchase.vo.statistics.param.BaseParam;
import com.purchase.vo.statistics.vo.CountVo;
import com.purchase.vo.statistics.vo.PurchaseCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SpendingMapper extends BaseMapper<Spending> {
    List<SpendingBo> getSpendingTypePercentage(@Param("param") BaseParam param);
    List<PurchaseCountVo> getBuydPurchaseCountByDay(@Param("param") BaseParam param);
    List<PurchaseCountVo> getBuydPurchaseCountByMonth(@Param("param") BaseParam param);
    List<PurchaseCountVo> getBuydPurchaseCountByYear(@Param("param") BaseParam param);

    List<PurchaseCountVo> getApplyCountByDay(@Param("param") BaseParam param);
    List<PurchaseCountVo> getApplyCountByMonth(@Param("param") BaseParam param);
    List<PurchaseCountVo> getApplyCountByYear(@Param("param") BaseParam param);
}
