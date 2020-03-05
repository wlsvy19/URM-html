package com.ism.urm.service.rule;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ism.urm.dao.rule.RuleDao;
import com.ism.urm.vo.HiberUtill;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.rule.RuleVo;

public abstract class RuleService<T extends RuleVo> {

    protected Logger logger = LoggerFactory.getLogger("debug");
    
    protected SessionFactory sessionFactory;
    protected RuleDao<T> dao;
    
    public RuleService() {
        sessionFactory = HiberUtill.getSessionFactory();
    }

    public List<T> search(List<RelationOp> filter) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            List<T> rtn = dao.search(session, filter);
            return rtn;
        } catch (Exception e) {
            logger.error("Failed to search", e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
    
    public T get(String id) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            T rtn =  dao.get(session, id);
            return rtn;
        } catch (Exception e) {
            logger.error("Failed to get " + dao.getEntityName(), e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
    
    public T save(T vo) throws Exception {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Date now = new Date();
            vo.setChgDate(now);
            // TODO get user id from session
            vo.setChgId("eai");
            
            if (vo.getId() == null || vo.getId().trim().length() == 0) {
                logger.info("create rule ID");
                String newId = createId();
                if (newId == null) {
                    throw new Exception("failed to create ID");
                }
                vo.setId(newId);
                vo.setRegDate(now);
                // TODO get user id from session
                vo.setRegId("eai");
            }

            dao.save(session, vo);
            tx.commit();
            
            T rtn = dao.get(session, vo.getId());
            
            return rtn;
        } catch (Exception e) {
            logger.error("Failed to save " + dao.getEntityName(), e);
            if (tx != null)    tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
    
    protected String createId() throws Exception {
        String id = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            id = dao.createId(session);
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) session.close();
        }
        return id;
    }
}
