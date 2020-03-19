package com.ism.urm.dao.rule.data;

import java.util.List;

import org.hibernate.Session;

import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.rule.data.Field;

public class FieldDao extends BasicDao<Field> {

    public FieldDao() {
        super();
        entityName = "FIELD";
    }

    public Field get(Session session, String id) throws Exception {
        return null;
    }

    public List<Field> listByDataId(Session session, String dataId) throws Exception {
        String hql = "from " + entityName
                + " a where a.dataId = :dataId"
                + " order by cast(sno as int), cast(rsno as int)"; //TODO DB test exclude oracle
        List<Field> rtn = session.createQuery(hql)
                                 .setString("dataId", dataId).list();
        return rtn;
    }

    public void deleteByDataId(Session session, String dataId) throws Exception {
        session.createQuery("delete from " + entityName + " a where a.dataId = :dataId")
               .setString("dataId", dataId).executeUpdate();
    }

    public String createId(Session session) throws Exception {
        // TODO Auto-generated method stub
//        StringBuilder sb = new StringBuilder();
//        sb.append("FLD");
//        int seq = 0;
//        sb.append(String.format("%09d", seq));
        
        return (String) session.createSQLQuery("SELECT 'FLD' || LPAD(DATA_FLD_ID_SEQ.NEXTVAL,9,'0') AS dataId FROM DUAL")
                               .uniqueResult();
    }

    @Override
    public void delete(Session session, String id) throws Exception {
        // do nothing
    }

}
