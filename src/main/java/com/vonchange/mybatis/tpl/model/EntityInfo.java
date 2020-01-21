package com.vonchange.mybatis.tpl.model;

import java.util.Map;

/**
 *  实体信息
 * @author von_change@163.com
 * @date 2015-6-14 下午12:44:06
 */
public class EntityInfo {
	private String entityName;
	private String tableName;
	private String idFieldName;
	private String idColumnName;
	private String idType;
	private Map<String, EntityField> fieldMap;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getIdFieldName() {
		return idFieldName;
	}

	public void setIdFieldName(String idFieldName) {
		this.idFieldName = idFieldName;
	}

	public String getIdColumnName() {
		return idColumnName;
	}

	public void setIdColumnName(String idColumnName) {
		this.idColumnName = idColumnName;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public Map<String, EntityField> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, EntityField> fieldMap) {
		this.fieldMap = fieldMap;
	}

}
