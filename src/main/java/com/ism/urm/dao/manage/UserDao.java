package com.ism.urm.dao.manage;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.fasterxml.classmate.AnnotationConfiguration;
import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.manage.User;

import javassist.bytecode.Descriptor.Iterator;

public class UserDao extends BasicDao<User> {
    protected SessionFactory sessionFactory;

    public UserDao() {
    }

    public int idCheck(Session session, String userId) {
        // "SELECT COUNT(*) FROM TB_SSO_USER WHERE USER_ID = {userId}"
        int count = 0;
        String SQL_QUERY = "SELECT COUNT(*) FROM TB_SSO_USER WHERE USER_ID =: ${userid}";

        Query query = session.createQuery(SQL_QUERY);
        query.setString("userid", userId);

        for (Iterator it = (Iterator) query.iterate(); it.hasNext();) {
            count = (Integer) it.next();
            System.out.println("count: " + count);
        }
        return count;
    }

    @Override
    public User get(Session session, String id) throws Exception {
        User rtn = getUserById(session, id);
        return rtn;
    }

    public User getUserById(Session session, String id) throws Exception {
        User rtn = (User) session.get("USER", id);
        return rtn;
    }

    @Override
    public void delete(Session session, String id) throws Exception {
        // TODO Auto-generated method stub
    }
}

// public class UserDao {
// String entityName;
// public UserDao() {
// }
//
// public User get(Session session, String id) throws SQLException {
// User rtn = getById(session, id);
// System.out.println("rtn:" + rtn);
// return rtn;
// }
//
// public void setChild(Session session, User vo) throws SQLException {
//
// }
//
// public User getById(Session session, String id) throws SQLException{
// User rtn = (User) session.get(entityName, id);
// return rtn;
// }
//
// }
