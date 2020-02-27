package com.ism.urm.dao.manage;

import java.sql.SQLException;

import org.hibernate.Session;

import com.ism.urm.dao.rule.RuleDao;
import com.ism.urm.vo.manage.User;

public class UserDao extends RuleDao<User> {

	public UserDao() {
		super();
		entityName = "USER";
	}

	@Override
	public String createId(Session session) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}  

	@Override
	protected void setChild(Session session, User vo) throws SQLException {
		// TODO Auto-generated method stub

	}

}
