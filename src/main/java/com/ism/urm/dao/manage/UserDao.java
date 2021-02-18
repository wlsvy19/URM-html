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

    public int idCheck(Session session, String id) {
      Criteria crit = session.createCriteria(entityClass);
      crit.setProjection(Projections.rowCount()).add(Restrictions.eq("id", id));
      int rtn = ((Number) crit.uniqueResult()).intValue();
      return rtn;
    }
  
    @Override
    public User get(Session session, String id) throws Exception {
        User rtn = (User) session.get(entityName, id);
        return rtn;
    }

    @Override
    public void delete(Session session, String id) throws Exception {
        session.createQuery("delete from " + entityName + " a where a.id = :id")
               .setString("id", id).executeUpdate();
    }

    public int getInfluence(Session session, String id) throws Exception {
        String hql = "select count(*) from REQUEST a"
                + " where a.sendAdminId = :userId or a.rcvAdminId = :userId";
        int res = ((Number) session.createQuery(hql).setString("userId", id).uniqueResult()).intValue();
        return res;
    }
}
