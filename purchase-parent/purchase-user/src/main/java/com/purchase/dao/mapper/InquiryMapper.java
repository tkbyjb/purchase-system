package com.purchase.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.InquiryBo;
import com.purchase.dao.po.Inquiry;
import com.purchase.vo.param.InquiryGetParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InquiryMapper extends BaseMapper<Inquiry> {
    Page<InquiryBo> getInquiryBos(Page<InquiryBo> page, InquiryGetParam param);
    @Select("select purchaseId from t_inquiry where supplierId=#{supplierId} and isDelete=0  group by purchaseId")
    List<Long> getPurchaseIds(Long supplierId);
    @Select("select supplierId from t_inquiry where purchaseId=#{purchaseId} and isDelete=0  group by supplierId")
    List<Long> getSupplierIds(Long purchaseId);
}
