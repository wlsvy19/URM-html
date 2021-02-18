package com.ism.urm.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.ism.urm.vo.process.HeaderField;
import com.ism.urm.vo.process.LogBatch;
import com.ism.urm.vo.process.LogBatchDetail;
import com.ism.urm.vo.process.LogDeferred;
import com.ism.urm.vo.process.LogDeferredError;
import com.ism.urm.vo.process.LogRealtime;
import com.ism.urm.vo.process.LogRealtimeDetail;
import com.ism.urm.vo.process.ProcessStaticsDay;
import com.ism.urm.vo.process.ProcessStaticsHour;

public class ProcessDao {

    public ProcessDao() {
        super();
    }
    
//    public List<DateInterfaceError> getErrorList(String value) {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("realtimeTable", "LOG_REALTIME" + value.substring(6));
//        params.put("prcDate", value);
//        return getSqlMapClientTemplate().queryForList("DateInterfaceErrorList.get", params);
//    }
//    
//    
//    public List<TableSpace> getTableSpace() {
//        return getSqlMapClientTemplate().queryForList("TableSpace.get");
//    }
    
    
//    public List<TotalProcessCount> getTotalProcessCount(Map<String, String> params) {
//        return getSqlMapClientTemplate().queryForList("TotalProcessCount.get", params);
//    }
//    
//    public List<ProcessCount> getInterfaceProcessCount() {
//        return getSqlMapClientTemplate().queryForList("InterfaceProcessCount.get");
//    }
//    
//    public List<InterfaceError> getInterfaceError(String realtimeLogTable) {
//        return getSqlMapClientTemplate().queryForList("InterfaceErrorList.get", realtimeLogTable);
//    }
    
    public List<LogBatch> getLogBatch(Session session, Map<String, String> params) {
        List<LogBatch> rtn = new ArrayList<>();;
        String sql = "SELECT" + 
                "   BATCHID batchId, " + 
                "   INTEGRATIONSERVICEID ruleId, " + 
                "   ISJOBFINISHED status, " + 
                "   TOTALCOUNT totalCount," + 
                "   SUCCESSCOUNT successCount," + 
                "   FAILURECOUNT failcount," + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(STARTTIME , 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) startTime," + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(ENDTIME , 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) endTime," + 
                "   SENDFILEPATH sndFilePath," + 
                "   SENDFILENAME sndFileName," + 
                "   RECVFILEPATH rcvFilePath," + 
                "   RECVFILENAME rcvFileName " + 
                " FROM LOG_BATCH" + 
                " WHERE PROCESSDATE = :processDate" + 
                "   AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "   AND BATCHID like DECODE(:batchId, NULL,'%%', '%' || :batchId || '%')" + 
                "   AND ( ISJOBFINISHED like DECODE(:result, 'T','%%', 'E') OR ISJOBFINISHED IS NULL )" + 
                "   AND SUBSTR(STARTTIME, 9, 4) BETWEEN :startTime AND :endTime " + 
                " ORDER BY startTime desc";
        List<Object[]> list = session.createSQLQuery(sql)
                .setString("processDate", params.get("processDate"))
                .setString("interfaceId", params.get("interfaceId"))
                .setString("batchId", params.get("batchId"))
                .setString("result", params.get("result"))
                .setString("startTime", params.get("startTime"))
                .setString("endTime", params.get("endTime"))
                .list();
        for (Object[] o : list) {
            LogBatch lb = new LogBatch();
            lb.setBatchId((String) o[0]);
            lb.setInterfaceId((String) o[1]);
            lb.setStatus((String) o[2]);
            lb.setTotalCount(((BigDecimal)o[3]).intValue());
            lb.setSuccessCount(((BigDecimal)o[4]).intValue());
            lb.setFailCount(((BigDecimal)o[5]).intValue());
            lb.setStartTime((String) o[6]);
            lb.setEndTime((String) o[7]);
            lb.setSendFilePath((String) o[8]);
            lb.setSendFileName((String) o[9]);
            lb.setRcvFilePath((String) o[10]);
            lb.setRcvFileName((String) o[11]);
            rtn.add(lb);
        }
        return rtn;
    }
    
    public List<LogBatchDetail> getLogBatchDetail(Session session, String batchId) {
        List<LogBatchDetail> rtn = new ArrayList<>();;
        String sql = "SELECT " + 
                "   MODELCOMPONENTNAME modelName," + 
                "   APPID appId," + 
                "   MODELPROCESSCONTENT processContent," + 
                "   RESULTCODE result," + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(PROCESSMESSAGETIME , 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) processTime," + 
                "   ERRORCONTENTS errorContent" + 
                " FROM LOGBATCHDETAIL" + 
                " WHERE BATCHID = :batchId" + 
                " ORDER BY PROCESSMESSAGETIME DESC";
        List<Object[]> list = session.createSQLQuery(sql)
                .setString("batchId", batchId)
                .list();
        for (Object[] o : list) {
            LogBatchDetail lbd = new LogBatchDetail();
            lbd.setModelName((String) o[0]);
            lbd.setAppId((String) o[1]);
            lbd.setProcessContent((String) o[2]);
            lbd.setResult((String) o[3]);
            lbd.setProcessTime((String) o[4]);
            lbd.setErrorContents((String) o[5]);
            rtn.add(lbd);
        }
        return rtn;
    }
    
    public List<LogDeferred> getLogDeferred(Session session, Map<String, String> params) {
        List<LogDeferred> rtn = new ArrayList<>();;
        String sql = "SELECT" + 
                "   PROCESSDATE processDate," + 
                "   INTEGRATIONSERVICEID interfaceId, " + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(MIN(STARTTIME) , 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) startTime," + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(MAX(ENDTIME) , 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) endTime," + 
                "   SUM(TOTALCOUNT) totalCount," + 
                "   SUM(SUCCESSCOUNT) successCount," + 
                "   SUM(FAILURECOUNT) failCount" + 
                " FROM LOG_DEFERRED" + 
                " WHERE PROCESSDATE = :processDate" + 
                "   AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "   AND SUBSTR(STARTTIME, 9, 4) BETWEEN :startTime AND :endTime " + 
                "   AND STEPINDEX <> 'R'" + 
                " GROUP BY PROCESSDATE, INTEGRATIONSERVICEID";
        List<Object[]> list = session.createSQLQuery(sql)
                .setString("processDate", params.get("processDate"))
                .setString("interfaceId", params.get("interfaceId"))
                .setString("startTime", params.get("startTime"))
                .setString("endTime", params.get("endTime"))
                .list();
        for (Object[] o : list) {
            LogDeferred ld = new LogDeferred();
            ld.setProcessDate((String) o[0]);
            ld.setInterfaceId((String) o[1]);
            ld.setStartTime((String) o[2]);
            ld.setEndTime((String) o[3]);
            ld.setTotalCount(((BigDecimal)o[4]).intValue());
            ld.setSuccessCount(((BigDecimal)o[5]).intValue());
            ld.setFailCount(((BigDecimal)o[6]).intValue());
            rtn.add(ld);
        }
        return rtn;
    }
    
