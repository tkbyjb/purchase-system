package com.purchase.service;

import com.purchase.dao.po.Detail;
import com.purchase.global.exception.CusNoPermissionException;
import com.purchase.global.exception.ParamIllegalException;
import com.purchase.vo.DetailUnreviewVo;
import com.purchase.vo.DetailUpdateVo;
import com.purchase.vo.DetailVo;
import com.purchase.vo.param.DetailAddParam;
import com.purchase.vo.param.DetailUncombGetParam;

import java.util.List;

public interface DetailService {
    List<Detail> getDetailByApplyId(Long applyId);
    List<DetailVo> getDetailVoByApplyId(Long applyId);
    Boolean addDetail(DetailAddParam detailAddParam);
    Integer deleteByApplyId(Long applyId);
    Integer deleteByDetailId(Long detailId) throws ParamIllegalException, CusNoPermissionException;
    DetailUpdateVo getByDetailId(Long detailId);
    Integer updateDetail(DetailUpdateVo detailUpdateVo) throws ParamIllegalException, CusNoPermissionException;
    List<DetailUnreviewVo> getUncombDetails(DetailUncombGetParam param);
    List<DetailUnreviewVo> getCombDetails(Long combId);
}
