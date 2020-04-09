package com.ism.urm.service;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ism.urm.dao.StaticsDao;
import com.ism.urm.vo.HiberUtill;
import com.ism.urm.vo.statics.RequestChangeCount;
import com.ism.urm.vo.statics.RequestProcessCount;

public class StaticsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private SessionFactory sessionFactory;
    private StaticsDao dao = null;

    public StaticsService() {
        sessionFactory = HiberUtill.getSessionFactory();
        dao = new StaticsDao();
    }

    public List<RequestProcessCount> getRequestProcessCount(int type, Map<String, String> params) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            return dao.getRequestProcessCount(session, type, params);
        } catch (Exception e){
            logger.error("Fail to getRequestProcessCount. ", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
    }
    
    public List<RequestChangeCount> getRequestChangeCount(int type, Map params) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            return dao.getRequestChangeCount(session, type, params);
        } catch (Exception e){
            logger.error("Fail to getRequestChangeCount. ", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
    }
    
    
//    public List<Statics> getPrcStatOfDay(Map params) throws Exception {
//            
//        debug.info("StaticsService.getPrcStatOfDay() call.");
//
//        if(params == null || params.size() == 0 ) throw new NotFoundRequestException("Exception:Argment params is null");
//        try{
//            return staticsDao.staticsPrcStatOfDay(params);
//        }catch(Exception e){
//            debug.fatal("",e);
//            throw e;
//        }
//    }
//
//    public List<Statics> getPrcStatOfMonth(Map params) throws Exception {
//        
//        debug.info("StaticsService.getPrcStatOfMonth() call.");
//
//        if(params == null || params.size() == 0 ) throw new NotFoundRequestException("Exception:Argment params is null");
//        try{
//            return staticsDao.staticsPrcStatOfMonth(params);
//        }catch(Exception e){
//            debug.fatal("",e);
//            throw e;
//        }
//    }
//    
//    public List<Statics> getIfTypeOfDay(Map params) throws Exception {
//        
//        debug.info("StaticsService.getIfTypeOfDay() call.");
//
//        if(params == null || params.size() == 0 ) throw new NotFoundRequestException("Exception:Argment params is null");
//        try{
//            return staticsDao.staticsIfTypeOfDay(params);
//        }catch(Exception e){
//            debug.fatal("",e);
//            throw e;
//        }
//    }
//    
//    public List<Statics> getIfTypeOfMonth(Map params) throws Exception {
//        
//        debug.info("StaticsService.getIfTypeOfMonth() call.");
//
//        if(params == null || params.size() == 0 ) throw new NotFoundRequestException("Exception:Argment params is null");
//        try{
//            return staticsDao.staticsIfTypeOfMonth(params);
//        }catch(Exception e){
//            debug.fatal("",e);
//            throw e;
//        }
//    }
//    
//    
//    public List<Statics> getJobType() throws Exception {
//        
//        debug.info("StaticsService.getJobType() call.");
//        try{
//            return staticsDao.staticsJobType();
//        }catch(Exception e){
//            debug.fatal("",e);
//            throw e;
//        }
//    }
}
