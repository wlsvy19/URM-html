package com.ism.urm.vo.process;

public class LogDeferred {

    String id = "";
    String processDate = "";
    String interfaceId = "";
    String startTime = "";
    String endTime = "";
    long totalCount = 0;
    long successCount = 0;
    long failCount = 0;
    String appId = "";
    String stepIndex = "";

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getProcessDate() {
        return processDate;
    }
    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }
    public String getInterfaceId() {
        return interfaceId;
    }
    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
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
    public long getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
    public long getSuccessCount() {
        return successCount;
    }
    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }
    public long getFailCount() {
        return failCount;
    }
    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getStepIndex() {
        return stepIndex;
    }
    public void setStepIndex(String stepIndex) {
        this.stepIndex = stepIndex;
    }

}

