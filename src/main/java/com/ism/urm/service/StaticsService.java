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
    
    public List<RequestChangeCount> getRequestChangeCount(int type, Map<String, String> params) throws Exception {
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

}
