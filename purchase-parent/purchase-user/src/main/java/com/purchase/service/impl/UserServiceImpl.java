package com.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.UserBo;
import com.purchase.dao.mapper.MenuMapper;
import com.purchase.dao.mapper.RoleMapper;
import com.purchase.dao.mapper.RoleMenuMapper;
import com.purchase.dao.mapper.UserMapper;
import com.purchase.dao.po.Menu;
import com.purchase.dao.po.Role;
import com.purchase.dao.po.RoleMenu;
import com.purchase.dao.po.User;
import com.purchase.global.DefaultData;
import com.purchase.global.exception.UploadFileNotLegalException;
import com.purchase.service.FileService;
import com.purchase.service.UserService;
import com.purchase.vo.UserAssignVo;
import com.purchase.vo.param.system.UserAddParam;
import com.purchase.vo.param.system.UserGetParam;
import com.purchase.vo.param.system.UserUpdateParam;
import com.purchase.vo.system.UserListVo;
import com.purchase.vo.system.UserVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private FileService fileService;
    @Resource
    private DefaultData defaultData;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public User getUserByEmail(String email) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", email);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Long addUser(User user) {
        userMapper.insert(user);
        return user.getUserId();
    }

    @Override
    public Integer updateCreateUserId(Long userId, Long createUserId) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("userId", userId);
        updateWrapper.set("createUserId", createUserId);
        return userMapper.update(null, updateWrapper);
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", password);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public UserListVo getUserBos(UserGetParam userGetParam) {
        Page<UserBo> page = new Page<>(userGetParam.getPageParam().getPageIndex(), userGetParam.getPageParam().getPageSize());
        userMapper.getUserBos(page, userGetParam);
        UserListVo userListVo = new UserListVo();
        userListVo.setTotal((int) page.getTotal());

        List<UserVo> userVos = new ArrayList<>();
        for (UserBo record : page.getRecords()) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(record.getUser(), userVo);
            userVo.setCreateUserRealname(record.getCreateUser().getRealname());
            userVo.setDepartmentname(record.getDepartment().getDepartmentname());
            userVo.setDepartmentId(record.getDepartment().getDepartmentId());
            userVo.setRoleId(record.getRole() != null ? record.getRole().getRoleId() : null);
            userVo.setRolename(record.getRole() != null ? record.getRole().getRolename() : null);

            if (!userVo.getAvatar().equals(defaultData.AVATAR_URL)) {
                userVo.setAvatar(defaultData.AVATAR_ACCESS_BASE_URL + "/" + userVo.getAvatar());//test
            }

            userVos.add(userVo);
        }
        userListVo.setUserVos(userVos);
        return userListVo;
    }

    @Override
    @Transactional
    public Boolean updateUser(UserUpdateParam userUpdateParam) throws UploadFileNotLegalException, IOException {
        String oldAvatar = null;
        String filename = null;
        if (userUpdateParam.getFiles() != null && userUpdateParam.getFiles().length > 0) {
            for (MultipartFile file : userUpdateParam.getFiles()) {
                if (!fileService.isFileLegal(file)) {
                    throw new UploadFileNotLegalException();
                }
            }
            filename = fileService.saveFile(userUpdateParam.getFiles()[0], defaultData.AVATAR_UPLOAD_PATH);
            oldAvatar = userMapper.selectById(userUpdateParam.getUserId()).getAvatar();
        }
        userUpdateParam.setAvatar(filename);//sql会判断是否为空,为空不修改

//        if(userUpdateParam.getRoleId() != null) {
//            userUpdateParam.setState(1);//恢复账号
//        }else{
//            userUpdateParam.setState(3);//不分配角色
//        }
        if (userMapper.updateUser(userUpdateParam) > 0) {//修改成功且头像改了才删除原来的
            if (filename != null && !oldAvatar.equals(defaultData.AVATAR_URL))
                rabbitTemplate.convertAndSend("routing_exchange", "purchase_delete_avatar", oldAvatar);
        } else {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean addUserBySystem(UserAddParam userAddParam, User createUser) throws UploadFileNotLegalException, IOException {
        String filename = null;
        if (userAddParam.getFiles() != null && userAddParam.getFiles().length > 0) {
            for (MultipartFile file : userAddParam.getFiles()) {
                if (!fileService.isFileLegal(file)) {
                    throw new UploadFileNotLegalException();
                }
            }
            filename = fileService.saveFile(userAddParam.getFiles()[0], defaultData.AVATAR_UPLOAD_PATH);
        } else {
            filename = defaultData.AVATAR_URL;
        }
        User user = new User();
        BeanUtils.copyProperties(userAddParam, user);
        user.setState(1);
        user.setIsDelete(0);
        user.setAvatar(filename);
        user.setCreateTime(LocalDateTime.now());
        user.setCreateUserId(createUser.getUserId());
        return userMapper.insert(user) > 0 ? true : false;
    }

    @Override
    public User isExistUsername(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User isExistEmail(String email) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("email", email);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer deleteUsers(List<Long> userIds) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("userId", userIds);
        return userMapper.delete(queryWrapper);
    }

    @Override
    public Boolean reviewUser(Integer state, Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getState() != 0) {
            return false;
        }
        if (userMapper.upateUserState(state, userId) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<UserAssignVo> getUserAssignVo(Integer level) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(level==1) {
            queryWrapper.eq("permission", "apply-review-1");
        }else if(level==2){
            queryWrapper.eq("permission", "apply-review-2");
        }else if(level==3){
            queryWrapper.eq("permission", "apply-review-3");
        }else if(level==4){
            queryWrapper.eq("permission", "comb-review");
        }else{
            queryWrapper.eq("permission", "comb-undeal-get");
        }
        Menu menu = menuMapper.selectOne(queryWrapper);

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("menuId", menu.getMenuId());
        queryWrapper1.select("roleId");
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(queryWrapper1);

        List<Long> roleIds = new ArrayList<>();
        roleMenus.forEach(roleMenu -> {
            roleIds.add(roleMenu.getRoleId());
        });
        QueryWrapper queryWrapper2 = new QueryWrapper<User>().in("roleId", roleIds);
        queryWrapper.select("userId", "realname");
        List<User> users = userMapper.selectList(queryWrapper2);
        List<UserAssignVo> userAssignVos = new ArrayList<>();
        for (User user : users) {
            UserAssignVo vo = new UserAssignVo();
            BeanUtils.copyProperties(user, vo);
            userAssignVos.add(vo);
        }
        return userAssignVos;
    }

    @Override
    public List<UserAssignVo> getPayUsers() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("permission", "pay");
        Menu menu = menuMapper.selectOne(queryWrapper);
        List<RoleMenu> roles = roleMenuMapper.selectList(new QueryWrapper<RoleMenu>().eq("menuId", menu.getMenuId()));
        List<Long> roleIds = new ArrayList<>();
        roles.forEach(roleMenu -> {
            roleIds.add(roleMenu.getRoleId());
        });
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.in("roleId", roleIds);
        queryWrapper.eq("state", 2);
        List<User> users = userMapper.selectList(queryWrapper1);
        List<UserAssignVo> userAssignVos = new ArrayList<>();
        users.forEach(user -> {
            UserAssignVo vo = new UserAssignVo();
            BeanUtils.copyProperties(user, vo);
            userAssignVos.add(vo);
        });
        return userAssignVos;
    }
}
