package com.vonchange.mybatis.tpl.model;

import java.util.Arrays;

/**
 * @author 冯昌义
 * @brief
 * @details
 * @date 2017/12/12.
 */
public class SqlWithParam {
    private String sql;
    private Object[] params;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "SqlWithParam{" +
                "sql='" + sql + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
