package com.ism.urm.vo.rule;

public class Request extends RuleVo {

    String jobType;
    String interfaceId;
    String interfaceType;

    String sendJobCodeId;
    String sendSystemId;
//    String sndSystemName;
    String sendSystemType;
//    String sndMethodNm;
    String sendPeriod;
//    String sndPeriodNm;
    String sendServerName;
    String sendDbInfo;
    String sendAdminId;

    String rcvJobCodeId;
    String rcvSystemId;
//    String rcvHostNm;
    String rcvSystemType;
//    String rcvMethodNm;
    String rcvServerName;
    String rcvDbInfo;
    String rcvAdminId;

    String processStat;
    String chgStat;
    String trType;
    String syncType;
    String syncTypeNm;
    boolean tpcYN;
    String dcnt;
    String testStartYMD;
    String testEndYMD;
    String openYMD;

    boolean dataMapYN;
    String reqDataMappingId;
//    String reqDataMapNm;
    String reqSendDataId;
    String reqRcvDataId;
    String resDataMappingId;
//    String resDataMapNm;
    String resSendDataId;
    String resRcvDataId;

    String etcRemark;
    boolean eaiYN;
    String sql;
    String sqlPlain;
    String dbCrudType;
    String dbCrudTypeNm;
    String fileCrudType;
    String fileCrudTypeNm;
    String ifFileNm;
    String eaiRemark;

    String delYN;
    AppSystem sendSystem;
    AppSystem rcvSystem;

//  BusinessCode sndJobCode;
//  BusinessCode rcvJobCode;
//    User regAdm;
//    User sndAdm;
//    User rcvAdm;

