package com.purchase.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.purchase.dao.bo.UserBo;
import com.purchase.dao.po.User;
import com.purchase.global.exception.UploadFileNotLegalException;
import com.purchase.vo.UserAssignVo;
import com.purchase.vo.param.system.UserAddParam;
import com.purchase.vo.param.system.UserGetParam;
import com.purchase.vo.param.system.UserUpdateParam;
import com.purchase.vo.system.UserListVo;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User getUserByEmail(String email);
    User getUserByUsername(String username);
    Long addUser(User user);
    Integer updateCreateUserId(Long userId, Long createUserId);
    User getUserByUsernameAndPassword(String username, String password);
    UserListVo getUserBos(UserGetParam userGetParam);
    Boolean updateUser(UserUpdateParam userUpdateParam) throws UploadFileNotLegalException, IOException;
    Boolean addUserBySystem(UserAddParam userAddParam, User createUser) throws UploadFileNotLegalException, IOException;
    User isExistUsername(String username);
    User isExistEmail(String email);
    Integer deleteUsers(List<Long> userIds);
    Boolean reviewUser(Integer state, Long userId);
    List<UserAssignVo> getUserAssignVo(Integer level);
    List<UserAssignVo> getPayUsers();
}
