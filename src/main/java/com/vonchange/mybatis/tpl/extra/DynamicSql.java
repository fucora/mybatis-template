package com.vonchange.mybatis.tpl.extra;

import com.vonchange.mybatis.tpl.clazz.ClazzUtils;
import com.vonchange.mybatis.tpl.exception.MyRuntimeException;
import com.vonchange.mybatis.tpl.model.EntityField;
import com.vonchange.mybatis.tpl.model.EntityInfo;
import com.vonchange.mybatis.tpl.EntityUtil;
import com.vonchange.mybatis.tpl.map.HashMap;
import com.vonchange.mybatis.tpl.sql.SqlCommentUtil;
import jodd.bean.BeanUtil;
import jodd.bean.BeanUtilBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author 冯昌义
 */
public class DynamicSql {
    private DynamicSql() {
        throw new IllegalStateException("Utility class");
    }

    private static Logger logger = LoggerFactory.getLogger(DynamicSql.class);
    private static Map<String, String> conditionMap = new HashMap<String, String>()
            .set("eq", "=").set("neq", "&lt;&gt;").set("gt", "&gt;").set("gte", "&gt;=").set("lt", "&lt;").set("lte", "&lt;=")
            .set("in", "in").set("like", "like").set("like5", "like").set("5like5", "like")
            .set("5like", "like");
    private static Map<String, String> conditionAliasMap = new HashMap<String, String>()
            .set("eq", "eq").set("neq", "neq").set("gt", "gt").set("gte", "gte").set("lt", "lt").set("lte", "lte")
            .set("=", "eq").set("<>", "neq").set("!=", "neq").set(">", "gt").set(">=", "gte").set("<", "lt").set("<=", "lte")
            .set("in", "in").set("like", "like").set("like5", "like5").set("5like", "5like").set("5like5", "5like5");

    public static String dynamicSql(String sql, Map<String, Object> param) {
        String dialog = SqlCommentUtil.getDialect(sql);
        if (!sql.contains("{@")) {
            return sql;
        }
        String startSym = "{@";
        String endSym = "}";
        int len = sql.length();
        int startLen = startSym.length();
        int endLen = endSym.length();
        int i = 0;
        StringBuilder newSql = new StringBuilder();
        String model = null;
        while (i < len) {
            int ndx = sql.indexOf(startSym, i);
            if (ndx == -1) {
                newSql.append(i == 0 ? sql : sql.substring(i));
                break;
            }
            newSql.append(sql.substring(i, ndx));
            ndx += startLen;
            //newSql
            int ndx2 = sql.indexOf(endSym, ndx);
            if (ndx2 == -1) {
                throw new IllegalArgumentException("无结尾 } 符号 at: " + (ndx - startLen));
            }
            model = sql.substring(ndx, ndx2);
            newSql.append(new StringBuffer(getModel(model, dialog, param)));
            i = ndx2 + endLen;

        }
        logger.debug("自定义语言\n{}", newSql);
        return newSql.toString();
    }

    private static String getModel(String model, String dialog, Map<String, Object> param) {
        model = model.trim();
        model = model.replaceAll("[\\s]+", " ");
        String[] strs = model.split(" ");
        List<String> resultList = new ArrayList<>();
        for (String str : strs) {
            resultList.add(str.trim());
        }
        //set
        if (resultList.size() == 2) {
            if (resultList.get(0).equals("eq")) {
                return getSetSql(resultList, param);
            }
            if (resultList.get(0).equals("key")) {
                return getSaveSql(resultList, param,0);
            }
            if (resultList.get(0).equals("value")) {
                return getSaveSql(resultList, param,1);
            }
        }
        //save
        if (resultList.size() == 4) {
            Map<String, String> allowConditonMap = analyeAllowCondition(resultList.get(2));
            AnalyeNamed analyeNamed = analyeNamed(resultList, allowConditonMap, param);
            return workNamed(analyeNamed, dialog);
        }
        return "";
    }

