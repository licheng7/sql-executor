package com.orange.sqlexecutor.util.dalinterceptor;


import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * 实现mybatis提供的Interceptor, 可以在sql执行前进行拦截
 * Created by licheng on 2018/11/14.
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
        Object.class, RowBounds.class, ResultHandler.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey
        .class, BoundSql.class})})
public class DalLogInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        long start = System.nanoTime();
        Object returnValue = null;
        try {
            returnValue = invocation.proceed();
        }
        catch (Exception e) {
            throw new RuntimeException("我是sql执行拦截器,我出异常了", e);
        }
        finally {
            long duration = (System.nanoTime() - start);
            //System.out.println("我是sql语句打印小助手,我负责打印sql语句:    1>="+mappedStatement.getId()+"    2>="+duration+"    3>="+returnValue+"    4>="+getParam(invocation));
        }

        return returnValue;
    }

    private String getParam(Invocation invocation) {
        Object[] list = invocation.getArgs();
        if (list != null && list.length > 1 && list[1] != null) {
            return list[1].toString();
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
