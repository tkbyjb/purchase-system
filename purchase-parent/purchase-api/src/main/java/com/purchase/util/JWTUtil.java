package com.purchase.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.purchase.dao.po.User;
import java.util.Date;

/**
 * token工具类
 */
public class JWTUtil {
    //token头部,存在前端头部的字段名,本身不参与生成token
    public static final String AUTH_HEADER_KEY = "Authorization";
    //token前缀
    public static final String TOKEN_PREFIX = "Purchase";
    //签名密钥,随机字符串
    public static final String SECRET_KEY = "q3t68w9z$C&FTu)J@NcQfjTjWnZr4&$u71x";
    //有效期
    public static final long EXPIRATION_TIME = 1000L * 60 * 60 * 4;

    public static String generateToken(User user) {
        //生成token
        return TOKEN_PREFIX + JWT.create()
                .withClaim("typ", "JWT")
                .withClaim("alg", "HS256")
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public static String verifyToken(String token) throws  Exception {
        //验证token
        try {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();
        } catch (TokenExpiredException e) {
            throw new Exception("token失效", e);
        } catch (JWTVerificationException e) {
            throw new Exception("token错误", e);
        }
    }
}
