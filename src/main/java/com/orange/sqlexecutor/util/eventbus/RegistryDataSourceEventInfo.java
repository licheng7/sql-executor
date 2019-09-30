package com.orange.sqlexecutor.util.eventbus;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/21
 * Time:下午5:20
 **/
public class RegistryDataSourceEventInfo extends EventInfo {

    private String dbName;

    private String url;

    private String userName;

    private String password;

    private String driverClassName;


    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
