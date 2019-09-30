package com.orange.sqlexecutor.request;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/21
 * Time:上午12:01
 **/
public class DBInfoRequest {

    private String dbName;

    private String dbUrl;

    private String dbUsername;

    private String dbPassword;

    private String driverClassName;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Override
    public String toString() {
        return "DBInfoRequest{" +
                "dbName='" + dbName + '\'' +
                ", dbUrl='" + dbUrl + '\'' +
                ", dbUsername='" + dbUsername + '\'' +
                ", dbPassword='" + dbPassword + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                '}';
    }
}