    public String getJobType() {
        return jobType;
    }
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
    public String getInterfaceId() {
        return interfaceId;
    }
    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }
    public String getInterfaceType() {
        return interfaceType;
    }
    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }
    public String getSendJobCodeId() {
        return sendJobCodeId;
    }
    public void setSendJobCodeId(String sendJobCodeId) {
        this.sendJobCodeId = sendJobCodeId;
    }
    public String getSendSystemId() {
        return sendSystemId;
    }
    public void setSendSystemId(String sendSystemId) {
        this.sendSystemId = sendSystemId;
    }
    public String getSendSystemType() {
        return sendSystemType;
    }
    public void setSendSystemType(String sendSystemType) {
        this.sendSystemType = sendSystemType;
    }
    public String getSendPeriod() {
        return sendPeriod;
    }
    public void setSendPeriod(String sendPeriod) {
        this.sendPeriod = sendPeriod;
    }
    public String getSendServerName() {
        return sendServerName;
    }
    public void setSendServerName(String sendServerName) {
        this.sendServerName = sendServerName;
    }
    public String getSendDbInfo() {
        return sendDbInfo;
    }
    public void setSendDbInfo(String sendDbInfo) {
        this.sendDbInfo = sendDbInfo;
    }
    public String getSendAdminId() {
        return sendAdminId;
    }
    public void setSendAdminId(String sendAdminId) {
        this.sendAdminId = sendAdminId;
    }
    public String getRcvJobCodeId() {
        return rcvJobCodeId;
    }
    public void setRcvJobCodeId(String rcvJobCodeId) {
        this.rcvJobCodeId = rcvJobCodeId;
    }
    public String getRcvSystemId() {
        return rcvSystemId;
    }
    public void setRcvSystemId(String rcvSystemId) {
        this.rcvSystemId = rcvSystemId;
    }
    public String getRcvSystemType() {
        return rcvSystemType;
    }
    public void setRcvSystemType(String rcvSystemType) {
        this.rcvSystemType = rcvSystemType;
    }
    public String getRcvServerName() {
        return rcvServerName;
    }
    public void setRcvServerName(String rcvServerName) {
        this.rcvServerName = rcvServerName;
    }
    public String getRcvDbInfo() {
        return rcvDbInfo;
    }
    public void setRcvDbInfo(String rcvDbInfo) {
        this.rcvDbInfo = rcvDbInfo;
    }
    public String getRcvAdminId() {
        return rcvAdminId;
    }
    public void setRcvAdminId(String rcvAdminId) {
        this.rcvAdminId = rcvAdminId;
    }
    public String getProcessStat() {
        return processStat;
    }
    public void setProcessStat(String processStat) {
        this.processStat = processStat;
    }
    public String getChgStat() {
        return chgStat;
    }
    public void setChgStat(String chgStat) {
        this.chgStat = chgStat;
    }
    public String getTrType() {
        return trType;
    }
    public void setTrType(String trType) {
        this.trType = trType;
    }
    public String getSyncType() {
        return syncType;
    }
    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }
    public String getSyncTypeNm() {
        return syncTypeNm;
    }
    public void setSyncTypeNm(String syncTypeNm) {
        this.syncTypeNm = syncTypeNm;
    }
    public boolean isTpcYN() {
        return tpcYN;
    }
    public void setTpcYN(boolean tpcYN) {
        this.tpcYN = tpcYN;
    }
    public String getDcnt() {
        return dcnt;
    }
    public void setDcnt(String dcnt) {
        this.dcnt = dcnt;
    }
    public String getTestStartYMD() {
        return testStartYMD;
    }
    public void setTestStartYMD(String testStartYMD) {
        this.testStartYMD = testStartYMD;
    }
    public String getTestEndYMD() {
        return testEndYMD;
    }
    public void setTestEndYMD(String testEndYMD) {
        this.testEndYMD = testEndYMD;
    }
    public String getOpenYMD() {
        return openYMD;
    }
    public void setOpenYMD(String openYMD) {
        this.openYMD = openYMD;
    }
    public boolean getDataMapYN() {
        return dataMapYN;
    }
    public void setDataMapYN(boolean dataMapYN) {
        this.dataMapYN = dataMapYN;
    }
    public String getReqDataMappingId() {
        return reqDataMappingId;
    }
    public void setReqDataMappingId(String reqDataMappingId) {
        this.reqDataMappingId = reqDataMappingId;
    }
    public String getReqSendDataId() {
        return reqSendDataId;
    }
    public void setReqSendDataId(String reqSendDataId) {
        this.reqSendDataId = reqSendDataId;
    }
    public String getReqRcvDataId() {
        return reqRcvDataId;
    }
    public void setReqRcvDataId(String reqRcvDataId) {
        this.reqRcvDataId = reqRcvDataId;
    }
    public String getResDataMappingId() {
        return resDataMappingId;
    }
    public void setResDataMappingId(String resDataMappingId) {
        this.resDataMappingId = resDataMappingId;
    }
    public String getResSendDataId() {
        return resSendDataId;
    }
    public void setResSendDataId(String resSendDataId) {
        this.resSendDataId = resSendDataId;
    }
    public String getResRcvDataId() {
        return resRcvDataId;
    }
    public void setResRcvDataId(String resRcvDataId) {
        this.resRcvDataId = resRcvDataId;
    }
    public String getEtcRemark() {
        return etcRemark;
    }
    public void setEtcRemark(String etcRemark) {
        this.etcRemark = etcRemark;
    }
    public boolean isEaiYN() {
        return eaiYN;
    }
    public void setEaiYN(boolean eaiYN) {
        this.eaiYN = eaiYN;
    }
    public String getSql() {
        return sql;
    }
    public void setSql(String sql) {
        this.sql = sql;
    }
    public String getSqlPlain() {
        return sqlPlain;
    }
    public void setSqlPlain(String sqlPlain) {
        this.sqlPlain = sqlPlain;
    }
    public String getDbCrudType() {
        return dbCrudType;
    }
    public void setDbCrudType(String dbCrudType) {
        this.dbCrudType = dbCrudType;
    }
    public String getDbCrudTypeNm() {
        return dbCrudTypeNm;
    }
    public void setDbCrudTypeNm(String dbCrudTypeNm) {
        this.dbCrudTypeNm = dbCrudTypeNm;
    }
    public String getFileCrudType() {
        return fileCrudType;
    }
    public void setFileCrudType(String fileCrudType) {
        this.fileCrudType = fileCrudType;
    }
    public String getFileCrudTypeNm() {
        return fileCrudTypeNm;
    }
    public void setFileCrudTypeNm(String fileCrudTypeNm) {
        this.fileCrudTypeNm = fileCrudTypeNm;
    }
    public String getIfFileNm() {
        return ifFileNm;
    }
    public void setIfFileNm(String ifFileNm) {
        this.ifFileNm = ifFileNm;
    }
    public String getEaiRemark() {
        return eaiRemark;
    }
    public void setEaiRemark(String eaiRemark) {
        this.eaiRemark = eaiRemark;
    }
    public String getDelYN() {
        return delYN;
    }
    public void setDelYN(String delYN) {
        this.delYN = delYN;
    }
    public AppSystem getSendSystem() {
        return sendSystem;
    }
    public void setSendSystem(AppSystem sendSystem) {
        this.sendSystem = sendSystem;
    }
    public AppSystem getRcvSystem() {
        return rcvSystem;
    }
    public void setRcvSystem(AppSystem rcvSystem) {
        this.rcvSystem = rcvSystem;
    }

}
