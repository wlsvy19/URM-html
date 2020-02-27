package com.ism.urm.dao.rule.data;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;

import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.rule.data.Field;

public class FieldDao extends BasicDao<Field> {

    public Field get(Session session, String id) throws SQLException {
        return (Field) session.get(entityName, id);
    }

    public List<Field> listByDataId(Session session, String dataId) throws SQLException {
        String hql = "from FIELD"
                + " where dataId = :dataId"
                + " order by cast(sno as int), cast(rsno as int)"; //TODO DB test exclude oracle
        List<Field> rtn = session.createQuery(hql)
                                 .setString("dataId", dataId).list();
        return rtn;
    }

    public void delete(Session session, String key) throws SQLException {
        //session.delete(key, new Request());
    }

    public String createId(Session session) throws SQLException {
        // TODO Auto-generated method stub
//        StringBuilder sb = new StringBuilder();
//        sb.append("FLD");
//        int seq = 0;
//        sb.append(String.format("%09d", seq));
        
        return (String) session.createSQLQuery("SELECT 'FLD' || LPAD(DATA_FLD_ID_SEQ.NEXTVAL,9,'0') AS dataId FROM DUAL")
                               .uniqueResult();
    }

}
