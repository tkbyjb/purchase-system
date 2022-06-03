package com.purchase.service;

import com.purchase.dao.po.User;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.vo.CombListVo;
import com.purchase.vo.CombVo;
import com.purchase.vo.param.CombAddParam;
import com.purchase.vo.param.CombGetParam;
import com.purchase.vo.param.CombUpdateParam;

import java.util.List;

public interface CombService {
    Boolean addComb(CombAddParam combAddParam, User user);
    CombListVo getCombVo(CombGetParam param);
    CombListVo getUnreviewCombVo(CombGetParam param);
    Boolean updateCombToReview(Long combId, Long assignUserId) throws CusNoPermissionException;
    CombListVo getUnconfirmCombVo(CombGetParam param);
    Boolean deletes(List<Long> combIds);
    Boolean updateComb(CombUpdateParam param);
}
