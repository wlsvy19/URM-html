package com.ism.urm.vo.rule.request;

import java.io.Serializable;

public class RequestHistory extends Request implements Serializable {

    private static final long serialVersionUID = 1L;

    String chgSeq;

    public RequestHistory() {
        // TODO Auto-generated constructor stub
    }

    public RequestHistory(Request req) {
        setId(req.getId());
        setName(req.getName());
        setRegId(req.getRegId());
        setRegDate(req.getRegDate());
        setChgId(req.getChgId());
        setChgDate(req.getChgDate());
        jobType = req.jobType;
        processStat = req.processStat;
        chgStat = req.chgStat;
        syncType = req.syncType;
        eaiYN = req.eaiYN;
        tpcYN = req.tpcYN;
        trType = req.trType;

        interfaceId = req.interfaceId;
        interfaceType = req.interfaceType;

        sendJobCodeId = req.sendJobCodeId;
        sendSystemId = req.sendSystemId;
        sendSystemType = req.sendSystemType;
        sendPeriod = req.sendPeriod;
        sendServerName = req.sendServerName;
        sendDbInfo = req.sendDbInfo;
        sendAdminId = req.sendAdminId;

        rcvJobCodeId = req.rcvJobCodeId;
        rcvSystemId = req.rcvSystemId;
        rcvSystemType = req.rcvSystemType;
        rcvServerName = req.rcvServerName;
        rcvDbInfo = req.rcvDbInfo;
        rcvAdminId = req.rcvAdminId;

        testStartYMD = req.testStartYMD;
        testEndYMD = req.testEndYMD;
        etcRemark = req.etcRemark;
        eaiRemark = req.eaiRemark;

        dataMapYN = req.dataMapYN;
        reqDataMappingId = req.reqDataMappingId;
        resDataMappingId = req.resDataMappingId;
//        reqSendDataId = req.reqSendDataId;
//        reqRcvDataId = req.reqRcvDataId;
//        resSendDataId = req.resSendDataId;
//        resRcvDataId = req.resRcvDataId;

        sql = req.sql;
        sqlPlain = req.sqlPlain;
        dbCrudType = req.dbCrudType;
        fileCrudType = req.fileCrudType;
        infFileName = req.infFileName;

        dcnt = req.dcnt;
        openYMD = req.openYMD;
        delYN = req.delYN;
    }


    public String getChgSeq() {
        return chgSeq;
    }
    public void setChgSeq(String chgSeq) {
        this.chgSeq = chgSeq;
    }

}
