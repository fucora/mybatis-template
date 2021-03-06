package com.vonchange.mybatis.tpl;

import com.vonchange.mybatis.tpl.annotation.ColumnNot;


import com.vonchange.mybatis.tpl.annotation.UpdateNotNull;
import com.vonchange.mybatis.tpl.model.EntityField;
import com.vonchange.mybatis.tpl.model.EntityInfo;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Created by 冯昌义 on 2018/4/19.
 */
public class EntityUtil {
    public static final Map<String, EntityInfo> entityMap = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(EntityUtil.class);
    public static void initEntityInfo(Class<?> clazz) {
        String entityName = clazz.getSimpleName();
        if(null==entityMap.get(entityName)){
            initEntity(clazz, entityName);
        }
    }

    private static  void initEntity(Class<?> clazz, String entityName) {
        logger.debug("初始化{0}", entityName);
        EntityInfo entity = new EntityInfo();
        entity.setEntityName(entityName);
        Table table=clazz.getAnnotation(Table.class);
        String tableName=null;
        if(null!=table){
            tableName=table.name();
        }
        if(StringUtil.isBlank(tableName)){
            tableName= OrmUtil.toSql(entityName);
        }
        entity.setTableName(tableName);
        Field[] fileds = clazz.getDeclaredFields();// 只有本类
        Map<String, EntityField> entityFieldMap = new LinkedHashMap<>();
        Column column=null;
        for (Field field : fileds) {
            EntityField entityField = new EntityField();
            String fieldName = field.getName();
            entityField.setFieldName(fieldName);
            column=field.getAnnotation(Column.class);
            String columnName =null;
            if(null!=column){
                columnName=column.name();
            }
            if(StringUtil.isBlank(columnName)){
                columnName = OrmUtil.toSql(fieldName);
            }
            Class<?> type = field.getType();
            entityField.setColumnName(columnName);
            entityField.setTypeName(type.getSimpleName());
            Boolean isBaseType = ClassUtils.isBaseType(type);
            entityField.setIsBaseType(isBaseType);
            if (isBaseType) {
                entityField.setIsColumn(true);
            }
            entityField.setUpdateNotNull(false);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Id) {
                    entityField.setIsId(true);
                    entity.setIdFieldName(fieldName);
                    entity.setIdColumnName(columnName);
                    entity.setIdType(type.getSimpleName());
                    continue;
                }
                if (annotation instanceof ColumnNot) {
                    entityField.setIsColumn(false);
                    continue;
                }
                if (annotation instanceof UpdateNotNull) {
                    entityField.setUpdateNotNull(true);
                }
            }
            entityFieldMap.put(fieldName, entityField);
        }
        entity.setFieldMap(entityFieldMap);
        entityMap.put(entityName, entity);
    }
}
