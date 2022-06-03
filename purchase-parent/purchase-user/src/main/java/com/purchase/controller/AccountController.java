package com.purchase.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.purchase.dao.po.User;
import com.purchase.global.DefaultData;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.global.state.State;
import com.purchase.service.AccountService;
import com.purchase.service.MenuService;
import com.purchase.vo.LoginVo;
import com.purchase.vo.param.LoginParam;
import com.purchase.vo.param.RegisterParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.Arrays;

@RestController
@Api(tags = "用户账号相关的接口")
@Validated
public class AccountController {
    @Resource
    private AccountService accountService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private MenuService menuService;
    @Resource
    private DefaultData defaultData;

    @PostMapping("/public/user/register")
    @ApiOperation("用户账号自己注册")
    public RespResult register(@RequestBody @Valid RegisterParam registerParam) throws Exception {
        /**
         * 1.邮箱是否已注册
         * 2.用户名是否已注册
         * 3.检查验证码
         * 4.写入数据库
         */
        if (accountService.isExistEmail(registerParam.getEmail())) {
            return new RespResult(ResultState.EXIST_EMAIL);
        }
        if (accountService.isExistUsername(registerParam.getUsername())) {
            return new RespResult(ResultState.EXIST_USERNAME);
        }
        String verifyCode = (String) redisTemplate.opsForValue().get("register_" + registerParam.getEmail());
        if (!verifyCode.equals(registerParam.getVerifyCode())) {
            return new RespResult(ResultState.VERIFYCOE_ERROR);
        }
        if (accountService.userRegister(registerParam)) {
            return new RespResult(ResultState.SUCCESS, "注册成功,请等待管理员审核,审核结果会邮件通知", null);
        } else {
            throw new Exception();
        }
    }

    @GetMapping("/public/verifyCode/register")
    @ApiOperation("获取用户注册验证码")
    public RespResult getVerifyCode(@Email String email) {
        /**
         * 1.邮箱是否已注册
         * 2.生成验证码
         * 3.验证码存入redis
         * 4.验证码发至邮箱
         */
        if (accountService.isExistEmail(email)) {
            return new RespResult(ResultState.EXIST_EMAIL);
        }
        accountService.sendRegisterVerifyCode(email);
        return new RespResult(ResultState.SUCCESS, "验证码发送成功", null);
    }

    @PostMapping("/public/user/login")
    @ApiOperation("用户登录")
    public RespResult userLogin(@RequestBody @Valid LoginParam loginParam) {
        /*
         * 1.用户是否已登录+redis此用户输入尝试是否>5次
         * 2.是否有此用户+账号state是否可登录状态中+密码是否正确|密码错误,redis次数+1
         * 3.生成token,user放入redis
         * 4.返回用户名+用户头像+token
         * 登录成功时把redis中尝试次数删掉防止锁账号
         */
        User user = accountService.userLogin(loginParam);
        if (user == null) {
            return new RespResult(ResultState.LOGIN_FAIL, "登录失败,用户名或密码错误", null);
        }
        if (!Arrays.asList(State.USER_CAN_LOGIN).contains(user.getState())) {
            return new RespResult(ResultState.LOGIN_FAIL, "登录失败,账号未审核或审核不通过", null);
        }
        StpUtil.login(user.getUserId());
        String token = StpUtil.getTokenValue();
        redisTemplate.opsForValue().set("userId_" + user.getUserId(), user);
        LoginVo loginVo = new LoginVo();
        if (!user.getAvatar().equals(defaultData.AVATAR_URL)) {
            loginVo.setAvatar(defaultData.AVATAR_ACCESS_BASE_URL + "/" + user.getAvatar());
        } else {
            loginVo.setAvatar(user.getAvatar());
        }
        loginVo.setToken(token);
        loginVo.setUsername(user.getUsername());
        loginVo.setMenuVos(menuService.getRoleMenuVo(user.getRoleId(), null));
        loginVo.setType(0);
        loginVo.setId(user.getUserId());
        return new RespResult(ResultState.SUCCESS, "登录成功", loginVo);
    }

    @GetMapping("/logout")
    @ApiOperation("退出登录")
    @SaCheckLogin
    public RespResult logout() {
        if(!redisTemplate.delete("userId_"+ StpUtil.getLoginId())){
            redisTemplate.delete("supplierId_"+ StpUtil.getLoginId());
        }
        StpUtil.logout();
        return new RespResult(ResultState.SUCCESS);
    }
}

