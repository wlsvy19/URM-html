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
	private UserDao dao;
	protected SessionFactory sessionFactory;

	static List<User> userList = new ArrayList<User>();

	public UserService() {
		dao = new UserDao();
		sessionFactory = HiberUtill.getSessionFactory();
	}

	public List<User> search(List<RelationOp> filter) throws Exception {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			List<User> rtn = dao.search(session, filter);
			return rtn;
		} catch (Exception e) {
			 logger.error("Failed to User search()", e);
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

	public User get(String id) throws Exception {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			User rtn = dao.get(session, id);
			return rtn;
		} catch (Exception e) {
			logger.error("Failed to User get()", e);
		} finally {
			if (session != null)
				session.close();
		}
		return null;
	}
	
	public User save(User vo) throws Exception{
		Session session = null;
		Transaction tx =null;
		
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			
			dao.save(session, vo);
			tx.commit();
			
			User rtn = dao.get(session, vo.getId());
			return rtn;	
		}catch(Exception e) {
			logger.error("Failed to User save()", e);
		}finally {
			if(session != null) session.close();
		}
		return null;
	}

}
