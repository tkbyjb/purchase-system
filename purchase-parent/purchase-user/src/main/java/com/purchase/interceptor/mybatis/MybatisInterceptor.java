package com.purchase.interceptor.mybatis;

import com.alibaba.druid.pool.DruidPooledPreparedStatement;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;

//@Component
//@Intercepts({
//        @Signature(type = Executor.class,
//                method = "update",
//                args = {MappedStatement.class, Object.class}),
//        @Signature(type = Executor.class,
//                method = "query",
//                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
//                        CacheKey.class, BoundSql.class}),
//        @Signature(type = Executor.class,
//                method = "query",
//                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
//        @Signature(method = "query", type = StatementHandler.class, args = {Statement.class, ResultHandler.class})
//})
public class MybatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        System.out.println(invocation.getTarget().getClass());
        DruidPooledPreparedStatement ms = (DruidPooledPreparedStatement) args[0];
        System.out.println(ms.getSql());
        System.out.println("定时发送东方红士大夫");
        return ms.getSql()+" where state !=0";
    }
}
