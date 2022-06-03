package com.purchase.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.purchase.dao.bo.DetailBo;
import com.purchase.dao.po.Detail;
import com.purchase.vo.DetailUpdateVo;
import com.purchase.vo.param.DetailUncombGetParam;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DetailMapper extends BaseMapper<Detail> {
    Integer updateDetail(@Param("detailUpdateVo") DetailUpdateVo detailUpdateVo);
    Integer updatePurchaseWay(List<Long> detailIds, Integer purchaseWay);
    List<DetailBo> getUncombDetails(@Param("param")DetailUncombGetParam param);
    Integer updateState(Integer state, List<Long> detailIds);
    List<DetailBo> getCombDetails(Long combId);
}
