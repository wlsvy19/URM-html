package com.ism.urm.service.manage;

import java.util.ArrayList;
import java.util.HashMap;
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
    private UserDao userDao;
    protected SessionFactory sessionFactory;

    static List<User> userList = new ArrayList<User>();

    public UserService() {
        userDao = new UserDao();
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
            rtn = userDao.idCheck(session, userID);
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
        return rtn;
    }

    public List<User> search(List<RelationOp> filter) throws Exception {
        List<User> rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            rtn = userDao.search(session, filter);

        } catch (Exception e) {
            logger.error("Failed to User search()", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
        return rtn;
    }

    public User get(String userId) throws Exception {
        User rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            rtn = userDao.get(session, userId);

        } catch (Exception e) {
            logger.error("Failed to User get()", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
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
                User org = userDao.get(session, vo.getId());
                vo.setPassword(org.getPassword());
            } else {
                vo.setPassword(SessionUtil.getEncString(passwd));
            }
            
            userDao.save(session, vo);
            tx.commit();
            rtn = userDao.get(session, vo.getId());
        } catch (Exception e) {
            logger.error("Failed to User save()", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
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
                if (vo.getPassword() == null || vo.getPassword().length() == 0) {
                    SessionUtil.invalidate();
                    throw new Exception("session expired!!!");
                }
                sessionId = vo.getId();

                org = userDao.get(session, sessionId);
                if (org == null) {
                    rtn = new JobResult(1, "user not exist.");
                    rtn.setObj("/login/login.page");
                    return rtn;
                } else {
                    String passwd = SessionUtil.getDecString(org.getPassword());
                    if (!passwd.equals(vo.getPassword())) {
                        rtn = new JobResult(2, "invalid password.");
                        rtn.setObj("/login/login.page");
                        return rtn;
                    }
                }

                SessionUtil.setUserID(sessionId);
                rtn = new JobResult(0, "login success.");
            } else {
                rtn = new JobResult(0, "already logged!!!");
                org = userDao.get(session, sessionId);
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("id", sessionId);
            map.put("name", org.getName());
            map.put("authId", org.getAuthId());
            rtn.setObj(map);
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
}
