package com.ism.urm.service.rule;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ism.urm.dao.rule.RuleDao;
import com.ism.urm.util.SessionUtil;
import com.ism.urm.vo.HiberUtill;
import com.ism.urm.vo.JobResult;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.rule.RuleVo;

public abstract class RuleService<T extends RuleVo> {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    
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
            if (session != null) try { session.close(); } catch (Exception ignore) { }
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
            if (session != null) try{ session.close(); } catch (Exception ignore) { }
        }
    }
    
    public T save(T vo) throws Exception {
        T rtn = null;
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Date now = new Date();
            vo.setChgDate(now);
            vo.setChgId(SessionUtil.getUserID());
            
            if (vo.getId() == null || vo.getId().trim().length() == 0) {
                logger.info("create rule ID");
                String newId = dao.createId(session);
                if (newId == null) {
                    throw new Exception("failed to create ID");
                }
                vo.setId(newId);
                vo.setRegDate(now);
                vo.setRegId(SessionUtil.getUserID());
            }

            dao.save(session, vo);
            
            rtn = dao.get(session, vo.getId());
            tx.commit();
        } catch (Exception e) {
            logger.error("Failed to save " + dao.getEntityName(), e);
            if (tx != null) try { tx.rollback(); } catch (Exception ignore) { }
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    public JobResult delete(List<String> ids) throws Exception {
        JobResult res = new JobResult(0);
        int count = 0;
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            
            boolean used = false;
            String idStr = "";
            for (String id : ids) {
                if (getInfluence(id) > 0) {
                    used = true;
                    idStr += ", " + id ;
                    continue;
                }
                dao.delete(session, id);
                count++;
            }
            
            if (used) {
                res = new JobResult(1, "[" + idStr.substring(1) + " ] are used.");
            } else {
                res.setObj(count);
            }
            tx.commit();
        } catch (Exception e) {
            logger.error("Failed to delete " + dao.getEntityName(), e);
            if (tx != null) try { tx.rollback(); } catch (Exception ignore) { }
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return res;
    }

    protected abstract int getInfluence(String id) throws Exception;
}
