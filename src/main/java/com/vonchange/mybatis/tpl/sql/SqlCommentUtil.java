package com.vonchange.mybatis.tpl.sql;



/**
 *
 * Created by 冯昌义 on 2018/4/19.
 */
public class SqlCommentUtil {
    public  static  class Dialect{
        public static final  String MYSQL="mysql";
        public static final  String ORACLE="oracle";
        public static final  String BASE="base";
    }
    public static    String  getDialect(String sql){
        if(sql.contains("@mysql")){
            return Dialect.MYSQL;
        }
        if(sql.contains("@oracle")){
            return Dialect.ORACLE;
        }
        if(sql.contains("@base")){
            return Dialect.BASE;
        }
        return   Dialect.MYSQL;
    }
}
