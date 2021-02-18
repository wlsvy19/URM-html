package com.ism.urm.vo.process;

public class LogBatch {

    String batchId= "";
    String interfaceId = "";
    String status = "";
    long totalCount = 0;
    long successCount = 0;
    long failCount = 0;
    String startTime = "";
    String endTime = "";
    String sendFilePath = "";
    String sendFileName = "";
    String rcvFilePath = "";
    String rcvFileName = "";

    public String getBatchId() {
        return batchId;
    }
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
    public String getInterfaceId() {
        return interfaceId;
    }
    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
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
    public String getSendFilePath() {
        return sendFilePath;
    }
    public void setSendFilePath(String sendFilePath) {
        this.sendFilePath = sendFilePath;
    }
    public String getSendFileName() {
        return sendFileName;
    }
    public void setSendFileName(String sendFileName) {
        this.sendFileName = sendFileName;
    }
    public String getRcvFilePath() {
        return rcvFilePath;
    }
    public void setRcvFilePath(String rcvFilePath) {
        this.rcvFilePath = rcvFilePath;
    }
    public String getRcvFileName() {
        return rcvFileName;
    }
    public void setRcvFileName(String rcvFileName) {
        this.rcvFileName = rcvFileName;
    }

}
