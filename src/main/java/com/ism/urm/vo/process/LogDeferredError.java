package com.ism.urm.vo.process;

public class LogDeferredError {

    String id = "";
    String interfaceId = "";
    String processDate = "";

    String messageTime = "";
    String appId = "";
    String errorContents = "";
    String errorDataContents = "";

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getInterfaceId() {
        return interfaceId;
    }
    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }
    public String getProcessDate() {
        return processDate;
    }
    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }
    public String getMessageTime() {
        return messageTime;
    }
    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getErrorContents() {
        return errorContents;
    }
    public void setErrorContents(String errorContents) {
        this.errorContents = errorContents;
    }
    public String getErrorDataContents() {
        return errorDataContents;
    }
    public void setErrDataContents(String errorDataContents) {
        this.errorDataContents = errorDataContents;
    }

}

