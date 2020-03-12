package com.ism.urm.service.manage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ism.urm.dao.manage.AuthDao;
import com.ism.urm.dao.manage.BusinessCodeDao;
import com.ism.urm.dao.manage.CommonCodeDao;
import com.ism.urm.dao.manage.UserDao;
import com.ism.urm.vo.HiberUtill;
import com.ism.urm.vo.manage.BusinessCode;
import com.ism.urm.vo.manage.CommonCode;
import com.ism.urm.vo.manage.User;
import com.ism.urm.vo.manage.Auth;

public class CodeService {

	private Logger logger = LoggerFactory.getLogger("debug");
	private SessionFactory sessionFactory;

	private CommonCodeDao commonCodeDao;
	private BusinessCodeDao businessCodeDao;
	private AuthDao authDao;

	static Map<String, List> commonCodeMap = new Hashtable<String, List>();
	
	static List<CommonCode> commonCodeList = new ArrayList<CommonCode>();
	static List<Auth> authList = new ArrayList<Auth>();
	static List<BusinessCode> businessCodeList = new ArrayList<BusinessCode>();

	public CodeService() {
		super();
		commonCodeDao = new CommonCodeDao();
		businessCodeDao = new BusinessCodeDao();
		authDao = new AuthDao();
		sessionFactory = HiberUtill.getSessionFactory();
	}

	static String noti = null;

	public List<CommonCode> getCommonCodeList() throws Exception {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			if (commonCodeList == null || commonCodeList.isEmpty()) {
				commonCodeList = commonCodeDao.list(session);
			}
			logger.debug("getCommonCodeList return");
			return commonCodeList;
		} catch (Exception e) {
			logger.error("Failed to get CommonCodeList. ", e);
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

	public List<Auth> getAuthList() throws Exception {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			if (authList == null || authList.isEmpty()) {
				authList = authDao.list(session);
			}
			logger.debug("getAuthList return");
			return authList;
		} catch (Exception e) {
			logger.error("Failed to get getAuthList. ", e);
			throw e;
		}
	}

	public List<CommonCode> getCommonCodeList(String knd) throws Exception {
		// if(knd == null) throw new NotFoundRequestException("Exception:Argment knd is
		// null");
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			if (commonCodeMap == null || commonCodeMap.isEmpty()) {
				////////////////////////////////////////////
				List<String> kndList = commonCodeDao.getKnd(session);
				for (String key : kndList) {
					List<CommonCode> codeList = commonCodeDao.list(session, key);
					commonCodeMap.put(key, codeList);
				}
			}
			return commonCodeMap.get(knd);
		} catch (Exception e) {
			logger.error("Failed to get CommonCodeList(" + knd + ")", e);
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

	public List<BusinessCode> getBusinessCodeList() throws Exception {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			if (businessCodeList == null || businessCodeList.isEmpty()) {
				businessCodeList = businessCodeDao.list(session);
			}
			logger.debug("getBusinessCodeList return");
			return businessCodeList;
		} catch (Exception e) {
			logger.error("Failed to get BusinessCodeList", e);
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

	public void addBusinessCode(BusinessCode code) throws Exception {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			businessCodeDao.save(session, code);
			tx.commit();

			businessCodeList = businessCodeDao.list(session);

		} catch (Exception e) {
			logger.error("Failed to save BusinessCode", e);
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

	public void setNoti(String noti) {
		CodeService.noti = noti;
		logger.info(noti);
	}

	public String getNoti() {
		return noti;
	}
}
