package com.ism.urm.service.rule;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ism.urm.dao.rule.RuleDao;
import com.ism.urm.vo.HiberUtill;
import com.ism.urm.vo.rule.RuleVo;

public abstract class RuleService<T extends RuleVo> {

    protected Logger logger = LoggerFactory.getLogger("debug");
    
    protected SessionFactory sessionFactory;
    protected RuleDao<T> dao;
    
    public RuleService() {
        sessionFactory = HiberUtill.getSessionFactory();
    }

    public List<T> search(Map<String, String> params) throws Exception {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            List<T> rtn = dao.search(session, params);
            return rtn;
        } catch (Exception e) {
            logger.error("", e);
            throw e;
        }
    }
    
    public T get(String id) throws Exception {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            T rtn =  dao.get(session, id);
            return rtn;
        } catch (Exception e) {
            logger.error("", e);
            throw e;
        }
    }
    
    public T save(T vo) throws Exception {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            
            if (vo.getId() == null || vo.getId().trim().length() == 0) {
                logger.info("create ID");
                String newId = createId();
                if (newId == null) {
                    throw new Exception("failed to create ID");
                }
                vo.setId(newId);
            }

            Date now = new Date();
            vo.setRegDate(now);
            vo.setChgDate(now);
            
            dao.save(session, vo);
            tx.commit();
            
            T rtn = dao.get(session, vo.getId());
            
            return rtn;
        } catch (Exception e) {
            logger.error("", e);
            if (tx != null)    tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
    
    protected String createId() {
        String id = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            id = dao.createId(session);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (session != null) session.close();
        }
        return id;
    }
}
