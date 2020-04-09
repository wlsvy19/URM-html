package com.ism.urm.vo.process;

public class ProcessStaticsDay {

    int align = 0;
    String processDate = "";
    String interfaceId = "";
    int totalCount = 0;
    int simCount = 0;
    int successCount = 0;
    int failCount = 0;
    String processTime = "";

    public int getSimCount() {
        return simCount;
    }
    public void setSimCount(int simCount) {
        this.simCount = simCount;
    }
    public int getAlign() {
        return align;
    }
    public void setAlign(int align) {
        this.align = align;
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
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public int getSuccessCount() {
        return successCount;
    }
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
    public int getFailCount() {
        return failCount;
    }
    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }
    public String getProcessTime() {
        return processTime;
    }
    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

}
