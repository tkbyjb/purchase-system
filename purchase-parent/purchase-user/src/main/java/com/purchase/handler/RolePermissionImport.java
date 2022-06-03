package com.purchase.handler;

import com.purchase.dao.bo.RoleBo;
import com.purchase.dao.mapper.MenuMapper;
import com.purchase.dao.mapper.RoleMapper;
import com.purchase.dao.po.Menu;
import com.purchase.service.RoleService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 项目启动时从数据库读取所有角色及其对应权限到redis,方便后边权限检查
 */
@Component
public class RolePermissionImport {
    @Resource
    private RoleService roleService;
    @Resource
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        List<RoleBo> roleBos = roleService.getAllRoleBo();
        Map<Long, RoleBo> map = new HashMap<>();
        for (RoleBo roleBo : roleBos) {
            map.put(roleBo.getRole().getRoleId(), roleBo);
        }
        redisTemplate.opsForValue().set("all_role_menus", map);
    }
}
