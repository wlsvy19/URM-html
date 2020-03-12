package com.ism.urm.dao.manage;

import org.hibernate.Session;
import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.manage.User;

public class UserDao extends BasicDao<User> {

    public UserDao() {
    }

    public User get(Session session, String id) throws Exception {
        User rtn = getUserById(session, id);
        return rtn;
    }

    @Override
    public void delete(Session session, String id) throws Exception {
        // TODO Auto-generated method stub
    }
}  

//public class UserDao {
//    String entityName;
//    public UserDao() {
//    }
//
//    public User get(Session session, String id) throws SQLException {
//        User rtn = getById(session, id);
//        System.out.println("rtn:" + rtn);
//        return rtn;
//    }
//
//    public void setChild(Session session, User vo) throws SQLException {
//
//    }
//    
//    public User getById(Session session, String id) throws SQLException{
//        User rtn = (User) session.get(entityName, id);
//        return rtn;
//    }
//
//}

