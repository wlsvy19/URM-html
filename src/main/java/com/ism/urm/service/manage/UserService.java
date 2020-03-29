package com.ism.urm.service.manage;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ism.urm.dao.manage.UserDao;
import com.ism.urm.vo.HiberUtill;
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
            rtn = userDao.getUserById(session, userId);

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

   
}
