package com.ism.urm.vo.rule.mapping;

import java.io.Serializable;

public class DataMapValue implements Serializable {

    private static final long serialVersionUID = 1L;

    String mapId;
    int valueSeq;
    String fieldId;
    String defaultValue;

    public String getMapId() {
        return mapId;
    }
    public void setMapId(String mapId) {
        this.mapId = mapId;
    }
    public int getValueSeq() {
        return valueSeq;
    }
    public void setValueSeq(int valueSeq) {
        this.valueSeq = valueSeq;
    }
    public String getFieldId() {
        return fieldId;
    }
    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }
    public String getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
