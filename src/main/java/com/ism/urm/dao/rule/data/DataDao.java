package com.ism.urm.dao.rule.data;

import java.sql.SQLException;

import org.hibernate.Session;

import com.ism.urm.dao.rule.RuleDao;
import com.ism.urm.vo.rule.data.Data;

public class DataDao extends RuleDao<Data> {
    
    private FieldDao fieldDao;
    
    public DataDao() {
        super();
        entityName = "DATA";
        fieldDao = new FieldDao();
    }

    public void delete(Session session, String key) throws SQLException {
        //session.delete(key, new Request());
    }

    @Override
    public String createId(Session session) throws SQLException {
        // TODO Auto-generated method stub
//        StringBuilder sb = new StringBuilder();
//        sb.append("DAT");
//        int seq = 0;
//        sb.append(String.format("%09d", seq));
        
        return (String) session.createSQLQuery("SELECT 'DAT' || LPAD(DATA_ID_SEQ.NEXTVAL,9,'0') AS dataId FROM DUAL")
                               .uniqueResult();
    }

    @Override
    protected void setChild(Session session, Data vo) throws SQLException {
        vo.setFields(fieldDao.listByDataId(session, vo.getId()));
    }

}