    public List<LogDeferred> getLogDeferredDetail(Session session, Map<String, String> params) {
        List<LogDeferred> rtn = new ArrayList<>();;
        String sql = "SELECT" + 
                "   INTEGRATIONSERVICEID interfaceId," + 
                "   APPID appId," + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(ENDTIME , 'YYYYMMDDHH24MISSFF'), 'YYYY-MM-DD'), 0, 12) processDate, " + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(STARTTIME , 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) startTime," + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(ENDTIME , 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) endTime," + 
                "   TOTALCOUNT totalCount," + 
                "   SUCCESSCOUNT successCount," + 
                "   FAILURECOUNT failCount," + 
                "   STEPINDEX stepIndex" + 
                " FROM LOG_DEFERRED " + 
                " WHERE PROCESSDATE = :processDate " + 
                "   AND INTEGRATIONSERVICEID = UPPER(:interfaceId)" + 
                " ORDER BY SERIALNUMBER DESC";
        List<Object[]> list = session.createSQLQuery(sql)
                .setString("processDate", params.get("processDate"))
                .setString("interfaceId", params.get("interfaceId"))
                .list();
        for (Object[] o : list) {
            LogDeferred ld = new LogDeferred();
            ld.setInterfaceId((String) o[0]);
            ld.setAppId((String) o[1]);
            ld.setProcessDate((String) o[2]);
            ld.setStartTime((String) o[3]);
            ld.setEndTime((String) o[4]);
            ld.setTotalCount(((BigDecimal)o[5]).intValue());
            ld.setSuccessCount(((BigDecimal)o[6]).intValue());
            ld.setFailCount(((BigDecimal)o[7]).intValue());
            ld.setStepIndex((String) o[8]);
            rtn.add(ld);
        }
        return rtn;
    }

    public List<LogDeferredError> getLogDeferredError(Session session, Map<String, String> params) {
        List<LogDeferredError> rtn = new ArrayList<>();;
        String sql = "SELECT" + 
                "   INTEGRATIONSERVICEID interfaceId," + 
                "   TO_CHAR(TO_DATE(PROCESSDATE), 'YYYY-MM-DD') processDate ,   " + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(STARTMESSAGETIME, 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) messageTime," + 
                "   APPID appId," + 
                "   ERRORCONTENTS errContents," + 
                "   ERRORDATACONTENT errDataContents" + 
                " FROM LOGDEFERREDERRORDATA" + 
                " WHERE PROCESSDATE = :processDate " + 
                "   AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "   AND SUBSTR(STARTMESSAGETIME, 9, 4) BETWEEN :startTime AND :endTime " + 
                " ORDER BY STARTMESSAGETIME DESC";
        List<Object[]> list = session.createSQLQuery(sql)
                .setString("processDate", params.get("processDate"))
                .setString("interfaceId", params.get("interfaceId"))
                .setString("startTime", params.get("startTime"))
                .setString("endTime", params.get("endTime"))
                .list();
        for (Object[] o : list) {
            LogDeferredError ldr = new LogDeferredError();
            ldr.setInterfaceId((String) o[0]);
            ldr.setProcessDate((String) o[1]);
            ldr.setMessageTime((String) o[2]);
            ldr.setAppId((String) o[3]);
            ldr.setErrorContents((String) o[4]);
            ldr.setErrorDataContents((String) o[5]);
            rtn.add(ldr);
        }
        return rtn;
    }
    
    public List<LogRealtime> getLogRealtime(Session session, Map<String, String> params) {
        List<LogRealtime> rtn = new ArrayList<>();
        String interfaceId = params.get("interfaceId");
        String globalId = params.get("globalId");
        String error = params.get("error");
        String sql1 = "SELECT " + 
                "   PROCESSDATE processDate," + 
                "   INTEGRATIONSERVICEID interfaceId," + 
                "   GLOBALID globalId," + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(MIN(STARTTIME) , 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) startTime," + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(MAX(ENDTIME) , 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) endTime, " + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(MIN(STARTTIME) ,'YYYYMMDDHH24MISSFF') - TO_TIMESTAMP(MAX(ENDTIME),'YYYYMMDDHH24MISSFF')), 12, 12) processTime," + 
                "   DECODE(MAX(ERRORCODE), NULL, 0, MAX(ERRORCODE)) errorCode" + 
                " FROM ";
        String sql2 = " WHERE PROCESSDATE = :processDate" + 
                "   AND STARTTIME BETWEEN :startTime AND :endTime ";
        String sql3 = " GROUP BY PROCESSDATE, INTEGRATIONSERVICEID, GLOBALID" + 
                " ORDER BY startTime desc";
        
        StringBuilder sql = new StringBuilder();
        sql.append(sql1).append(params.get("logTable")).append(sql2);
        if (interfaceId != null && interfaceId.trim().length() > 0) {
            sql.append("   AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')");
        }
        if (globalId != null && globalId.trim().length() > 0) {
            sql.append("   AND GLOBALID like DECODE(:globalId,    NULL,'%%', '%' || :globalId || '%')");
        }
        if ("Y".equals(error)) {
            sql.append("   AND ERRORCODE IS NOT NULL");
        }
        sql.append(sql3);
        Query query = session.createSQLQuery(sql.toString())
                .setString("processDate", params.get("processDate"))
                .setString("startTime", params.get("startTime"))
                .setString("endTime", params.get("endTime"));
        if (interfaceId != null && interfaceId.trim().length() > 0) {
            query.setString("interfaceId", interfaceId);
        }
        if (globalId != null && globalId.trim().length() > 0) {
            query.setString("globalId", globalId);
        }
        
        List<Object[]> list = query.list();
        for (Object[] o : list) {
            LogRealtime lr = new LogRealtime();
            lr.setProcessDate("" + ((BigDecimal)o[0]).intValue());
            lr.setInterfaceId((String) o[1]);
            lr.setGlobalId((String) o[2]);
            lr.setStartTime((String) o[3]);
            lr.setEndTime((String) o[4]);
            lr.setProcessTime((String) o[5]);
            lr.setErrorCode("" + ((BigDecimal)o[0]).intValue());
            rtn.add(lr);
        }
        return rtn;
    }
                                   
    public List<LogRealtimeDetail> getLogRealtimeDetail(Session session, Map<String, String> params) {
        List<LogRealtimeDetail> rtn = new ArrayList<>();
        String sql = "SELECT " + 
                "   SERIALNUMBER            serialNumber," + 
                "   PROCESSDATE             processDate," + 
                "   SUBSTR(STEPINDEX, 2, 2) stepIndex," + 
                "   INTEGRATIONSERVICEID interfaceId," + 
                "   SERVERNAME serverName," + 
                "   MODELID modelId," + 
                "   PROCESSCONTENTS   appId," + 
                "   PROCESSSEQUENCE processSeq," + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(STARTTIME , 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) startTime," + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(ENDTIME , 'YYYYMMDDHH24MISSFF'), 'HH24:MI:SS.FF'), 0, 12) endTime, " + 
                "   SUBSTR(TO_CHAR(TO_TIMESTAMP(STARTTIME ,'YYYYMMDDHH24MISSFF') - TO_TIMESTAMP(ENDTIME,'YYYYMMDDHH24MISSFF')), 12, 12) processTime," + 
                "   ERRORMESSAGE errMessage," + 
                "   SERVERINDEX serverIndex," + 
                "   MESSAGEHEADER header" + 
                " FROM " + params.get("logTable") + 
                " WHERE GLOBALID = :globalId" + 
                "   AND PROCESSDATE = :processDate" + 
                " ORDER BY endTime";
        List<Object[]> list = session.createSQLQuery(sql)
                .setString("processDate", params.get("processDate"))
                .setString("globalId", params.get("globalId"))
                .list();
        for (Object[] o : list) {
            LogRealtimeDetail lrd = new LogRealtimeDetail();
            lrd.setSerialNumber(((BigDecimal)o[0]).longValue());
            lrd.setProcessDate("" + ((BigDecimal)o[0]).intValue());
            lrd.setStepIndex((String) o[2]);
            lrd.setInterfaceId((String) o[3]);
            lrd.setServerName((String) o[4]);
            lrd.setModelId((String) o[5]);
            lrd.setAppId((String) o[6]);
            lrd.setProcessSeq(((BigDecimal)o[7]).intValue());
            lrd.setStartTime((String) o[8]);
            lrd.setEndTime((String) o[9]);
            lrd.setProcessTime((String) o[10]);
            lrd.setErrMessage((String) o[11]);
            lrd.setServerIndex(((BigDecimal)o[12]).intValue());
            lrd.setHeader((String) o[13]);
            rtn.add(lrd);
        }
        return rtn;
    }
    
    public List<ProcessStaticsHour> getProcessStaticsBatchHour(Session session, Map<String, String> params) {
        List<ProcessStaticsHour> rtn = new ArrayList<>();
        String sql = "SELECT " + 
                "   align," + 
                "   processDate," + 
                "   interfaceId," + 
                "   countType," + 
                "   NVL(hour00, 0) hour00," + 
                "   NVL(hour01, 0) hour01," + 
                "   NVL(hour02, 0) hour02," + 
                "   NVL(hour03, 0) hour03," + 
                "   NVL(hour04, 0) hour04," + 
                "   NVL(hour05, 0) hour05," + 
                "   NVL(hour06, 0) hour06," + 
                "   NVL(hour07, 0) hour07," + 
                "   NVL(hour08, 0) hour08," + 
                "   NVL(hour09, 0) hour09," + 
                "   NVL(hour10, 0) hour10," + 
                "   NVL(hour11, 0) hour11," + 
                "   NVL(hour12, 0) hour12," + 
                "   NVL(hour13, 0) hour13," + 
                "   NVL(hour14, 0) hour14," + 
                "   NVL(hour15, 0) hour15," + 
                "   NVL(hour16, 0) hour16," + 
                "   NVL(hour17, 0) hour17," + 
                "   NVL(hour18, 0) hour18," + 
                "   NVL(hour19, 0) hour19," + 
                "   NVL(hour20, 0) hour20," + 
                "   NVL(hour21, 0) hour21," + 
                "   NVL(hour22, 0) hour22," + 
                "   NVL(hour23, 0) hour23" + 
                "        FROM" + 
                "        (" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 101       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 102       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 103       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 104       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "        " + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 201       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 202       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 203       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 204       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "        " + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 301       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 302       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 303       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 304       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSBATCHHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   ORDER BY processDate , align, interfaceId )";
        List<Object[]> list = session.createSQLQuery(sql).setString("interfaceId", params.get("interfaceId"))
                .setString("startDate", params.get("startDate"))
                .setString("endDate", params.get("endDate"))
                .list();
        for (Object[] o : list) {
            ProcessStaticsHour psd = new ProcessStaticsHour();
            psd.setAlign(((BigDecimal)o[0]).intValue());
            psd.setProcessDate((String) o[1]);
            psd.setInterfaceId((String) o[2]);
            psd.setCountType((String) o[3]);
            psd.setHour00(((BigDecimal)o[4]).intValue());
            psd.setHour01(((BigDecimal)o[5]).intValue());
            psd.setHour02(((BigDecimal)o[6]).intValue());
            psd.setHour03(((BigDecimal)o[7]).intValue());
            psd.setHour04(((BigDecimal)o[8]).intValue());
            psd.setHour05(((BigDecimal)o[9]).intValue());
            psd.setHour06(((BigDecimal)o[10]).intValue());
            psd.setHour07(((BigDecimal)o[11]).intValue());
            psd.setHour08(((BigDecimal)o[12]).intValue());
            psd.setHour09(((BigDecimal)o[13]).intValue());
            psd.setHour10(((BigDecimal)o[14]).intValue());
            psd.setHour11(((BigDecimal)o[15]).intValue());
            psd.setHour12(((BigDecimal)o[16]).intValue());
            psd.setHour13(((BigDecimal)o[17]).intValue());
            psd.setHour14(((BigDecimal)o[18]).intValue());
            psd.setHour15(((BigDecimal)o[19]).intValue());
            psd.setHour16(((BigDecimal)o[20]).intValue());
            psd.setHour17(((BigDecimal)o[21]).intValue());
            psd.setHour18(((BigDecimal)o[22]).intValue());
            psd.setHour19(((BigDecimal)o[23]).intValue());
            psd.setHour20(((BigDecimal)o[24]).intValue());
            psd.setHour21(((BigDecimal)o[25]).intValue());
            psd.setHour22(((BigDecimal)o[26]).intValue());
            psd.setHour23(((BigDecimal)o[27]).intValue());
            rtn.add(psd);
        }
        return rtn;
    }

    public List<ProcessStaticsHour> getProcessStaticsRealtimeHour(Session session, Map<String, String> params) {
        List<ProcessStaticsHour> rtn = new ArrayList<>();
        String sql = "SELECT " + 
                "   align," + 
                "   processDate," + 
                "   interfaceId," + 
                "   countType," + 
                "   NVL(hour00, 0) hour00," + 
                "   NVL(hour01, 0) hour01," + 
                "   NVL(hour02, 0) hour02," + 
                "   NVL(hour03, 0) hour03," + 
                "   NVL(hour04, 0) hour04," + 
                "   NVL(hour05, 0) hour05," + 
                "   NVL(hour06, 0) hour06," + 
                "   NVL(hour07, 0) hour07," + 
                "   NVL(hour08, 0) hour08," + 
                "   NVL(hour09, 0) hour09," + 
                "   NVL(hour10, 0) hour10," + 
                "   NVL(hour11, 0) hour11," + 
                "   NVL(hour12, 0) hour12," + 
                "   NVL(hour13, 0) hour13," + 
                "   NVL(hour14, 0) hour14," + 
                "   NVL(hour15, 0) hour15," + 
                "   NVL(hour16, 0) hour16," + 
                "   NVL(hour17, 0) hour17," + 
                "   NVL(hour18, 0) hour18," + 
                "   NVL(hour19, 0) hour19," + 
                "   NVL(hour20, 0) hour20," + 
                "   NVL(hour21, 0) hour21," + 
                "   NVL(hour22, 0) hour22," + 
                "   NVL(hour23, 0) hour23" + 
                "        FROM" + 
                "        (" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 101       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 102       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 103       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 104       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "        " + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 201       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 202       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 203       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 204       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "        " + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 301       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 302       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 303       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 304       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   ORDER BY processDate , align, interfaceId )";
        List<Object[]> list = session.createSQLQuery(sql).setString("interfaceId", params.get("interfaceId"))
                .setString("startDate", params.get("startDate"))
                .setString("endDate", params.get("endDate"))
                .list();
        for (Object[] o : list) {
            ProcessStaticsHour psd = new ProcessStaticsHour();
            psd.setAlign(((BigDecimal)o[0]).intValue());
            psd.setProcessDate((String) o[1]);
            psd.setInterfaceId((String) o[2]);
            psd.setCountType((String) o[3]);
            psd.setHour00(((BigDecimal)o[4]).intValue());
            psd.setHour01(((BigDecimal)o[5]).intValue());
            psd.setHour02(((BigDecimal)o[6]).intValue());
            psd.setHour03(((BigDecimal)o[7]).intValue());
            psd.setHour04(((BigDecimal)o[8]).intValue());
            psd.setHour05(((BigDecimal)o[9]).intValue());
            psd.setHour06(((BigDecimal)o[10]).intValue());
            psd.setHour07(((BigDecimal)o[11]).intValue());
            psd.setHour08(((BigDecimal)o[12]).intValue());
            psd.setHour09(((BigDecimal)o[13]).intValue());
            psd.setHour10(((BigDecimal)o[14]).intValue());
            psd.setHour11(((BigDecimal)o[15]).intValue());
            psd.setHour12(((BigDecimal)o[16]).intValue());
            psd.setHour13(((BigDecimal)o[17]).intValue());
            psd.setHour14(((BigDecimal)o[18]).intValue());
            psd.setHour15(((BigDecimal)o[19]).intValue());
            psd.setHour16(((BigDecimal)o[20]).intValue());
            psd.setHour17(((BigDecimal)o[21]).intValue());
            psd.setHour18(((BigDecimal)o[22]).intValue());
            psd.setHour19(((BigDecimal)o[23]).intValue());
            psd.setHour20(((BigDecimal)o[24]).intValue());
            psd.setHour21(((BigDecimal)o[25]).intValue());
            psd.setHour22(((BigDecimal)o[26]).intValue());
            psd.setHour23(((BigDecimal)o[27]).intValue());
            rtn.add(psd);
        }
        return rtn;
    }

    public List<ProcessStaticsHour> getProcessStaticsRealtimeHourSim(Session session, Map<String, String> params) {
        List<ProcessStaticsHour> rtn = new ArrayList<>();
        String sql = "SELECT " + 
                "   align," + 
                "   processDate," + 
                "   interfaceId," + 
                "   countType," + 
                "   NVL(hour00, 0) hour00," + 
                "   NVL(hour01, 0) hour01," + 
                "   NVL(hour02, 0) hour02," + 
                "   NVL(hour03, 0) hour03," + 
                "   NVL(hour04, 0) hour04," + 
                "   NVL(hour05, 0) hour05," + 
                "   NVL(hour06, 0) hour06," + 
                "   NVL(hour07, 0) hour07," + 
                "   NVL(hour08, 0) hour08," + 
                "   NVL(hour09, 0) hour09," + 
                "   NVL(hour10, 0) hour10," + 
                "   NVL(hour11, 0) hour11," + 
                "   NVL(hour12, 0) hour12," + 
                "   NVL(hour13, 0) hour13," + 
                "   NVL(hour14, 0) hour14," + 
                "   NVL(hour15, 0) hour15," + 
                "   NVL(hour16, 0) hour16," + 
                "   NVL(hour17, 0) hour17," + 
                "   NVL(hour18, 0) hour18," + 
                "   NVL(hour19, 0) hour19," + 
                "   NVL(hour20, 0) hour20," + 
                "   NVL(hour21, 0) hour21," + 
                "   NVL(hour22, 0) hour22," + 
                "   NVL(hour23, 0) hour23" + 
                "        FROM" + 
                "        (" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 101       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   " + 
                "   SELECT " + 
                "       SUBSTR(A.PROCESSTIME, 1, 8) as processDate," + 
                "       B.INTEGRATIONSERVICEID as interfaceId," + 
                "       '시뮬레이션건수' as countType," + 
                "       102     as align," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '00', 1, 0)) hour00," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '01', 1, 0)) hour01," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '02', 1, 0)) hour02," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '03', 1, 0)) hour03," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '04', 1, 0)) hour04," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '05', 1, 0)) hour05," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '06', 1, 0)) hour06," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '07', 1, 0)) hour07," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '08', 1, 0)) hour08," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '09', 1, 0)) hour09," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '10', 1, 0)) hour10," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '11', 1, 0)) hour11," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '12', 1, 0)) hour12," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '13', 1, 0)) hour13," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '14', 1, 0)) hour14," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '15', 1, 0)) hour15," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '16', 1, 0)) hour16," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '17', 1, 0)) hour17," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '18', 1, 0)) hour18," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '19', 1, 0)) hour19," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '20', 1, 0)) hour20," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '21', 1, 0)) hour21," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '22', 1, 0)) hour22," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '23', 1, 0)) hour23" + 
                "   FROM SIMULATIONRESULT A, SIMULATION B" + 
                "   WHERE A.SIMULATIONID = B.SIMULATIONID" + 
                "       AND B.INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "       AND SUBSTR(A.PROCESSTIME, 1, 10) BETWEEN :startDate AND :endDate" + 
                "   GROUP BY SUBSTR(A.PROCESSTIME, 1, 8), B.INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   " + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 103       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 104       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 105       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "        " + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 201       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       SUBSTR(A.PROCESSTIME, 1, 8) as processDate," + 
                "       'SUB-TOTAL' as interfaceId," + 
                "       '시뮬레이션건수' as countType," + 
                "       202     as align," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '00', 1, 0)) hour00," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '01', 1, 0)) hour01," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '02', 1, 0)) hour02," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '03', 1, 0)) hour03," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '04', 1, 0)) hour04," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '05', 1, 0)) hour05," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '06', 1, 0)) hour06," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '07', 1, 0)) hour07," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '08', 1, 0)) hour08," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '09', 1, 0)) hour09," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '10', 1, 0)) hour10," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '11', 1, 0)) hour11," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '12', 1, 0)) hour12," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '13', 1, 0)) hour13," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '14', 1, 0)) hour14," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '15', 1, 0)) hour15," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '16', 1, 0)) hour16," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '17', 1, 0)) hour17," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '18', 1, 0)) hour18," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '19', 1, 0)) hour19," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '20', 1, 0)) hour20," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '21', 1, 0)) hour21," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '22', 1, 0)) hour22," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '23', 1, 0)) hour23" + 
                "   FROM SIMULATIONRESULT A, SIMULATION B" + 
                "   WHERE A.SIMULATIONID = B.SIMULATIONID" + 
                "       AND B.INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "       AND SUBSTR(A.PROCESSTIME, 1, 10) BETWEEN :startDate AND :endDate" + 
                "   GROUP BY SUBSTR(A.PROCESSTIME, 1, 8)" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 203       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 204       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 205       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "        " + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 301       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' as processDate," + 
                "       'TOTAL' as interfaceId," + 
                "       '시뮬레이션건수' as countType," + 
                "       302     as align," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '00', 1, 0)) hour00," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '01', 1, 0)) hour01," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '02', 1, 0)) hour02," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '03', 1, 0)) hour03," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '04', 1, 0)) hour04," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '05', 1, 0)) hour05," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '06', 1, 0)) hour06," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '07', 1, 0)) hour07," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '08', 1, 0)) hour08," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '09', 1, 0)) hour09," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '10', 1, 0)) hour10," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '11', 1, 0)) hour11," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '12', 1, 0)) hour12," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '13', 1, 0)) hour13," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '14', 1, 0)) hour14," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '15', 1, 0)) hour15," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '16', 1, 0)) hour16," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '17', 1, 0)) hour17," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '18', 1, 0)) hour18," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '19', 1, 0)) hour19," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '20', 1, 0)) hour20," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '21', 1, 0)) hour21," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '22', 1, 0)) hour22," + 
                "       SUM(decode(SUBSTR(A.PROCESSTIME, 9, 2), '23', 1, 0)) hour23" + 
                "   FROM SIMULATIONRESULT A, SIMULATION B" + 
                "   WHERE A.SIMULATIONID = B.SIMULATIONID" + 
                "       AND B.INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "       AND SUBSTR(A.PROCESSTIME, 1, 10) BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   " + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 303       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 304       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 305       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSREALTIMEHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   ORDER BY processDate , align, interfaceId )";
        List<Object[]> list = session.createSQLQuery(sql).setString("interfaceId", params.get("interfaceId"))
                .setString("startDate", params.get("startDate"))
                .setString("endDate", params.get("endDate"))
                .list();
        for (Object[] o : list) {
            ProcessStaticsHour psd = new ProcessStaticsHour();
            psd.setAlign(((BigDecimal)o[0]).intValue());
            psd.setProcessDate((String) o[1]);
            psd.setInterfaceId((String) o[2]);
            psd.setCountType((String) o[3]);
            psd.setHour00(((BigDecimal)o[4]).intValue());
            psd.setHour01(((BigDecimal)o[5]).intValue());
            psd.setHour02(((BigDecimal)o[6]).intValue());
            psd.setHour03(((BigDecimal)o[7]).intValue());
            psd.setHour04(((BigDecimal)o[8]).intValue());
            psd.setHour05(((BigDecimal)o[9]).intValue());
            psd.setHour06(((BigDecimal)o[10]).intValue());
            psd.setHour07(((BigDecimal)o[11]).intValue());
            psd.setHour08(((BigDecimal)o[12]).intValue());
            psd.setHour09(((BigDecimal)o[13]).intValue());
            psd.setHour10(((BigDecimal)o[14]).intValue());
            psd.setHour11(((BigDecimal)o[15]).intValue());
            psd.setHour12(((BigDecimal)o[16]).intValue());
            psd.setHour13(((BigDecimal)o[17]).intValue());
            psd.setHour14(((BigDecimal)o[18]).intValue());
            psd.setHour15(((BigDecimal)o[19]).intValue());
            psd.setHour16(((BigDecimal)o[20]).intValue());
            psd.setHour17(((BigDecimal)o[21]).intValue());
            psd.setHour18(((BigDecimal)o[22]).intValue());
            psd.setHour19(((BigDecimal)o[23]).intValue());
            psd.setHour20(((BigDecimal)o[24]).intValue());
            psd.setHour21(((BigDecimal)o[25]).intValue());
            psd.setHour22(((BigDecimal)o[26]).intValue());
            psd.setHour23(((BigDecimal)o[27]).intValue());
            rtn.add(psd);
        }
        return rtn;
    }
    
    public List<ProcessStaticsHour> getProcessStaticsDeferredHour(Session session, Map<String, String> params) {
        List<ProcessStaticsHour> rtn = new ArrayList<>();
        String sql = "SELECT " + 
                "   align," + 
                "   processDate," + 
                "   interfaceId," + 
                "   countType," + 
                "   NVL(hour00, 0) hour00," + 
                "   NVL(hour01, 0) hour01," + 
                "   NVL(hour02, 0) hour02," + 
                "   NVL(hour03, 0) hour03," + 
                "   NVL(hour04, 0) hour04," + 
                "   NVL(hour05, 0) hour05," + 
                "   NVL(hour06, 0) hour06," + 
                "   NVL(hour07, 0) hour07," + 
                "   NVL(hour08, 0) hour08," + 
                "   NVL(hour09, 0) hour09," + 
                "   NVL(hour10, 0) hour10," + 
                "   NVL(hour11, 0) hour11," + 
                "   NVL(hour12, 0) hour12," + 
                "   NVL(hour13, 0) hour13," + 
                "   NVL(hour14, 0) hour14," + 
                "   NVL(hour15, 0) hour15," + 
                "   NVL(hour16, 0) hour16," + 
                "   NVL(hour17, 0) hour17," + 
                "   NVL(hour18, 0) hour18," + 
                "   NVL(hour19, 0) hour19," + 
                "   NVL(hour20, 0) hour20," + 
                "   NVL(hour21, 0) hour21," + 
                "   NVL(hour22, 0) hour22," + 
                "   NVL(hour23, 0) hour23" + 
                "        FROM" + 
                "        (" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 101       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 102       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 103       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,INTEGRATIONSERVICEID interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 104       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE, INTEGRATIONSERVICEID" + 
                "        " + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 201       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 202       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 203       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       PROCESSDATE processDate" + 
                "       ,'SUB-TOTAL' interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 204       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   GROUP BY PROCESSDATE" + 
                "        " + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'총건수' as countType" + 
                "       , 301       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT + FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT + FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT + FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT + FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT + FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT + FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT + FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT + FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT + FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT + FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT + FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT + FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT + FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT + FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT + FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT + FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT + FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT + FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT + FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT + FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT + FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT + FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT + FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT + FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'성공건수' as countType" + 
                "       , 302       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',SUCCESSCOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',SUCCESSCOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',SUCCESSCOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',SUCCESSCOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',SUCCESSCOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',SUCCESSCOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',SUCCESSCOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',SUCCESSCOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',SUCCESSCOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',SUCCESSCOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',SUCCESSCOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',SUCCESSCOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',SUCCESSCOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',SUCCESSCOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',SUCCESSCOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',SUCCESSCOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',SUCCESSCOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',SUCCESSCOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',SUCCESSCOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',SUCCESSCOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',SUCCESSCOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',SUCCESSCOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',SUCCESSCOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',SUCCESSCOUNT, 0)) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'실패건수' as countType" + 
                "       , 303       as align" + 
                "       ,SUM(decode(PROCESSHOUR,'00',FAILURECOUNT, 0)) hour00" + 
                "       ,SUM(decode(PROCESSHOUR,'01',FAILURECOUNT, 0)) hour01" + 
                "       ,SUM(decode(PROCESSHOUR,'02',FAILURECOUNT, 0)) hour02" + 
                "       ,SUM(decode(PROCESSHOUR,'03',FAILURECOUNT, 0)) hour03" + 
                "       ,SUM(decode(PROCESSHOUR,'04',FAILURECOUNT, 0)) hour04" + 
                "       ,SUM(decode(PROCESSHOUR,'05',FAILURECOUNT, 0)) hour05" + 
                "       ,SUM(decode(PROCESSHOUR,'06',FAILURECOUNT, 0)) hour06" + 
                "       ,SUM(decode(PROCESSHOUR,'07',FAILURECOUNT, 0)) hour07" + 
                "       ,SUM(decode(PROCESSHOUR,'08',FAILURECOUNT, 0)) hour08" + 
                "       ,SUM(decode(PROCESSHOUR,'09',FAILURECOUNT, 0)) hour09" + 
                "       ,SUM(decode(PROCESSHOUR,'10',FAILURECOUNT, 0)) hour10" + 
                "       ,SUM(decode(PROCESSHOUR,'11',FAILURECOUNT, 0)) hour11" + 
                "       ,SUM(decode(PROCESSHOUR,'12',FAILURECOUNT, 0)) hour12" + 
                "       ,SUM(decode(PROCESSHOUR,'13',FAILURECOUNT, 0)) hour13" + 
                "       ,SUM(decode(PROCESSHOUR,'14',FAILURECOUNT, 0)) hour14" + 
                "       ,SUM(decode(PROCESSHOUR,'15',FAILURECOUNT, 0)) hour15" + 
                "       ,SUM(decode(PROCESSHOUR,'16',FAILURECOUNT, 0)) hour16" + 
                "       ,SUM(decode(PROCESSHOUR,'17',FAILURECOUNT, 0)) hour17" + 
                "       ,SUM(decode(PROCESSHOUR,'18',FAILURECOUNT, 0)) hour18" + 
                "       ,SUM(decode(PROCESSHOUR,'19',FAILURECOUNT, 0)) hour19" + 
                "       ,SUM(decode(PROCESSHOUR,'20',FAILURECOUNT, 0)) hour20" + 
                "       ,SUM(decode(PROCESSHOUR,'21',FAILURECOUNT, 0)) hour21" + 
                "       ,SUM(decode(PROCESSHOUR,'22',FAILURECOUNT, 0)) hour22" + 
                "       ,SUM(decode(PROCESSHOUR,'23',FAILURECOUNT, 0)) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   UNION ALL" + 
                "   SELECT " + 
                "       'TOTAL' processDate" + 
                "       ,'TOTAL' interfaceId" + 
                "       ,'평균처리시간' as countType" + 
                "       , 304       as align" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'00',AVERAGETIME, 0)), 3) hour00" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'01',AVERAGETIME, 0)), 3) hour01" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'02',AVERAGETIME, 0)), 3) hour02" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'03',AVERAGETIME, 0)), 3) hour03" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'04',AVERAGETIME, 0)), 3) hour04" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'05',AVERAGETIME, 0)), 3) hour05" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'06',AVERAGETIME, 0)), 3) hour06" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'07',AVERAGETIME, 0)), 3) hour07" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'08',AVERAGETIME, 0)), 3) hour08" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'09',AVERAGETIME, 0)), 3) hour09" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'10',AVERAGETIME, 0)), 3) hour10" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'11',AVERAGETIME, 0)), 3) hour11" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'12',AVERAGETIME, 0)), 3) hour12" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'13',AVERAGETIME, 0)), 3) hour13" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'14',AVERAGETIME, 0)), 3) hour14" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'15',AVERAGETIME, 0)), 3) hour15" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'16',AVERAGETIME, 0)), 3) hour16" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'17',AVERAGETIME, 0)), 3) hour17" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'18',AVERAGETIME, 0)), 3) hour18" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'19',AVERAGETIME, 0)), 3) hour19" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'20',AVERAGETIME, 0)), 3) hour20" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'21',AVERAGETIME, 0)), 3) hour21" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'22',AVERAGETIME, 0)), 3) hour22" + 
                "       ,ROUND(AVG(decode(PROCESSHOUR,'23',AVERAGETIME, 0)), 3) hour23" + 
                "   FROM STTSDEFERREDHOUR" + 
                "   WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "     AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "     AND PROCESSDATE || PROCESSHOUR BETWEEN :startDate AND :endDate" + 
                "   ORDER BY processDate , align, interfaceId )";
        List<Object[]> list = session.createSQLQuery(sql).setString("interfaceId", params.get("interfaceId"))
                .setString("startDate", params.get("startDate"))
                .setString("endDate", params.get("endDate"))
                .list();
        for (Object[] o : list) {
            ProcessStaticsHour psd = new ProcessStaticsHour();
            psd.setAlign(((BigDecimal)o[0]).intValue());
            psd.setProcessDate((String) o[1]);
            psd.setInterfaceId((String) o[2]);
            psd.setCountType((String) o[3]);
            psd.setHour00(((BigDecimal)o[4]).intValue());
            psd.setHour01(((BigDecimal)o[5]).intValue());
            psd.setHour02(((BigDecimal)o[6]).intValue());
            psd.setHour03(((BigDecimal)o[7]).intValue());
            psd.setHour04(((BigDecimal)o[8]).intValue());
            psd.setHour05(((BigDecimal)o[9]).intValue());
            psd.setHour06(((BigDecimal)o[10]).intValue());
            psd.setHour07(((BigDecimal)o[11]).intValue());
            psd.setHour08(((BigDecimal)o[12]).intValue());
            psd.setHour09(((BigDecimal)o[13]).intValue());
            psd.setHour10(((BigDecimal)o[14]).intValue());
            psd.setHour11(((BigDecimal)o[15]).intValue());
            psd.setHour12(((BigDecimal)o[16]).intValue());
            psd.setHour13(((BigDecimal)o[17]).intValue());
            psd.setHour14(((BigDecimal)o[18]).intValue());
            psd.setHour15(((BigDecimal)o[19]).intValue());
            psd.setHour16(((BigDecimal)o[20]).intValue());
            psd.setHour17(((BigDecimal)o[21]).intValue());
            psd.setHour18(((BigDecimal)o[22]).intValue());
            psd.setHour19(((BigDecimal)o[23]).intValue());
            psd.setHour20(((BigDecimal)o[24]).intValue());
            psd.setHour21(((BigDecimal)o[25]).intValue());
            psd.setHour22(((BigDecimal)o[26]).intValue());
            psd.setHour23(((BigDecimal)o[27]).intValue());
            rtn.add(psd);
        }
        return rtn;
    }
    
    
    public List<ProcessStaticsDay> getProcessStaticsBatchDay(Session session, Map<String, String> params) {
        List<ProcessStaticsDay> rtn = new ArrayList<>();
        String sql = "SELECT" + 
                "   100 align," + 
                "   PROCESSDATE processDate," + 
                "   INTEGRATIONSERVICEID interfaceId," + 
                "   (SUCCESSCOUNT + FAILURECOUNT) totalCount," + 
                "   SUCCESSCOUNT successCount," + 
                "   FAILURECOUNT failCount," + 
                "   ROUND(AVERAGETIME,3) processTime" + 
                "        FROM STTSBATCHDAY" + 
                "        WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                " AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                " AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "        UNION ALL" + 
                "        SELECT" + 
                "   200 align," + 
                "   PROCESSDATE processDate," + 
                "   'SUB-TOTAL' interfaceId," + 
                "   SUM(SUCCESSCOUNT + FAILURECOUNT) totalCount," + 
                "   SUM(SUCCESSCOUNT) successCount," + 
                "   SUM(FAILURECOUNT) failCount," + 
                "   ROUND(AVG(AVERAGETIME), 3) processTime" + 
                "        FROM STTSBATCHDAY" + 
                "        WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                " AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                " AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "        GROUP BY PROCESSDATE" + 
                "        UNION ALL" + 
                "        SELECT" + 
                "   300 align," + 
                "   'TOTAL' processDate," + 
                "   'TOTAL' interfaceId," + 
                "   NVL(SUM(SUCCESSCOUNT + FAILURECOUNT), 0) totalCount," + 
                "   NVL(SUM(SUCCESSCOUNT), 0) successCount," + 
                "   NVL(SUM(FAILURECOUNT), 0) failCount," + 
                "   NVL(ROUND(AVG(AVERAGETIME), 3), 0) processTime" + 
                "        FROM STTSBATCHDAY" + 
                "        WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                " AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                " AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "        ORDER BY processDate, align";
        List<Object[]> list = session.createSQLQuery(sql).setString("interfaceId", params.get("interfaceId"))
                .setString("startDate", params.get("startDate"))
                .setString("endDate", params.get("endDate"))
                .list();
        for (Object[] o : list) {
            ProcessStaticsDay psd = new ProcessStaticsDay();
            psd.setAlign(((BigDecimal)o[0]).intValue());
            psd.setProcessDate((String) o[1]);
            psd.setInterfaceId((String) o[2]);
            psd.setTotalCount(((BigDecimal)o[3]).intValue());
            psd.setSuccessCount(((BigDecimal)o[4]).intValue());
            psd.setFailCount(((BigDecimal)o[5]).intValue());
            psd.setProcessTime("" + ((BigDecimal)o[6]).intValue());
            rtn.add(psd);
        }
        return rtn;
    }

    public List<ProcessStaticsDay> getProcessStaticsRealtimeDay(Session session, Map<String, String> params) {
        List<ProcessStaticsDay> rtn = new ArrayList<>();
        String sql = "SELECT" + 
                "   100 align," + 
                "   PROCESSDATE processDate," + 
                "   INTEGRATIONSERVICEID interfaceId," + 
                "   (SUCCESSCOUNT + FAILURECOUNT) totalCount," + 
                "   SUCCESSCOUNT successCount," + 
                "   FAILURECOUNT failCount," + 
                "   ROUND(AVERAGETIME,3) processTime" + 
                "        FROM STTSREALTIMEDAY" + 
                "        WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                " AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                " AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "        UNION ALL" + 
                "        SELECT" + 
                "   200 align," + 
                "   PROCESSDATE processDate," + 
                "   'SUB-TOTAL' interfaceId," + 
                "   SUM(SUCCESSCOUNT + FAILURECOUNT) totalCount," + 
                "   SUM(SUCCESSCOUNT) successCount," + 
                "   SUM(FAILURECOUNT) failCount," + 
                "   ROUND(AVG(AVERAGETIME), 3) processTime" + 
                "        FROM STTSREALTIMEDAY" + 
                "        WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                " AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                " AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "        GROUP BY PROCESSDATE" + 
                "        UNION ALL" + 
                "        SELECT" + 
                "   300 align," + 
                "   'TOTAL' processDate," + 
                "   'TOTAL' interfaceId," + 
                "   NVL(SUM(SUCCESSCOUNT + FAILURECOUNT), 0) totalCount," + 
                "   NVL(SUM(SUCCESSCOUNT), 0) successCount," + 
                "   NVL(SUM(FAILURECOUNT), 0) failCount," + 
                "   NVL(ROUND(AVG(AVERAGETIME), 3), 0) processTime" + 
                "        FROM STTSREALTIMEDAY" + 
                "        WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                " AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                " AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "        ORDER BY processDate, align";
        List<Object[]> list = session.createSQLQuery(sql).setString("interfaceId", params.get("interfaceId"))
                .setString("startDate", params.get("startDate"))
                .setString("endDate", params.get("endDate"))
                .list();
        for (Object[] o : list) {
            ProcessStaticsDay psd = new ProcessStaticsDay();
            psd.setAlign(((BigDecimal)o[0]).intValue());
            psd.setProcessDate((String) o[1]);
            psd.setInterfaceId((String) o[2]);
            psd.setTotalCount(((BigDecimal)o[3]).intValue());
            psd.setSuccessCount(((BigDecimal)o[4]).intValue());
            psd.setFailCount(((BigDecimal)o[5]).intValue());
            psd.setProcessTime("" + ((BigDecimal)o[6]).intValue());
            rtn.add(psd);
        }
        return rtn;
    }


    public List<ProcessStaticsDay> getProcessStaticsRealtimeDaySim(Session session, Map<String, String> params) {
        List<ProcessStaticsDay> rtn = new ArrayList<>();
        String sql = "SELECT" + 
                " AA.align align," + 
                " AA.processDate processDate," + 
                " AA.interfaceId interfaceId," + 
                " NVL(AA.totalCount, 0) totalCount," + 
                " NVL(BB.simCount, 0) simCount," + 
                " NVL(AA.successCount, 0) successCount," + 
                " NVL(AA.failCount, 0) failCount," + 
                " NVL(AA.processTime, 0) processTime" + 
                " FROM ( SELECT" + 
                "        100 align," + 
                "        PROCESSDATE processDate," + 
                "        INTEGRATIONSERVICEID interfaceId," + 
                "        (SUCCESSCOUNT + FAILURECOUNT) totalCount," + 
                "        SUCCESSCOUNT successCount," + 
                "        FAILURECOUNT failCount," + 
                "        ROUND(AVERAGETIME,3) processTime" + 
                "    FROM STTSREALTIMEDAY" + 
                "    WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "      AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "      AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "    UNION ALL" + 
                "    SELECT" + 
                "        200 align," + 
                "        PROCESSDATE processDate," + 
                "        'SUB-TOTAL' interfaceId," + 
                "        SUM(SUCCESSCOUNT + FAILURECOUNT) totalCount," + 
                "        SUM(SUCCESSCOUNT) successCount," + 
                "        SUM(FAILURECOUNT) failCount," + 
                "        ROUND(AVG(AVERAGETIME), 3) processTime" + 
                "    FROM STTSREALTIMEDAY" + 
                "    WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "      AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "      AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "    GROUP BY PROCESSDATE" + 
                "    UNION ALL" + 
                "    SELECT" + 
                "        300 align," + 
                "        'TOTAL' processDate," + 
                "        'TOTAL' interfaceId," + 
                "        NVL(SUM(SUCCESSCOUNT + FAILURECOUNT), 0) totalCount," + 
                "        NVL(SUM(SUCCESSCOUNT), 0) successCount," + 
                "        NVL(SUM(FAILURECOUNT), 0) failCount," + 
                "        NVL(ROUND(AVG(AVERAGETIME), 3), 0) processTime" + 
                "    FROM STTSREALTIMEDAY" + 
                "    WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                "      AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "      AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "    ORDER BY processDate, align ) AA," + 
                " ( SELECT" + 
                "    100 align," + 
                "    SUBSTR(A.PROCESSTIME,1 , 8) processDate, " + 
                "    B.INTEGRATIONSERVICEID interfaceId, " + 
                "    SUM(1) simCount" + 
                "        FROM SIMULATIONRESULT A, SIMULATION B" + 
                "        WHERE A.SIMULATIONID = B.SIMULATIONID" + 
                "    AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "    AND SUBSTR(A.PROCESSTIME,1 , 8) BETWEEN :startDate AND :endDate" + 
                "        GROUP BY SUBSTR(A.PROCESSTIME,1 , 8), B.INTEGRATIONSERVICEID" + 
                "        " + 
                "        UNION ALL" + 
                "        SELECT" + 
                "    200 align," + 
                "    SUBSTR(A.PROCESSTIME,1 , 8) processDate, " + 
                "    'SUB-TOTAL' interfaceId, " + 
                "    SUM(1) simCount" + 
                "        FROM SIMULATIONRESULT A, SIMULATION B" + 
                "        WHERE A.SIMULATIONID = B.SIMULATIONID" + 
                "    AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "    AND SUBSTR(A.PROCESSTIME,1 , 8) BETWEEN :startDate AND :endDate" + 
                "        GROUP BY SUBSTR(A.PROCESSTIME,1 , 8)" + 
                "        UNION ALL" + 
                "        SELECT" + 
                "        300 align," + 
                "    'TOTAL' processDate, " + 
                "    'TOTAL' interfaceId, " + 
                "    SUM(1) simCount" + 
                "        FROM SIMULATIONRESULT A, SIMULATION B" + 
                "        WHERE A.SIMULATIONID = B.SIMULATIONID" + 
                "    AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                "    AND SUBSTR(A.PROCESSTIME,1 , 8) BETWEEN :startDate AND :endDate" + 
                "        ORDER BY processDate, align ) BB" + 
                " WHERE AA.PROCESSDATE = BB.PROCESSDATE(+) AND AA.INTERFACEID = BB.INTERFACEID(+)" + 
                " ORDER BY processDate, align";
        List<Object[]> list = session.createSQLQuery(sql).setString("interfaceId", params.get("interfaceId"))
                .setString("startDate", params.get("startDate"))
                .setString("endDate", params.get("endDate"))
                .list();
        for (Object[] o : list) {
            ProcessStaticsDay psd = new ProcessStaticsDay();
            psd.setAlign(((BigDecimal)o[0]).intValue());
            psd.setProcessDate((String) o[1]);
            psd.setInterfaceId((String) o[2]);
            psd.setTotalCount(((BigDecimal)o[3]).intValue());
            psd.setSimCount(((BigDecimal)o[4]).intValue());
            psd.setSuccessCount(((BigDecimal)o[5]).intValue());
            psd.setFailCount(((BigDecimal)o[6]).intValue());
            psd.setProcessTime("" + ((BigDecimal)o[7]).intValue());
            rtn.add(psd);
        }
        return rtn;
    }
    
    public List<ProcessStaticsDay> getProcessStaticsDeferredDay(Session session, Map<String, String> params) {
        List<ProcessStaticsDay> rtn = new ArrayList<>();
        String sql = "SELECT" + 
                "   100 align," + 
                "   PROCESSDATE processDate," + 
                "   INTEGRATIONSERVICEID interfaceId," + 
                "   (SUCCESSCOUNT + FAILURECOUNT) totalCount," + 
                "   SUCCESSCOUNT successCount," + 
                "   FAILURECOUNT failCount," + 
                "   ROUND(AVERAGETIME,3) processTime" + 
                "        FROM STTSDEFERREDDAY" + 
                "        WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                " AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                " AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "        UNION ALL" + 
                "        SELECT" + 
                "   200 align," + 
                "   PROCESSDATE processDate," + 
                "   'SUB-TOTAL' interfaceId," + 
                "   SUM(SUCCESSCOUNT + FAILURECOUNT) totalCount," + 
                "   SUM(SUCCESSCOUNT) successCount," + 
                "   SUM(FAILURECOUNT) failCount," + 
                "   ROUND(AVG(AVERAGETIME), 3) processTime" + 
                "        FROM STTSDEFERREDDAY" + 
                "        WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                " AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                " AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "        GROUP BY PROCESSDATE" + 
                "        UNION ALL" + 
                "        SELECT" + 
                "   300 align," + 
                "   'TOTAL' processDate," + 
                "   'TOTAL' interfaceId," + 
                "   NVL(SUM(SUCCESSCOUNT + FAILURECOUNT), 0) totalCount," + 
                "   NVL(SUM(SUCCESSCOUNT), 0) successCount," + 
                "   NVL(SUM(FAILURECOUNT), 0) failCount," + 
                "   NVL(ROUND(AVG(AVERAGETIME), 3), 0) processTime" + 
                "        FROM STTSDEFERREDDAY" + 
                "        WHERE INTEGRATIONSERVICEID <> 'NA'" + 
                " AND INTEGRATIONSERVICEID like DECODE(:interfaceId,    NULL,'%%', '%' || UPPER(:interfaceId) || '%')" + 
                " AND PROCESSDATE BETWEEN :startDate AND :endDate" + 
                "        ORDER BY processDate, align";
        List<Object[]> list = session.createSQLQuery(sql).setString("interfaceId", params.get("interfaceId"))
                .setString("startDate", params.get("startDate"))
                .setString("endDate", params.get("endDate"))
                .list();
        for (Object[] o : list) {
            ProcessStaticsDay psd = new ProcessStaticsDay();
            psd.setAlign(((BigDecimal)o[0]).intValue());
            psd.setProcessDate((String) o[1]);
            psd.setInterfaceId((String) o[2]);
            psd.setTotalCount(((BigDecimal)o[3]).intValue());
            psd.setSuccessCount(((BigDecimal)o[4]).intValue());
            psd.setFailCount(((BigDecimal)o[5]).intValue());
            psd.setProcessTime("" + ((BigDecimal)o[6]).intValue());
            rtn.add(psd);
        }
        return rtn;
    }

    public List<HeaderField> getHeaderField(Session session) {
        List<HeaderField> rtn = null;
        String sql = "SELECT " + 
                "   A.FIELDINDEX fieldIndex, " + 
                "   C.FIELDNAME fieldName, " + 
                "   C.FIELDLENGTH fieldLength" + 
                " FROM FIELDGROUPMAP A , (SELECT CODEVALUE FROM ISM_CODE WHERE CODENAME ='FieldGroupName') B, FIELD C" + 
                " WHERE A.FIELDGROUPID = B.CODEVALUE" + 
                "    AND A.FIELDID = C.FIELDID" + 
                " ORDER BY FIELDINDEX";
        List<Object[]> list = session.createSQLQuery(sql).list();
        for (Object[] o : list) {
            HeaderField hf = new HeaderField();
            hf.setFieldIndex(((BigDecimal)o[0]).intValue());
            hf.setFieldName((String) o[1]);
            hf.setFieldLength(((BigDecimal)o[2]).intValue());
        }
        return rtn;
    }
}
