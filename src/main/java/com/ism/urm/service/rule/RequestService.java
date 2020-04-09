package com.ism.urm.service.rule;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.multipart.MultipartFile;

import com.ism.urm.config.URMProperties;
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

    public String requestFileUpload(MultipartFile item) throws Exception {
        String fileName = item.getOriginalFilename();
        
        int i = fileName.lastIndexOf('.');
        String extension = "";
        if (i > 0) {
            extension = fileName.substring(i);
        }
        String upFileName = null;
        
        synchronized (_lock) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
            upFileName = format.format(new Date()) + "_" + _seq++ + extension;
            if (_seq > 999) {
                _seq = 1;
            }
        }
        
        File outFile = new File(URMProperties.get("file.repository"), upFileName);
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(outFile);
            bos = new BufferedOutputStream(fos);
            bos.write(item.getBytes());
            bos.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (fos != null) try { fos.close(); } catch (Exception ignore) {}
            if (bos != null) try { bos.close(); } catch (Exception ignore) {}
        }
        
        return upFileName;
    }

}
