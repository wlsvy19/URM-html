package com.ism.urm.vo.rule.mapping;

public class DataMapValue {
    String mapId;
    int valSeq;
    String fieldId;
    String defaultValue;

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public void setValSeq(int valSeq) {
        this.valSeq = valSeq;
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
