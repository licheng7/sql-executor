package com.orange.sqlexecutor.controller;

import com.google.common.collect.Maps;
import com.orange.sqlexecutor.common.SqlInfo;
import com.orange.sqlexecutor.entity.DBInfo;
import com.orange.sqlexecutor.service.DBInfoManagerService;
import com.orange.sqlexecutor.service.SqlExecutorService;
import com.orange.sqlexecutor.util.spring.util.ContextUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/10
 * Time:下午3:38
 **/
@ServerEndpoint(value = "/websocket")
@Controller
public class WebSocketServer {

    static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private static SqlExecutorService sqlExecutorService;

    @Autowired
    public void setSqlExecutorService(SqlExecutorService sqlExecutorService) {
        WebSocketServer.sqlExecutorService = sqlExecutorService;
    }

    private static DBInfoManagerService dbInfoManagerService;

    @Autowired
    public void setDBInfoManagerService(DBInfoManagerService dbInfoManagerService) {
        WebSocketServer.dbInfoManagerService = dbInfoManagerService;
    }

    private static Map<String, WebSocketServer> webSocketMap = new HashMap<>();

    private Session session;

    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss:SSS");

    static final String PIXSQL = "@#$&&send!@#!!!";

    SqlSessionFactory sqlSessionFactory;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        logger.info("websocket建立连接，sessionId="+session.getId());
        webSocketMap.put(session.getId(), this);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketMap.remove(session.getId());
        logger.info("websocket连接关闭，sessionId="+session.getId());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message) {
        this.accept();

        Map<String, String> messageMap = this.checkPix(message);

        String command = messageMap.get("COMMAND");

        if(PIXSQL.equals(command)) {
            // 获取sql语句集合
            String sql = messageMap.get("INFO");
            // 检查语句是否以";"结尾
            if(!sqlExecutorService.check(sql)) {
                this.sendMessage("sql不合法，检查语句是否以';'结尾。");
                return;
            }
            // 以";"对sql做切割
            List<String> sqls = sqlExecutorService.sqlSplit(sql);
            // sql语句初筛，把切换数据库命令语句和具体执行语句分隔开
            List<SqlInfo> sqlInfoList = sqlExecutorService.analysisSQLs(sqls);
            // 查询已配置的数据库连接列表
            List<DBInfo> dbList = dbInfoManagerService.selectAll();
            if(dbList.isEmpty()) {
                this.sendMessage("错误：未配置数据库连接信息!");
                return;
            }
            Map<String, DBInfo> dbInfoMap = Maps.newHashMap();
            dbList.forEach((dbInfo) -> {
                dbInfoMap.put(dbInfo.getDbName().toUpperCase(), dbInfo);
            });
            // 便利sql语句，逐条分析
            for(SqlInfo sqlInfo : sqlInfoList) {
                if(sqlInfo.getType().equals("COMMAND")) {
                    try {
                        sqlExecutorService.analysisCommand(sqlInfo, dbInfoMap);
                    } catch(Exception e) {
                        this.sendMessage("sql不合法，检查语句["+sqlInfo.getSql()+"]语法，"+e.getMessage());
                        return;
                    }
                }
                else if(sqlInfo.getType().equals("SQL")) {
                    try {
                        sqlExecutorService.analysisSQL(sqlInfo);
                    } catch(Exception e) {
                        this.sendMessage("sql不合法，检查语句["+sqlInfo.getSql()+"]语法。");
                        return;
                    }
                }
            }
            // 循环执行sqlMap中的语句
            for(SqlInfo sqlInfo : sqlInfoList) {
                if(sqlInfo.getType().equals("COMMAND")) {
                    String _command = sqlInfo.getSql();
                    String dbName = _command.substring(6).trim();
                    DBInfo dbInfo = dbInfoMap.get(dbName);
                    String beanName = "SQLSESSIONFACTORY-"+dbInfo.getDbName().toUpperCase();
                    sqlSessionFactory = (DefaultSqlSessionFactory) ContextUtils.getBean(beanName);
                    this.sendMessage("切换数据源，当前使用["+dbName+"]");
                }
                else if(sqlInfo.getType().equals("SQL")) {
                    if(null == sqlSessionFactory) {
                        this.sendMessage("未指定数据连接！！！");
                        return;
                    }
                    try {
                        String result = sqlExecutorService.execute(sqlSessionFactory, sqlInfo);
                        this.sendMessage(result);
                    } catch (Exception e) {
                        this.sendMessage("sql执行失败，sql=["+sqlInfo.getSql()+"]");
                    }
                }
            }

        }
    }

    private Map<String, String> checkPix(String message) {
        Map<String, String> map = Maps.newHashMap();
        map.put("COMMAND", message.substring(0, 15));
        map.put("INFO", message.substring(15));
        return map;
    }

    private void accept() {
        this.sendMessage("!!!@@@##clear##@@@!!!", false);
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) {
        sendMessage(message, true);
    }

    public void sendMessage(String message, boolean printTime) {
        StringBuilder _message = new StringBuilder(message);
        if(printTime) {
            _message.insert(0, "["+sdf.format(new Date())+"]  ");
        }
        try {
            this.session.getBasicRemote().sendText(_message.toString());
        } catch (IOException e) {
            logger.error("发送消息失败，session="+session.getId()+"    message="+_message);
        }
    }

}
