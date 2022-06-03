package com.purchase.service;

import com.purchase.dao.po.Department;
import com.purchase.vo.DepartmentBalanceListVo;
import com.purchase.vo.DepartmentListVo;
import com.purchase.vo.DepartmentNameVo;
import com.purchase.vo.DepartmentVo;
import com.purchase.vo.param.DepartmentAddParam;
import com.purchase.vo.param.DepartmentBalanceGetParam;
import com.purchase.vo.param.DepartmentGetParam;

import java.util.List;

public interface DepartmentService {
    List<DepartmentNameVo> getAllName();
    DepartmentListVo getAll(DepartmentGetParam departmentGetParam);
    Boolean updateDepartment(DepartmentVo departmentVo);
    Boolean deletes(List<Long> departmentIds);
    Boolean addDepartment(DepartmentAddParam departmentAddParam) throws Exception;
    DepartmentBalanceListVo getDepartmentBalanceListVo(DepartmentBalanceGetParam param);
}
