package com.ism.urm.service.manage;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ism.urm.dao.manage.UserDao;
import com.ism.urm.util.SessionUtil;
import com.ism.urm.vo.HiberUtill;
import com.ism.urm.vo.JobResult;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.manage.User;

public class UserService {
    private Logger logger = LoggerFactory.getLogger("debug");
    private UserDao dao;
    protected SessionFactory sessionFactory;

    static List<User> userList = new ArrayList<User>();

    public UserService() {
        dao = new UserDao();
        sessionFactory = HiberUtill.getSessionFactory();
    }
    
    public int idCheck(String userID) throws Exception {
        Session session = null;
        int rtn = 0;

        if (userID == null || userID.length() == 0)
            logger.error("userId is null");

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            rtn = dao.idCheck(session, userID);
        } catch (Exception e) {
            logger.error("Failed to check ID. ", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public List<User> search(List<RelationOp> filter) throws Exception {
        List<User> rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            rtn = dao.search(session, filter);

        } catch (Exception e) {
            logger.error("Failed to search User. ", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public User get(String userId) throws Exception {
        User rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            rtn = dao.get(session, userId);

        } catch (Exception e) {
            logger.error("Failed to get User. ", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public User save(User vo) throws Exception {
        User rtn = null;
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            
            String passwd = vo.getPassword();
            if (passwd == null || passwd.trim().length() == 0) {
                User org = dao.get(session, vo.getId());
                vo.setPassword(org.getPassword());
                session.clear();
            } else {
                vo.setPassword(SessionUtil.getEncString(passwd));
            }
            
            dao.save(session, vo);
            
            rtn = dao.get(session, vo.getId());
            tx.commit();
        } catch (Exception e) {
            logger.error("Failed to save User. ", e);
            if (tx != null) try { tx.rollback(); } catch (Exception ignore) { }
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public JobResult login(User vo, String sessionId) throws Exception {
        JobResult rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            
            User org = null;
            if (sessionId == null || sessionId.length() == 0) {
                sessionId = vo.getId();
                org = dao.get(session, sessionId);
                
                if (org == null) {
                    rtn = new JobResult(1, "user not exist");
                    rtn.setObj("/login/login.page");
                    return rtn;
                } else {
                    String passwd = SessionUtil.getDecString(org.getPassword());
                    if (!passwd.equals(vo.getPassword())) {
                        rtn = new JobResult(2, "invalid password");
                        rtn.setObj("/login/login.page");
                        return rtn;
                    }
                }

                SessionUtil.setUserID(sessionId);
                rtn = new JobResult(0, "login success");
                rtn.setObj(sessionId);
            } else {
                rtn = new JobResult(0, "already logged!!!");
                rtn.setObj(sessionId);
            }
        } catch (Exception e) {
            logger.error("Faile to login. ", e);
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
                    idStr += ", " + id;
                    continue;
                }
                dao.delete(session, id);
                count++;
            }

            if (used) {
                res = new JobResult(1, idStr.substring(1) + " are used.");
            } else {
                res.setObj(count);
            }
            tx.commit();
        } catch (Exception e) {
            logger.error("Faile to delete " + dao.getEntityName(), e);
            if (tx != null) try { tx.rollback(); } catch (Exception ignore) { }
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return res;
    }
    
    private int getInfluence(String id) throws Exception {
        int res = 0;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            res = dao.getInfluence(session, id);
        } catch (Exception e) {
            logger.error("Failed to getInfluence", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return res;
    }
}
