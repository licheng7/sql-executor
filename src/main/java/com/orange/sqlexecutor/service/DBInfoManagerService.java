package com.orange.sqlexecutor.service;

import com.google.common.collect.Lists;
import com.orange.sqlexecutor.dao.DBInfoMapper;
import com.orange.sqlexecutor.entity.DBInfo;
import com.orange.sqlexecutor.util.cache.GuavaCacheUtil;
import com.orange.sqlexecutor.util.eventbus.EventBusUtil;
import com.orange.sqlexecutor.util.eventbus.RegistryDataSourceEventInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/11
 * Time:下午8:44
 **/
@Component
public class DBInfoManagerService {

    static Logger logger = LoggerFactory.getLogger(DBInfoManagerService.class);

    @Autowired
    private DBInfoMapper dBInfoMapper;

    @Autowired
    private EventBusUtil eventBusUtil;

    @Autowired
    private SqlExecutorService sqlExecutorService;

    /**
     * 项目启动时初始化数据连接
     */
    public void initDBLinks() {
        List<DBInfo> dbInfoList = selectAll();
        List<DBInfo> cahceDBInfoList = Lists.newArrayList();
        for(DBInfo dbInfo : dbInfoList) {
            try {
                sqlExecutorService.testConnect(dbInfo);
                this.asyncCreateDBLink(dbInfo);
                dbInfo.setStatus(1);
            } catch (Exception e) {
                logger.error("未能与["+dbInfo.getDbName()+"]建立连接");
            }
            // 把数据连接加入到可用连接缓存
            cahceDBInfoList.add(dbInfo);
        }
        GuavaCacheUtil.set("db_info", cahceDBInfoList);
    }

    /**
     * 增加连接信息，并异步初始化连接动态注入spring
     * @param record
     * @return
     */
    public boolean insert(DBInfo record) throws SQLException {
        try {
            sqlExecutorService.testConnect(record);
        } catch (Exception e) {
            return false;
        }
        if(dBInfoMapper.insert(record) == 1) {
            this.asyncCreateDBLink(record);
            List<DBInfo> dbInfoCahceList = GuavaCacheUtil.get("db_info", List.class);
            record.setStatus(1);
            dbInfoCahceList.add(record);
            return true;
        }
        return false;
    }

    /**
     * 异步注册数据库连接
     * @param dbInfo
     */
    private void asyncCreateDBLink(DBInfo dbInfo) {
        RegistryDataSourceEventInfo info = new RegistryDataSourceEventInfo();
        info.setUrl(dbInfo.getDbUrl());
        info.setDriverClassName("org.h2.Driver");
        info.setDbName(dbInfo.getDbName());
        info.setUserName(dbInfo.getUserName());
        info.setPassword(dbInfo.getPassword());
        eventBusUtil.send(info);
    }

    /**
     * 校验数据库连接正确性
     * @return
     */
    private boolean checkDBInfo() {

        return false;
    }

    /**
     * 根据主键查数据库连接信息
     * @param id
     * @return
     */
    public DBInfo selectById(int id) {
        DBInfo info = dBInfoMapper.selectByPrimaryKey(id);
        return info;
    }

    /**
     * 查询全部数据库连接信息
     * @return
     */
    public List<DBInfo> selectAll() {
        return dBInfoMapper.selectAll();
    }

    public List<DBInfo> selectAllByCache() {
        List<DBInfo> dbInfoList = GuavaCacheUtil.get("db_info", List.class);
        return dbInfoList;
    }

    /**
     * 根据主键删除数据库连接信息
     * @param dbId
     * @return
     */
    public boolean delDbById(int dbId) {
        if(dBInfoMapper.deleteByPrimaryKey(dbId) == 1) {
            // 清理缓存，重新加载
            GuavaCacheUtil.clear();
            this.initDBLinks();
            return true;
        }
        return false;
    }
}
