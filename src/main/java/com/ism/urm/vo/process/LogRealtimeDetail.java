package com.ism.urm.vo.process;

public class LogRealtimeDetail {

    long serialNumber = 0;
    String processDate = "";
    String stepIndex = "";
    int processSeq = 0;
    String interfaceId = "";
    String serverName = "";
    String modelId = "";
    String appId = "";
    String startTime = "";
    String endTime = "";
    String processTime = "";
    String errMessage = "";
    int serverIndex = 0;
    String header = "";

    public String getProcessDate() {
        return processDate;
    }
    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }
    public long getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }
    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }
    public String getStepIndex() {
        return stepIndex;
    }
    public void setStepIndex(String stepIndex) {
        this.stepIndex = stepIndex;
    }
    public int getProcessSeq() {
        return processSeq;
    }
    public void setProcessSeq(int processSeq) {
        this.processSeq = processSeq;
    }
    public String getInterfaceId() {
        return interfaceId;
    }
    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public String getModelId() {
        return modelId;
    }
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getProcessTime() {
        return processTime;
    }
    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }
    public String getErrMessage() {
        return errMessage;
    }
    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
    public int getServerIndex() {
        return serverIndex;
    }
    public void setServerIndex(int serverIndex) {
        this.serverIndex = serverIndex;
    }

}
