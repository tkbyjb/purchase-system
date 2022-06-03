package com.purchase.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.purchase.dao.po.Supplier;
import com.purchase.global.DefaultData;
import com.purchase.global.RespResult;
import com.purchase.global.ResultState;
import com.purchase.global.state.State;
import com.purchase.service.MenuService;
import com.purchase.service.SupplierAccountService;
import com.purchase.vo.LoginVo;
import com.purchase.vo.param.LoginParam;
import com.purchase.vo.param.SupplierRegisterParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.Arrays;

@RestController
@ApiOperation("供应商账号相关控制器")
public class SupplierAccountController {
    @Resource
    private SupplierAccountService supplierAccountService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private DefaultData defaultData;
    @Resource
    private MenuService menuService;

    @PostMapping("/public/supplier/register")
    @ApiOperation("供应商注册")
    public RespResult registerSupplier(@RequestBody @Valid SupplierRegisterParam registerParam) throws Exception {
        /*
         * 1.邮箱是否已注册
         * 2.用户名是否已注册,供应商名是否已注册
         * 3.检查验证码
         * 4.写入数据库
         */
        if (supplierAccountService.isExistEmail(registerParam.getEmail())) {
            return new RespResult(ResultState.EXIST_EMAIL);
        }
        if (supplierAccountService.isExistLoginname(registerParam.getLoginname())) {
            return new RespResult(ResultState.EXIST_USERNAME);
        }
        if (supplierAccountService.isExistSuppliername(registerParam.getSuppliername())) {
            return new RespResult(ResultState.EXIST_SUPPLIERNAME);
        }
        String verifyCode = (String) redisTemplate.opsForValue().get("supplierRegister_" + registerParam.getEmail());
        if (!verifyCode.equals(registerParam.getVerifyCode())) {
            return new RespResult(ResultState.VERIFYCOE_ERROR);
        }
        if (supplierAccountService.register(registerParam)) {
            return new RespResult(ResultState.SUCCESS, "注册成功,请等待管理员审核,审核结果会邮件通知", null);
        } else {
            throw new Exception();
        }
    }

    @GetMapping("/public/verifyCode/register/supplier")
    @ApiOperation("获取供应商注册验证码")
    public RespResult getVerifyCodeSupplier(@Email String email) {
        /*
         * 1.邮箱是否已注册
         * 2.生成验证码
         * 3.验证码存入redis
         * 4.验证码发至邮箱
         */
        if (supplierAccountService.isExistEmail(email)) {
            return new RespResult(ResultState.EXIST_EMAIL);
        }
        supplierAccountService.sendRegisterVerifyCode(email);
        return new RespResult(ResultState.SUCCESS, "验证码发送成功", null);
    }

    @PostMapping("/public/supplier/login")
    @ApiOperation("供应商登录")
    public RespResult userLoginSupplier(@RequestBody @Valid LoginParam loginParam) {
        /*
         * 1.用户是否已登录+redis此用户输入尝试是否>5次
         * 2.是否有此用户+账号state可登录+密码是否正确
         * 3.生成token,user放入redis
         * 4.返回用户名+用户头像+token
         */
        Supplier user = supplierAccountService.login(loginParam);
        if (user == null) {
            return new RespResult(ResultState.LOGIN_FAIL, "登录失败,用户名或密码错误", null);
        }
        if (!Arrays.asList(State.SUPPLIER_CAN_LOGIN).contains(user.getState())) {
            return new RespResult(ResultState.LOGIN_FAIL, "登录失败,账号未审核或审核不通过", null);
        }
        StpUtil.login(user.getSupplierId());
        String token = StpUtil.getTokenValue();
        redisTemplate.opsForValue().set("supplierId_" + user.getSupplierId(), user);
        LoginVo loginVo = new LoginVo();
        if (!user.getAvatar().equals(defaultData.AVATAR_URL)) {
            loginVo.setAvatar(defaultData.AVATAR_ACCESS_BASE_URL + "/" + user.getAvatar());
        } else {
            loginVo.setAvatar(user.getAvatar());
        }
        loginVo.setToken(token);
        loginVo.setUsername(user.getLoginname());
        loginVo.setMenuVos(menuService.getRoleMenuVo(7l, null));//供应商直接写死了,因为供应商没有角色字段
        loginVo.setType(1);
        loginVo.setId(user.getSupplierId());
        return new RespResult(ResultState.SUCCESS, "登录成功", loginVo);
    }

}
