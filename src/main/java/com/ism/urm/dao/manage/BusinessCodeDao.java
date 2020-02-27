package com.ism.urm.dao.manage;

import java.sql.SQLException;

import org.hibernate.Session;

import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.manage.BusinessCode;

public class BusinessCodeDao extends BasicDao<BusinessCode>{

    public BusinessCodeDao() {
        // TODO Auto-generated constructor stub
    }

    public BusinessCode get(Session session, String id) throws SQLException {
        BusinessCode rtn = (BusinessCode) session.get(entityName, id);
        return rtn;
    }
    
    public String createId(Session session) throws SQLException {
     // TODO Auto-generated method stub
//      StringBuilder sb = new StringBuilder();
//      sb.append("MAP");
//      int seq = 0;
//      sb.append(String.format("%09d", seq));
      
      return (String) session.createSQLQuery("SELECT 'BIZ' || LPAD(BIZ_ID_SEQ.NEXTVAL,9,'0') AS bizId FROM DUAL")
                      .uniqueResult();
    }
}
