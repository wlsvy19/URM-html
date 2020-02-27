package com.ism.urm.dao.rule;

import java.sql.SQLException;

import org.hibernate.Session;

import com.ism.urm.dao.rule.RuleDao;
import com.ism.urm.dao.rule.data.DataDao;
import com.ism.urm.vo.rule.mapping.DataMap;

public class DataMapDao extends RuleDao<DataMap> {
    
    private DataDao dataDao;
    
    public DataMapDao() {
        super();
        entityName = "DATAMap";
        dataDao = new DataDao();
    }

    public void delete(Session session, String key) throws SQLException {
        //session.delete(key, new Request());
    }

    @Override
    public String createId(Session session) throws SQLException {
        // TODO Auto-generated method stub
//        StringBuilder sb = new StringBuilder();
//        sb.append("MAP");
//        int seq = 0;
//        sb.append(String.format("%09d", seq));
        
        return (String) session.createSQLQuery("SELECT 'MAP' || LPAD(MAP_ID_SEQ.NEXTVAL,9,'0') AS dataId FROM DUAL")
                               .uniqueResult();
    }

    @Override
    protected void setChild(Session session, DataMap vo) throws SQLException {
        
    }

}
