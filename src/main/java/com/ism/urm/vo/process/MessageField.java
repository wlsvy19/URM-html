package com.ism.urm.vo.process;

public class MessageField {

    int messageIndex = 0;
    String fieldName = "";
    String fieldValue = "";

    public int getMessageIndex() {
        return messageIndex;
    }
    public void setMessageIndex(int messageIndex) {
        this.messageIndex = messageIndex;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getFieldValue() {
        return fieldValue;
    }
    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

}
