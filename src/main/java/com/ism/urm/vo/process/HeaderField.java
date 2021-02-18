package com.ism.urm.vo.process;

public class HeaderField {

    int fieldIndex = 0;
    String fieldName = "";
    int fieldLength = 0;

    public int getFieldIndex() {
        return fieldIndex;
    }
    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public int getFieldLength() {
        return fieldLength;
    }
    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

}
