package com.ism.urm.service.manage;

import com.ism.urm.dao.manage.UserDao;
import com.ism.urm.service.rule.RuleService;
import com.ism.urm.vo.manage.User;

public class UserService extends RuleService<User> {
	private UserDao userDao;
	private static int _seq = 0;
	private static Object _lock = new Object();

	public UserService() {
		super();
		dao = new UserDao();
		userDao = (UserDao) dao;
	}
}
  