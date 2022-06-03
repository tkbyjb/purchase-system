package com.purchase.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.CombBo;
import com.purchase.dao.po.Comb;
import com.purchase.vo.param.CombGetParam;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CombMapper extends BaseMapper<Comb> {
    Page<CombBo> getCombs(Page<CombBo> page, CombGetParam param);
    Integer updateCombState(Long combId, Integer state);
    Integer updateConfirmUserId(Long combId, Long confirmUserId);
    CombBo getCombBoByCombIdWithDeleted(Long combId);
}
