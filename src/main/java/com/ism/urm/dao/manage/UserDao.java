package com.ism.urm.dao.manage;

import org.hibernate.Criteria;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.manage.User;

public class UserDao extends BasicDao<User> {
    protected SessionFactory sessionFactory;
    
    public UserDao() {
        entityName = "USER";
    }

    public int idCheck(Session session, String userID) {
      Criteria crit = session.createCriteria(User.class);
      crit.setProjection(Projections.rowCount()).add(Restrictions.eq("id", userID));
      Object queryResult = crit.uniqueResult();
      int rtn = Integer.parseInt(queryResult.toString());
      return rtn;
    }
  
    @Override
    public User get(Session session, String id) throws Exception {
        User rtn = (User) session.get("USER", id);
        return rtn;
    }

    @Override
    public void delete(Session session, String id) throws Exception {
        // TODO Auto-generated method stub
    }
}