    private static String getSetSql(List<String> resultList, Map<String, Object> param) {
        if (resultList.get(1).equals("this")) {
            return getSetSqlFromMap(param);
        }
        String named = resultList.get(1);
        Object value = getValue(param, named);
        EntityUtil.initEntityInfo(value.getClass());
        String entityName = value.getClass().getSimpleName();
        EntityInfo entityInfo = EntityUtil.entityMap.get(entityName);
        StringBuilder sb = new StringBuilder("<trim suffixOverrides=\",\">");
        for (Map.Entry<String, EntityField> entry : entityInfo.getFieldMap().entrySet()) {
            EntityField entityField = entry.getValue();
            if (entityField.getIsColumn() && !entityField.getIsId()) {
                sb.append(format("<if test=\"null!={0}\">{1} = #'{'{2}'}',</if>",
                        named + "." + entityField.getFieldName(), entityField.getColumnName(), named + "." + entityField.getFieldName()));
            }
        }
        sb.append("</trim>");
        return sb.toString();
    }
    private static String getSaveSql(List<String> resultList, Map<String, Object> param,int type) {
        String pattern="{0},";
        String patternx="<if test=\"null!={0}\">{1}',</if>";
        if(type==1){
            pattern="#'{'{0}'}',";
            patternx="<if test=\"null!={0}\">#'{'{1}'}',</if>";
        }
        if (resultList.get(1).equals("this")) {
            return getSaveSqlFromMap(pattern,param);
        }
        String named = resultList.get(1);
        Object value = getValue(param, named);
        EntityUtil.initEntityInfo(value.getClass());
        String entityName = value.getClass().getSimpleName();
        EntityInfo entityInfo = EntityUtil.entityMap.get(entityName);
        StringBuilder sb = new StringBuilder("<trim suffixOverrides=\",\">");
        String one;
        for (Map.Entry<String, EntityField> entry : entityInfo.getFieldMap().entrySet()) {
            EntityField entityField = entry.getValue();
            if (entityField.getIsColumn() && !entityField.getIsId()) {
                 one=entityField.getColumnName();
                if(type==1){
                    one=named + "." +entityField.getFieldName();
                }
                sb.append(format(patternx,
                        named + "." + entityField.getFieldName(), one));
            }
        }
        sb.append("</trim>");
        return sb.toString();
    }
    private static String getSaveSqlFromMap(String pattern,Map<String, Object> param) {
        StringBuilder sb = new StringBuilder(" ");
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if (null != entry.getValue()) {
                sb.append(format(pattern, entry.getKey()));
            }
        }
        return sb.substring(0, sb.length() - 1) + " ";
    }
    private static String getSetSqlFromMap(Map<String, Object> param) {
        StringBuilder sb = new StringBuilder(" ");
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            if (null != entry.getValue()) {
                sb.append(format("{0}=#'{'{1}'}',", entry.getKey(), entry.getKey()));
            }
        }
        return sb.substring(0, sb.length() - 1) + " ";
    }

    private static Map<String, String> analyeAllowCondition(String condition) {
        Map<String, String> resultMap = new HashMap<>();
        String[] conditions = condition.split(",");
        for (String cond : conditions) {
            if (null == conditionAliasMap.get(cond.trim())) {
                throw new MyRuntimeException(cond + "不能识别");
            }
            resultMap.put(conditionAliasMap.get(cond.trim()), conditionMap.get(conditionAliasMap.get(cond.trim())));
        }
        if (conditions.length == 2 && resultMap.size() == 1) {//"=".equals(condition)
            return conditionMap;
        }
        return resultMap;
    }

    private static AnalyeNamed analyeNamed(List<String> resultList, Map<String, String> allowConditonMap, Map<String, Object> param) {
        String four = resultList.get(3);
        AnalyeNamed analyeNamed = new AnalyeNamed();
        analyeNamed.setNeedIf(true);
        String link = resultList.get(0);
        if (link.startsWith("1")) {
            analyeNamed.setNeedIf(false);
            link=link.substring(1,link.length());
        }
        String pa = four;
        String[] paramStrs = pa.split(":");
        List<String> strList = new ArrayList<>(Arrays.asList(paramStrs));
        if (strList.size() == 1) {
            strList.add("");
        } else {
            strList.set(1, "." + strList.get(1));
        }
         /*  if (".1".equals(strList.get(1))) {
            analyeNamed.setNeedIf(false);
        }*/
        String named = four.trim();
        String conditon = resultList.get(2);
        String namedFull = named;
        String likeType = "like";
        if (!param.containsKey(named)) {
            for (Map.Entry<String, String> entry : allowConditonMap.entrySet()) {
                String namedKey = named + "_" + entry.getKey();
                if (param.containsKey(namedKey)) {
                    conditon = entry.getValue();
                    namedFull = namedKey;
                    if (entry.getKey().contains("like")) {
                        likeType = entry.getKey();
                    }
                    break;
                }
            }
        }
        if(conditon.contains(",") && allowConditonMap.size() > 1&&namedFull.equals(named)){
            conditon = "=";
        }
        /*if (conditon.contains(",") && allowConditonMap.size() > 1) {
            throw new MyRuntimeException("非法传入条件参数！");
        }*/
        //conditon.contains(",") &&
        if (allowConditonMap.size() == 1) {
            for (Map.Entry<String, String> entry : allowConditonMap.entrySet()) {
                conditon = entry.getValue();
            }
        }
        Object value = getValue(param, namedFull);
        analyeNamed.setValue(value);
        analyeNamed.setType(getValueType(value));
        analyeNamed.setNamedFull(namedFull);
        analyeNamed.setLikeType(likeType);
        analyeNamed.setCondition(conditon);
        analyeNamed.setItemProperty(strList.get(1));
        analyeNamed.setLink(link);
        analyeNamed.setColumn(resultList.get(1));
        return analyeNamed;
    }

    private static Object getValue(Map<String, Object> param, String named) {
        BeanUtil beanUtil = new BeanUtilBean();
        Object value;
        try {
            value = beanUtil.getProperty(param, named);
        } catch (Exception e) {
            return null;
        }
        return value;
    }

    private static String getValueType(Object value) {
        if (null == value) {
            return "base";
        }
        if (value instanceof String) {
            return "string";
        }
        if (Collection.class.isAssignableFrom(value.getClass())) {
            return "list";
        }
        if (value instanceof Object[]) {
            return "arr";
        }
        if (Map.class.isAssignableFrom(value.getClass())) {
            return "map";
        }
        if (ClazzUtils.isBaseType(value.getClass())) {
            return "base";
        }
        return "base";
    }

    private static String workNamed(AnalyeNamed analyeNamed, String dialog) {
        String named = format("#'{'{0}'}'", analyeNamed.getNamedFull());
        if ("in".equals(analyeNamed.getCondition())) {
            named = in(analyeNamed.getNamedFull(), analyeNamed.getItemProperty());
        }
        if ("like".equals(analyeNamed.getCondition())) {
            named = like(analyeNamed.getNamedFull(), analyeNamed.getLikeType(), analyeNamed.getValue(), dialog);
        }
        String content = format(" {0} {1} {2} {3} ", analyeNamed.getLink(), analyeNamed.getColumn(), analyeNamed.getCondition(), named);
        if (!analyeNamed.getNeedIf()) {
            return content;
        }
        String ifStr = format("<if test=\"null!={0}\">", analyeNamed.getNamedFull());
        String type = analyeNamed.getType();
        if ("string".equals(type)) {
            ifStr = format("<if test=\"null!={0} and ''''!={0}\">", analyeNamed.getNamedFull());
        }
        if ("list".equals(type)) {
            ifStr = format("<if test=\"null!={0} and {0}.size>0\">", analyeNamed.getNamedFull());
        }
        if ("arr".equals(type)) {
            ifStr = format("<if test=\"null!={0} and {0}.length>0\">", analyeNamed.getNamedFull());
        }
        return format("{0} {1} </if>", ifStr, content);
    }

    private static String like(String named, String likeType, Object value, String dialog) {
        String str = "CONCAT(''%'',#'{'{0}'}',''%'') ";
        if (dialog.equals(SqlCommentUtil.Dialect.ORACLE)) {
            str = "''%''||#'{'{0}'}'||''%''";
        }
        if (dialog.equals(SqlCommentUtil.Dialect.BASE)) {
            str = "''%$'{'{0}'}'%''";
        }
        // String str="CONCAT(''%'',#'{'{0}'}',''%'') ";
        if ("like".equals(likeType) || "5like5".equals(likeType)) {
            return format(str, named);
        }
        str = " CONCAT(#'{'{0}'}',''%'') ";
        if (dialog.equals(SqlCommentUtil.Dialect.ORACLE)) {
            str = "#'{'{0}'}'||''%''";
        }
        if (dialog.equals(SqlCommentUtil.Dialect.BASE)) {
            str = "''$'{'{0}'}'%''";
        }
        if ("like5".equals(likeType)) {
            return format(str, named);
        }
        str = "CONCAT(''%'',#'{'{0}'}') ";
        if (dialog.equals(SqlCommentUtil.Dialect.ORACLE)) {
            str = "''%''||#'{'{0}'}'";
        }
        if (dialog.equals(SqlCommentUtil.Dialect.BASE)) {
            str = "''%$'{'{0}'}'";
        }
        if ("5like".equals(likeType)) {
            return format(str, named);
        }
        return format("#'{'{0}'}'", named);
    }

    public static String in(String named, String itemProperty) {
        String str = "<foreach collection=\"{0}\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">  \n" +
                "            #'{'item{1}'}'" +
                "    </foreach>";
        return format(str, named, itemProperty);
    }

    public static String format(String pattern, Object... arguments) {
        MessageFormat temp = new MessageFormat(pattern);
        return temp.format(arguments);
    }
}
