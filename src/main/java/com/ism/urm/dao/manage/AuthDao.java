package com.ism.urm.dao.manage;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.manage.Auth;

public class AuthDao extends BasicDao<Auth>{
    
    public AuthDao() {
        entityName = "AUTH";
    }
    
    @Override
    public Auth get(Session session, String id) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<String> getAuth(Session session) throws SQLException {
        return session.createCriteria(Auth.class).setProjection(Projections.property("authName")).list();
    }

    public List<Auth> list(Session session, String authName) throws SQLException {
        return session.createCriteria(Auth.class).add(Restrictions.eq("authName", authName)).list();
    }

    @Override
    public void delete(Session session, String id) throws Exception {
        // TODO Auto-generated method stub
    }

}
