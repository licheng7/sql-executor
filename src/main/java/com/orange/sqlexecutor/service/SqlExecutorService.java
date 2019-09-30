package com.orange.sqlexecutor.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.google.common.collect.Lists;
import com.orange.sqlexecutor.common.SqlInfo;
import com.orange.sqlexecutor.entity.DBInfo;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/11
 * Time:下午4:05
 **/
@Component
public class SqlExecutorService {

    static Logger logger = LoggerFactory.getLogger(SqlExecutorService.class);

    public boolean check(String sql) {
        return sql.endsWith(";");
    }

    public List<String> sqlSplit(String sql) {
        String[] sqls = sql.split(";");
        return Arrays.asList(sqls);
    }

    public void testConnect(DBInfo dbInfo) throws Exception {
        SqlSessionFactory sqlSessionFactory = this.getSqlSessionFactoryByDBInfo(dbInfo);
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = sqlSession.getConnection();
            pst = conn.prepareStatement("select 1;");
            pst.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (null != pst) {
                pst.close();
            }
            if (null != conn) {
                conn.close();
            }
        }
    }

    public SqlSessionFactory getSqlSessionFactoryByDBInfo(DBInfo info) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(this.getDataSource(info));
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:mybatis_mapper.mapping/*.xml"));
            bean.setConfigLocation(resolver.getResources("classpath*:mybatis_config/*.xml")[0]);
        }
        catch(Throwable t) {
            throw new RuntimeException("数据源加载失败,读取配置文件发生异常", t);
        }
        try {
            return bean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("实例化SqlSessionFactory失败", e);
        }
    }

    private DataSource getDataSource(DBInfo info) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(info.getDbUrl());
        dataSource.setUsername(info.getUserName());
        dataSource.setPassword(info.getPassword());
        dataSource.setDriverClassName(info.getDbType());
        dataSource.setConnectionErrorRetryAttempts(0);
        dataSource.setBreakAfterAcquireFailure(true);
        dataSource.setTestWhileIdle(false);
        dataSource.setTimeBetweenConnectErrorMillis(10);
        return dataSource;
    }

    public String execute(SqlSessionFactory sqlSessionFactory, SqlInfo sqlInfo) throws SQLException {
        StringBuilder result = new StringBuilder();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            conn = sqlSession.getConnection();
            pst = conn.prepareStatement(sqlInfo.getSql());
            pst.execute();

            if(pst.getUpdateCount() <= 0) {
                resultSet = pst.getResultSet();
                if(null != resultSet) {
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                        result.append(resultSetMetaData.getColumnName(i) + " | ");
                    }
                    while (resultSet.next()) {
                        result.append("\n");
                        result.append("\t\t\t\t\t");
                        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                            result.append(resultSet.getObject(i) + " | ");
                        }
                    }
                }
            } else {
                result.append("影响行数:");
                result.append(pst.getUpdateCount());
            }
            return result.toString();
        } finally {
            if(null != resultSet) {
                resultSet.close();
            }
            if(null != pst) {
                pst.close();
            }
            if(null != conn) {
                conn.close();
            }
        }
    }

    public List<SqlInfo> analysisSQLs(List<String> sqls) {
        List<SqlInfo> list = Lists.newArrayList();
        for(String sql : sqls) {
            String _sql = sql.trim().toUpperCase();
            if(_sql.startsWith("USEDB ")) {
                list.add(new SqlInfo("COMMAND", "CHANGEDB", _sql));
            }
            else {
                list.add(new SqlInfo("SQL", "", _sql));
            }
        }

        return list;
    }

    public void analysisCommand(SqlInfo sqlInfo, Map<String, DBInfo> dbInfoMap) {
        String _command = sqlInfo.getSql();
        String dbName = _command.substring(6).trim();
        if(dbName.isEmpty()) {
            throw new RuntimeException("数据库切换命令错误：未指定数据库名!");
        }
        if(!dbInfoMap.keySet().contains(dbName)) {
            throw new RuntimeException("数据库切换命令错误：未知的数据库!");
        }
    }

    public void analysisSQL(SqlInfo sqlInfo) {
        SQLStatementParser parser = new MySqlStatementParser(sqlInfo.getSql());
        SQLStatement statement = parser.parseStatement();
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statement.accept(visitor);
        sqlInfo.setMode(String.valueOf(visitor.getTableStat(visitor.getCurrentTable())).toUpperCase());
    }


    /*public static void main(String [] args) {
        //String sql = "select * from DDD.abc where id in (select id from DDD.hhh where name='gaga');";
        //String sql = "insert into FFF.ggg values(1,2,3);";
        //String sql = "ALTER TABLE table_name ADD column_name datatype;";
        String sql = "use hhh;";
        new SqlExecutorService().analysisSQL(sql);
    }*/
}
