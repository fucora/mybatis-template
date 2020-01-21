package com.vonchange.mybatis.tpl.model;


/**
 * 实体字段信息
 * @author von_change@163.com
 * @date 2015-6-14 下午12:44:21
 */
public class EntityField {
	private String fieldName;
	private String columnName;
	private String typeName;
	private Boolean isBaseType=false;
	private Boolean isColumn=false;
	private Boolean isId=false;
	private  String function;
	private Boolean ignoreUpdate;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Boolean getIsBaseType() {
		return isBaseType;
	}
	public void setIsBaseType(Boolean isBaseType) {
		this.isBaseType = isBaseType;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Boolean getIsId() {
		return isId;
	}
	public void setIsId(Boolean isId) {
		this.isId = isId;
	}
	public Boolean getIsColumn() {
		return isColumn;
	}
	public void setIsColumn(Boolean isColumn) {
		this.isColumn = isColumn;
	}

	public Boolean getIgnoreUpdate() {
		return ignoreUpdate;
	}

	public void setIgnoreUpdate(Boolean ignoreUpdate) {
		this.ignoreUpdate = ignoreUpdate;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}
}
