package com.purchase.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.purchase.dao.bo.RoleBo;
import com.purchase.dao.po.Supplier;
import com.purchase.dao.po.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import cn.dev33.satoken.stp.StpInterface;

import javax.annotation.Resource;

/**
 * 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        User user = (User) redisTemplate.opsForValue().get("userId_" + loginId);
        if (user != null) {
            Map<Long, RoleBo> map = (Map<Long, RoleBo>) redisTemplate.opsForValue().get("all_role_menus");
            RoleBo roleBo = map.get(user.getRoleId());
            roleBo.getMenus().forEach(menu -> {
                list.add(menu.getPermission());
            });
        } else {
            Supplier supplier = (Supplier) redisTemplate.opsForValue().get("supplierId_" + loginId);
            if (supplier != null) {
                Map<Long, RoleBo> map = (Map<Long, RoleBo>) redisTemplate.opsForValue().get("all_role_menus");
                RoleBo roleBo = map.get(7l);
                roleBo.getMenus().forEach(menu -> {
                    list.add(menu.getPermission());
                });
            }
        }
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        User user = (User) redisTemplate.opsForValue().get("userId_" + loginId);
        list.add(user.getRoleId() + "");
        return list;
    }

}

