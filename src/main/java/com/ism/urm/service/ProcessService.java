package com.ism.urm.service;


import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ism.urm.dao.ProcessDao;
import com.ism.urm.vo.HiberUtill;
import com.ism.urm.vo.process.HeaderField;
import com.ism.urm.vo.process.LogBatch;
import com.ism.urm.vo.process.LogBatchDetail;
import com.ism.urm.vo.process.LogDeferred;
import com.ism.urm.vo.process.LogDeferredError;
import com.ism.urm.vo.process.LogRealtime;
import com.ism.urm.vo.process.LogRealtimeDetail;
import com.ism.urm.vo.process.MessageField;
import com.ism.urm.vo.process.ProcessStaticsDay;
import com.ism.urm.vo.process.ProcessStaticsHour;


public class ProcessService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ProcessDao dao;
    private SessionFactory ismd;
    private SessionFactory ismt;
    private SessionFactory ismp;

    public ProcessService() {
        dao = new ProcessDao();
        ismd = HiberUtill.getSessionFactory("dev");
        ismt = HiberUtill.getSessionFactory("test");
        ismp = HiberUtill.getSessionFactory("prod");
    }

    public List<LogBatch> getLogBatch(int type, Map<String, String> params) throws Exception {
        List<LogBatch> rtn = new ArrayList<>();
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getLogBatch(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getLogBatch(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getLogBatch(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getLogBatch", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public List<LogBatchDetail> getLogBatchDetail(int type, String batchId) throws Exception {
        List<LogBatchDetail> rtn = new ArrayList<>();
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getLogBatchDetail(session, batchId);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getLogBatchDetail(session, batchId);
            } else {
                session = ismp.openSession();
                rtn = dao.getLogBatchDetail(session, batchId);
            }
        } catch (Exception e) { 
            logger.error("Failed to getLogBatchDetail", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    public List<LogDeferred> getLogDeferred(int type, Map<String, String> params) throws Exception {
        List<LogDeferred> rtn = new ArrayList<>();
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getLogDeferred(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getLogDeferred(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getLogDeferred(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getLogDeferred", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    public List<LogDeferred> getLogDeferredDetail(int type, Map<String, String> params) throws Exception {
        List<LogDeferred> rtn = new ArrayList<>();
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getLogDeferredDetail(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getLogDeferredDetail(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getLogDeferredDetail(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getLogDeferredDetail", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public List<LogDeferredError> getLogDeferredError(int type, Map<String, String> params) throws Exception {
        List<LogDeferredError> rtn = new ArrayList<>();
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getLogDeferredError(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getLogDeferredError(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getLogDeferredError(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getLogDeferredError", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    
    public List<LogRealtime> getLogRealtime(int type, Map<String, String> params) throws Exception {
        List<LogRealtime> rtn = new ArrayList<>();
        Session session = null;
        try {
            String processDate = params.get("processDate");
            params.put("logTable", "LOG_REALTIME" + processDate.substring(6));
            
//            AND SUBSTR(STARTTIME, 9, 6) BETWEEN LPAD(#startTime#, 6, '0') AND LPAD(#endTime#, 6 , '0')
            String startTime = params.get("startTime");
            while(startTime.length() < 6) {
                startTime = "0" + startTime;
            }
            String endTime = params.get("endTime");
            while(endTime.length() < 6) {
                endTime = "0" + endTime;
            }
            params.put("startTime", processDate + startTime + "000");
            params.put("endTime",   processDate + endTime   + "999");
            
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getLogRealtime(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getLogRealtime(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getLogRealtime(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getLogRealtime", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    public List<LogRealtimeDetail> getLogRealtimeDetail(int type, Map<String, String> params) throws Exception {
        List<LogRealtimeDetail> rtn = new ArrayList<>();
        Session session = null;
        try {
            String processDate = params.get("processDate");
            params.put("logTable", "LOG_REALTIME" + processDate.substring(6));
            
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getLogRealtimeDetail(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getLogRealtimeDetail(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getLogRealtimeDetail(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getLogDeferredError", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    public List<ProcessStaticsHour> getProcessStaticsBatchHour(int type, Map<String, String> params) throws Exception {
        List<ProcessStaticsHour> rtn = null;
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getProcessStaticsBatchHour(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getProcessStaticsBatchHour(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getProcessStaticsBatchHour(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getProcessStaticsBatchHour", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    public List<ProcessStaticsHour> getProcessStaticsRealtimeHour(int type, Map<String, String> params) throws Exception {
        List<ProcessStaticsHour> rtn = null;
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getProcessStaticsRealtimeHour(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getProcessStaticsRealtimeHour(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getProcessStaticsRealtimeHour(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getProcessStaticsRealtimeHour", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    
    public List<ProcessStaticsHour> getProcessStaticsRealtimeHourSim(int type, Map<String, String> params) throws Exception {
        List<ProcessStaticsHour> rtn = null;
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getProcessStaticsRealtimeHourSim(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getProcessStaticsRealtimeHourSim(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getProcessStaticsRealtimeHourSim(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getProcessStaticsRealtimeHourSim", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    
    public List<ProcessStaticsHour> getProcessStaticsDeferredHour(int type, Map<String, String> params) throws Exception {
        List<ProcessStaticsHour> rtn = null;
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getProcessStaticsDeferredHour(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getProcessStaticsDeferredHour(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getProcessStaticsDeferredHour(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getProcessStaticsDeferredHour", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public List<ProcessStaticsDay> getProcessStaticsBatchDay(int type, Map<String, String> params) throws Exception {
        List<ProcessStaticsDay> rtn = null;
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getProcessStaticsBatchDay(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getProcessStaticsBatchDay(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getProcessStaticsBatchDay(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getProcessStaticsBatchDay", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    public List<ProcessStaticsDay> getProcessStaticsRealtimeDay(int type, Map<String, String> params) throws Exception {
        List<ProcessStaticsDay> rtn = null;
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getProcessStaticsRealtimeDay(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getProcessStaticsRealtimeDay(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getProcessStaticsRealtimeDay(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getProcessStaticsRealtimeDay", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    public List<ProcessStaticsDay> getProcessStaticsRealtimeDaySim(int type, Map<String, String> params) throws Exception {
        List<ProcessStaticsDay> rtn = null;
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getProcessStaticsRealtimeDaySim(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getProcessStaticsRealtimeDaySim(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getProcessStaticsRealtimeDaySim(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getProcessStaticsRealtimeDaySim", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    public List<ProcessStaticsDay> getProcessStaticsDeferredDay(int type, Map<String, String> params) throws Exception {
        List<ProcessStaticsDay> rtn = null;
        Session session = null;
        try {
            if (type == 1) {
                session = ismd.openSession();
                rtn = dao.getProcessStaticsDeferredDay(session, params);
            } else if (type == 2) {
                session = ismt.openSession();
                rtn = dao.getProcessStaticsDeferredDay(session, params);
            } else {
                session = ismp.openSession();
                rtn = dao.getProcessStaticsDeferredDay(session, params);
            }
        } catch (Exception e) { 
            logger.error("Failed to getProcessStaticsDeferredDay", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    private List<MessageField> parseMessage(byte[] message,List<HeaderField> fields) {
        logger.info("ProcessService.parseMessage() call. - " + new String(message));
        byte[] msg = message;
        List<MessageField> rtnList = new ArrayList<MessageField>();
        int curIndex = 0;
        int messageIndex = 0;
        for (HeaderField field : fields) {
            try {
                byte[] tmp = new byte[field.getFieldLength()];
                System.arraycopy(msg, curIndex, tmp, 0, tmp.length);
                MessageField mf = new MessageField();
                mf.setMessageIndex(field.getFieldIndex());
                mf.setFieldName(field.getFieldName());
                mf.setFieldValue(new String(tmp));
                curIndex += tmp.length;
                messageIndex = field.getFieldIndex();
                logger.info("ProcessService.parseMessage() - parse !!!! - " + messageIndex);
                rtnList.add(mf);
            }
            catch (Exception e) {
                logger.error("", e);
                break;
            }
        }
        try {
            int dataLength = msg.length - curIndex;
            if (dataLength > 0) {
                byte[] tmp = new byte[dataLength];
                System.arraycopy(msg, curIndex, tmp, 0, dataLength);
                for (int i = 0; i < dataLength; i++) {
                    if (tmp[i] == 0) {
                        tmp[i] = ' ';
                    }
                }
                MessageField mf = new MessageField();
                mf.setMessageIndex(++messageIndex);
                mf.setFieldName("DATA");
                mf.setFieldValue(new String(tmp));
                logger.info("DATA: " + new String(tmp));
                rtnList.add(mf);
            }
        } catch (Exception e) {
            logger.error("", e);
            // skip.......
        }
        return rtnList;
    }
    
    public List<MessageField> getMessage(int type, String processDate, String interfaceId, int serialNumber) throws Exception {
        List<MessageField> rtn = null;
        Session session = null;
        try {
            List<HeaderField> fields = null;
            if (type == 1) {
                session = ismd.openSession();
                fields =  dao.getHeaderField(session);
            } else if (type == 2) {
                session = ismt.openSession();
                fields  = dao.getHeaderField(session);
            } else {
                session = ismp.openSession();
                fields = dao.getHeaderField(session);
            }
            
            byte[] message = null;
            if (type == 1) {
                for (int i = 0; ; i++) {
                    String server = System.getProperty("urm.dev.realtime." + i + ".server");
                    if (server == null || server.length() == 0) {
                        break;
                    }
                    String[] serverinfo = server.split(":");
                    if (serverinfo.length != 2) {
                        logger.error("server info get fail..");
                        break;
                    }
                    try {
                        Integer.parseInt(serverinfo[1]);
                    } catch (Exception e) {
                        logger.error("server info get fail..");
                        break;
                    }
                    byte[] sndMsg = ("S" + processDate + interfaceId + getNumString(serialNumber, 10)).getBytes();
                    byte[] length = getNumString(sndMsg.length, 8).getBytes();
                    byte[] fullMsg = new byte[sndMsg.length + length.length];
                    System.arraycopy(length, 0, fullMsg, 0, length.length);
                    System.arraycopy(sndMsg, 0, fullMsg, length.length, sndMsg.length);
                    
                    message = getDetailRealtimeMessage(fullMsg, serverinfo);
                    if (message != null && message.length != 0) {
                        break;
                    }
                }
            } else if (type == 2) {
                for (int i = 0; ; i++) {
                    String server = System.getProperty("urm.tst.realtime." + i + ".server");
                    if (server == null || server.length() == 0) {
                        break;
                    }
                    String[] serverinfo = server.split(":");
                    if (serverinfo.length != 2) {
                        logger.error("server info get fail..");
                        break;
                    }

                    try {
                        Integer.parseInt(serverinfo[1]);
                    } catch(Exception e) {
                        logger.error("server info get fail..");
                        break;
                    }
                    byte[] sndMsg = ("S" + processDate + interfaceId + getNumString(serialNumber, 10)).getBytes();
                    byte[] length = getNumString(sndMsg.length, 8).getBytes();
                    byte[] fullMsg = new byte[sndMsg.length + length.length];
                    System.arraycopy(length, 0, fullMsg, 0, length.length);
                    System.arraycopy(sndMsg, 0, fullMsg, length.length, sndMsg.length);
                    
                    message = getDetailRealtimeMessage(fullMsg, serverinfo);
                    if (message != null && message.length != 0) {
                        break;
                    }
                }
            } else {
                for (int i = 0; ; i++) {
                    String server = System.getProperty("urm.pro.realtime." + i + ".server");
                    if (server == null || server.length() == 0) {
                        break;
                    }
                    String[] serverinfo = server.split(":");
                    if (serverinfo.length != 2) {
                        logger.error("server info get fail..");
                        break;
                    }
                    try {
                        Integer.parseInt(serverinfo[1]);
                    } catch (Exception e) {
                        logger.error("server info get fail..");
                        break;
                    }
                    byte[] sndMsg = ("S" + processDate + interfaceId + getNumString(serialNumber, 10)).getBytes();
                    byte[] length = getNumString(sndMsg.length, 8).getBytes();
                    byte[] fullMsg = new byte[sndMsg.length + length.length];
                    System.arraycopy(length, 0, fullMsg, 0, length.length);
                    System.arraycopy(sndMsg, 0, fullMsg, length.length, sndMsg.length);
                    
                    message = getDetailRealtimeMessage(fullMsg, serverinfo);
                    if (message != null && message.length != 0) {
                        break;
                    }

                }
            }
            
            if (message != null && fields != null) {
                rtn = parseMessage(message, fields);
            }
            
        } catch (Exception e) { 
            logger.error("Failed to getMessage", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    private byte[] getDetailRealtimeMessage(byte[] fullMsg, String[] serverInfo) {
        Socket socket = null;
        byte[] data = null;
        try {
            socket = new Socket(serverInfo[0], Integer.parseInt(serverInfo[1]));
            OutputStream out = socket.getOutputStream();
            out.write(fullMsg);
            out.flush();
            
            byte[] bLen = new byte[8];
            DataInputStream in = new DataInputStream(socket.getInputStream());
            in.readFully(bLen);
            int len = Integer.parseInt(new String(bLen));
            if (len == 0) {
                return null;
            }
            data = new byte[len];
            in.readFully(data);
        } catch (Exception e) {
            if (socket != null) {
                try { socket.close(); } catch (Exception ee) {}
            }
        }
        return data;
    }
    
    private String getNumString(int data, int len) {
        String rtnValue = "" + data;
        if (rtnValue.length() > len) {
            return rtnValue;
        }
        for (int i = rtnValue.length(); i < len; i++) {
            rtnValue = "0" + rtnValue;
        }
        return rtnValue;
    }
}
