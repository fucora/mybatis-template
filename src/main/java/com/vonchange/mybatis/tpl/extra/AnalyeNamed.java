package com.vonchange.mybatis.tpl.extra;

/**
 * @author 冯昌义
 * @brief
 * @details
 * @date 2018/1/9.
 */
public class AnalyeNamed {
    private String condition;
    private Boolean needIf;
    private String type;
    //private  Object value;
    /**
     * 1 String  2 其他 base 3:list 4.arr
     */
    //private String type;
    private String likeType;
    private  String namedFull;
    private String itemProperty;
    private  String link;
    private Object value;
    private  String column;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean getNeedIf() {
        return needIf;
    }

    public void setNeedIf(Boolean needIf) {
        this.needIf = needIf;
    }


    public String getLikeType() {
        return likeType;
    }

    public void setLikeType(String likeType) {
        this.likeType = likeType;
    }

    public String getNamedFull() {
        return namedFull;
    }

    public void setNamedFull(String namedFull) {
        this.namedFull = namedFull;
    }

    public String getItemProperty() {
        return itemProperty;
    }

    public void setItemProperty(String itemProperty) {
        this.itemProperty = itemProperty;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
