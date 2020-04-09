package com.ism.urm.vo.process;

public class LogBatchDetail {

    String batchId = "";
    String modelName = "";
    String appId = "";
    String processContent = "";
    String result = "";
    String processTime = "";
    String errorContents = "";

    public String getBatchId() {
        return batchId;
    }
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getProcessContent() {
        return processContent;
    }
    public void setProcessContent(String processContent) {
        this.processContent = processContent;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getProcessTime() {
        return processTime;
    }
    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }
    public String getErrorContents() {
        return errorContents;
    }
    public void setErrorContents(String errorContents) {
        this.errorContents = errorContents;
    }

}
