package com.orange.sqlexecutor.util.eventbus;

import com.google.common.eventbus.Subscribe;
import com.orange.sqlexecutor.entity.DBInfo;
import com.orange.sqlexecutor.service.SqlExecutorService;
import com.orange.sqlexecutor.util.spring.util.ContextUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/21
 * Time:下午5:18
 **/
@Component
public class RegistryDataSourceListenerImpl implements EventBusListener<RegistryDataSourceEventInfo> {

    Logger logger = LoggerFactory.getLogger(RegistryDataSourceListenerImpl.class);

    static final String PIXBEANNAME = "SQLSESSIONFACTORY-";

    @Autowired
    private EventBusUtil eventBusUtil;

    @Autowired
    private SqlExecutorService sqlExecutorService;

    @Override
    @Subscribe
    public void listener(RegistryDataSourceEventInfo info) {
        try {
            this.registrySqlSessionFactory(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SqlSessionFactory registrySqlSessionFactory(RegistryDataSourceEventInfo info) throws IOException {
        DBInfo dbInfo = new DBInfo();
        dbInfo.setDbUrl(info.getUrl());
        dbInfo.setDbType(info.getDriverClassName());
        dbInfo.setUserName(info.getUserName());
        dbInfo.setPassword(info.getPassword());
        SqlSessionFactory sqlSessionFactory = sqlExecutorService.getSqlSessionFactoryByDBInfo(dbInfo);
        try {
            return ContextUtils.registryBean(PIXBEANNAME+info.getDbName().toUpperCase(), DefaultSqlSessionFactory.class, sqlSessionFactory);
        }
        catch (Exception e) {
            throw new RuntimeException("实例化SqlSessionFactory异常，dbName="+info.getDbName(), e);
        }
    }

    @Override
    public void registry() {
        eventBusUtil.register(this);
    }

}
