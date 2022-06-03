package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.mapper.DepartmentMapper;
import com.purchase.dao.mapper.UserMapper;
import com.purchase.dao.po.Department;
import com.purchase.service.DepartmentService;
import com.purchase.vo.*;
import com.purchase.vo.param.DepartmentAddParam;
import com.purchase.vo.param.DepartmentBalanceGetParam;
import com.purchase.vo.param.DepartmentGetParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<DepartmentNameVo> getAllName() {
        List<Department> departments = departmentMapper.selectList(new QueryWrapper<>());
        List<DepartmentNameVo> departmentVos = new ArrayList<>();
        for (Department department : departments) {
            DepartmentNameVo departmentVo = new DepartmentNameVo();
            BeanUtils.copyProperties(department, departmentVo);
            departmentVos.add(departmentVo);
        }
        return departmentVos;
    }

    @Override
    public DepartmentListVo getAll(DepartmentGetParam departmentGetParam) {
        Page page = new Page(departmentGetParam.getPageParam().getPageIndex(), departmentGetParam.getPageParam().getPageSize());
        QueryWrapper queryWrapper = new QueryWrapper();
        if(departmentGetParam.getDepartmentname() != null) {
            queryWrapper.like("departmentname", departmentGetParam.getDepartmentname());
        }
        departmentMapper.selectPage(page, queryWrapper);
        DepartmentListVo departmentListVo = new DepartmentListVo();
        departmentListVo.setTotal((int)page.getTotal());
        List<DepartmentVo> departmentVos = new ArrayList<>();
        page.getRecords().forEach(department -> {
            DepartmentVo departmentVo = new DepartmentVo();
            BeanUtils.copyProperties(department, departmentVo);
            departmentVos.add(departmentVo);
        });
        departmentListVo.setDepartmentVos(departmentVos);
        return departmentListVo;
    }

    @Override
    public Boolean updateDepartment(DepartmentVo departmentVo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("departmentname", departmentVo.getDepartmentname());
        Department department = departmentMapper.selectOne(queryWrapper);
        if(department != null && department.getDepartmentId() != departmentVo.getDepartmentId()) {
            return false;
        }
        departmentMapper.updatePart(departmentVo);
        return true;
    }

    @Override
    @Transactional
    public Boolean deletes(List<Long> departmentIds) {
        departmentIds.forEach(departmentId -> {
            userMapper.deleteDepartmentId(departmentId);
        });

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("departmentId", departmentIds);
        departmentMapper.delete(queryWrapper);
        return true;
    }

    @Override
    public Boolean addDepartment(DepartmentAddParam departmentAddParam) throws Exception {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("departmentname", departmentAddParam.getDepartmentname());
        if(departmentMapper.selectOne(queryWrapper) != null) {
            return false;
        }
        Department department = new Department();
        BeanUtils.copyProperties(departmentAddParam, department);
        department.setState(1);
        department.setIsDelete(0);
        if(departmentMapper.insert(department) < 1){//添加失败
            throw new Exception();
        }
        return true;
    }

    @Override
    public DepartmentBalanceListVo getDepartmentBalanceListVo(DepartmentBalanceGetParam param) {
        Page<Department> page = new Page<>(param.getPageParam().getPageIndex(), param.getPageParam().getPageSize());
        QueryWrapper queryWrapper = new QueryWrapper();
        if(param.getDepartmentname() != null && param.getDepartmentname() != "") {
            queryWrapper.like("departmentname", param.getDepartmentname());
        }
        departmentMapper.selectPage(page, queryWrapper);

        DepartmentBalanceListVo departmentBalanceListVo = new DepartmentBalanceListVo();
        departmentBalanceListVo.setTotal((int)page.getTotal());

        List<DepartmentBalanceVo> departmentBalanceVos = new ArrayList<>();
        for (Department record : page.getRecords()) {
            DepartmentBalanceVo vo = new DepartmentBalanceVo();
            BeanUtils.copyProperties(record, vo);
            departmentBalanceVos.add(vo);
        }
        departmentBalanceListVo.setDepartmentBalanceVos(departmentBalanceVos);
        return departmentBalanceListVo;
    }
}
