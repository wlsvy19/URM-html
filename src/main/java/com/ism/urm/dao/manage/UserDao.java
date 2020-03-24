package com.ism.urm.dao.manage;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.manage.User;

import javassist.bytecode.Descriptor.Iterator;

public class UserDao extends BasicDao<User> {
    protected SessionFactory sessionFactory;
    protected EntityManager entityManager;
    public UserDao() {
        entityName = "USER";
    }

    public int idCheck(Session session, String userId) {
        
        
        
//        List<Members> findMembers = em
//                .createQuery("select m from Members m where m.userID = :userID",Members.class)
//                .setParameter("userID", s)
//                .getResultList();
        
        
        // "SELECT COUNT(*) FROM TB_SSO_USER WHERE USER_ID = {userId}"
  
        //String SQL_QUERY = "SELECT COUNT(*) FROM USER WHERE USER_ID = :userId";
        //String SQL_QUERY = "SELECT COUNT(*) FROM TB_SSO_USER WHERE USER_ID = 'eai'";
        
        //query.setString("userId", userId);
     
        //return userList;
        //Query result = session.createQuery("SELECT COUNT(*) FROM " + entityName  + " WHERE USER_ID = :userId").setString("userId", userId);
       // Query query = session.createQuery(SQL_QUERY);
        
        
        
//      Criteria crit = session.createCriteria(User.class);
//      crit.add(Restrictions.idEq(userId));
//      ProjectionList projectionList = Projections.projectionList();
//      projectionList.add( Projections.alias( Projections.count("*"), "user_id") );   // count(sido) as sido_count
//       
//      crit.setProjection(projectionList);
//       
//      List<Integer> list = crit.list();
//       
//      for(int i = 0; i < list.size(); i++) {
//           System.out.println("*************************************************************");
//          System.out.println(list.get(i));
//       }
//         
//      crit.setProjection(Projections.rowCount());
//      List<Integer> codeList = crit.list();
//      System.out.println("count:" + codeList.get(0));
        
    
//        Criteria critCount = session.createCriteria(User.class);
//        long totalCount = ((Number) critCount.setProjection(Projections.rowCount()).uniqueResult()).longValue();
          
//        Query query = session.createQuery("SELECT COUNT(*) FROM USER u WHERE u.id=:userId");
//        System.out.println("UserDao userId: " + userId);
//        query.setString("userId", userId);
//        query.setParameter("userId", userId);
//        System.out.println("result: " + query.getFirstResult());
        
//        Long count =  (Long)query.uniqueResult();
//        System.out.println("*************count: " + count);
//        
//        for(java.util.Iterator it =  query.iterate(); it.hasNext();) {
//            long row = (Long) it.next();
//            System.out.println("Count: " + row);
//        }
        
//       CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//       CriteriaQuery<User> qdef = cb.createQuery(User.class);
//       Root<User> c = qdef.from(User.class);
//      
//       System.out.println(qdef.select(c).where(cb.equal(c.get("id"), userId)));
        
        //System.out.println(session.createSQLQuery("result: " + "SELECT COUNT(*) FROM USER u WHERE u.id \\=:userid").setString("userid", userId).uniqueResult());
        
        return 0;
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
