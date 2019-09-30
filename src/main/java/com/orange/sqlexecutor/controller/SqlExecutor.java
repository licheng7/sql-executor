package com.orange.sqlexecutor.controller;

import com.orange.sqlexecutor.entity.DBInfo;
import com.orange.sqlexecutor.request.DBInfoRequest;
import com.orange.sqlexecutor.service.DBInfoManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/10
 * Time:上午11:01
 **/
@Controller
public class SqlExecutor {

    Logger logger = LoggerFactory.getLogger(SqlExecutor.class);

    @Autowired
    private DBInfoManagerService dbInfoManagerService;

    @RequestMapping(value = "index")
    public String index(ModelMap map, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户 " + request.getSession().getId() + " 访问首页");
        Map<String, Object> params = new HashMap<>();
        params.put("session_id", request.getSession().getId());
        map.addAttribute("params", params);
        return "freemarker/index";
    }

    @RequestMapping(value = "db_list")
    @ResponseBody
    public List<DBInfo> getDBList(HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户 " + request.getSession().getId() + " 请求获取数据源列表");
        return dbInfoManagerService.selectAllByCache();
    }

    @RequestMapping(value = "del_db")
    @ResponseBody
    public boolean delDbById(String dbId, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户 " + request.getSession().getId() + " 请求删除数据源，dbId=" + dbId);
        return dbInfoManagerService.delDbById(Integer.valueOf(dbId));
    }

    @RequestMapping(value = "add_db")
    @ResponseBody
    public boolean addDB(DBInfoRequest dbInfoRequest, HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户 " + request.getSession().getId() + " 请求新增数据源，dbId=" + dbInfoRequest.toString());
        DBInfo dbInfo = new DBInfo();
        dbInfo.setDbName(dbInfoRequest.getDbName());
        dbInfo.setDbUrl(dbInfoRequest.getDbUrl());
        dbInfo.setUserName(dbInfoRequest.getDbUsername());
        dbInfo.setPassword(dbInfoRequest.getDbPassword());
        dbInfo.setDbType(dbInfoRequest.getDriverClassName());
        try {
            if( dbInfoManagerService.insert(dbInfo)) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }
}
