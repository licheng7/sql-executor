package com.orange.sqlexecutor.common;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/22
 * Time:下午10:54
 **/
public class SqlInfo {

    private String type;

    private String mode;

    private String sql;

    public SqlInfo(String type, String mode, String sql) {
        this.type = type;
        this.mode = mode;
        this.sql = sql;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
