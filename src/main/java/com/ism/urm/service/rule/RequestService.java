package com.ism.urm.service.rule;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ism.urm.dao.manage.BusinessCodeDao;
import com.ism.urm.dao.rule.AppSystemDao;
import com.ism.urm.dao.rule.RequestDao;
import com.ism.urm.vo.PagingResult;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.RelationOp.OpType;
import com.ism.urm.vo.RelationOp.ValueType;
import com.ism.urm.vo.rule.request.Request;
import com.ism.urm.vo.rule.request.RequestHistory;


public class RequestService extends RuleService<Request> {

    private static int _seq = 0;
    private static Object _lock = new Object();

    private AppSystemDao sysDao;

    public RequestService() {
        super();
        dao = new RequestDao();
        sysDao = new AppSystemDao();
    }

    public PagingResult<Request> search(int page, int size, List<RelationOp> filter) throws Exception {
        PagingResult<Request> rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            // 삭제 안된 목록만 조회
            if(filter != null) {
                filter.add(RelationOp.get("delYN", OpType.EQ, false, ValueType.BOOLEAN));
            }
            rtn = dao.searchPage(session, page, size, filter);
            
            // set child
            session = sessionFactory.openSession();
            session.beginTransaction();
            if (rtn.getList() != null) {
                for (Request r : rtn.getList()) {
                    r.setSendSystem(sysDao.get(session, r.getSendSystemId()));
                    r.setRcvSystem(sysDao.get(session, r.getRcvSystemId()));
                }
            }
            
        } catch (Exception e) {
            logger.error("Failed to search Request. ", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public List<Request> listByManager(String userId) throws Exception {
        List<Request> rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            
            rtn = ((RequestDao) dao).listByManager(session, userId);
            
            // set child
            session = sessionFactory.openSession();
            session.beginTransaction();
            if (rtn != null) {
                for (Request r : rtn) {
                    r.setSendSystem(sysDao.get(session, r.getSendSystemId()));
                    r.setRcvSystem(sysDao.get(session, r.getRcvSystemId()));
                }
            }
            
        } catch (Exception e) {
            logger.error("Failed to search Request. ", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public List<RequestHistory> getHistroy(String reqId) throws Exception {
        List<RequestHistory> rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            
            rtn = ((RequestDao) dao).getHistory(session, reqId);
            
            BusinessCodeDao bizDao = new BusinessCodeDao();
            session = sessionFactory.openSession();
            session.beginTransaction();
            if (rtn != null) {
                for (Request r : rtn) {
                    r.setSendSystem(sysDao.get(session, r.getSendSystemId()));
                    r.setRcvSystem(sysDao.get(session, r.getRcvSystemId()));
                    
                    r.setSendJobCode(bizDao.get(session, r.getSendJobCodeId()));
                    r.setRcvJobCode(bizDao.get(session, r.getRcvJobCodeId()));
                }
            }
            
        } catch (Exception e) {
            logger.error("Failed to get history. " + reqId, e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public int changeAdmin(List<Request> requests) throws Exception {
        int count = 0;
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Date now = new Date();
            for (Request r : requests) {
                r.setChgDate(now);
                dao.save(session, r);
                count++;
            }
            
            tx.commit();
        } catch (Exception e) {
            logger.error("Failed to change Admin", e);
            if (tx != null) try { tx.rollback(); } catch (Exception ignore) { }
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return count;
    }

//    public List<RequestExcel> getRequestList_Excel(Map params) throws Exception {
//        
//        debug.info("RequestService.getRequestList() call.");
//        
//        if(params == null ) throw new NotFoundRequestException("Exception:Argment params is null");
//        
//        try {
//            return requestDao.listExcel(params);
//        } catch (Exception e) {
//            debug.error("", e);
//            throw e;
//        }
//    }

    public String requestFileUpload(String fileName, byte[] data) throws Exception {
        logger.info("RequestService.requestFileUpload() call.");
        
        logger.info("file name:" + fileName);
        
        int i = fileName.lastIndexOf('.');
        String extension = "";
        if (i > 0) {
            extension = fileName.substring(i);
        }
        String upFileName = null;
        
        synchronized (_lock) {
            upFileName = null;//Accessory.getFormatDate("yyyyMMdd_HHmmss") + "_" + _seq++ + extension;
            if (_seq > 999) {
                _seq = 1;
            }
        }
         
        logger.info("FILE ENCODING :" + System.getProperty("file.encoding"));
        
        String utfFileName = new String(upFileName.getBytes("UTF-8"));
        logger.info("UTF-8  file name :" + utfFileName);
        
        String euckrFileName = new String(upFileName.getBytes("euc-kr")); 
        logger.info("EUC-KR file name :" + euckrFileName);
        
        String localFileName = new String(upFileName.getBytes(System.getProperty("file.encoding"))); 
        logger.info("Local file name :" + localFileName);
        
        File outFile = new File(System.getProperty("urm.file.repository"), upFileName);
        
        BufferedOutputStream bos = null;
        
        try {
            bos = new BufferedOutputStream(new FileOutputStream(outFile));
            bos.write(data);
            bos.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                bos.close();
            } catch (Exception ee) {}
            bos = null;
        }
        
        return upFileName;
    }

    private byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

}
