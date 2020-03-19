package com.ism.urm.dao.rule;

import java.sql.SQLException;

import org.hibernate.Session;

import com.ism.urm.vo.rule.system.AppSystem;

public class AppSystemDao extends RuleDao<AppSystem> {
    
    public AppSystemDao() {
        super();
        entityName = "APPSYSTEM";
    }

    public void delete(Session session, String id) throws SQLException {
        // TODO Auto-generated method stub
    }

    @Override
    public String createId(Session session) throws SQLException {
        // TODO Auto-generated method stub
//        StringBuilder sb = new StringBuilder();
//        sb.append("SYS");
//        int seq = 0;
//        sb.append(String.format("%09d", seq));
        
        return (String) session.createSQLQuery("SELECT 'SYS' || LPAD(SYS_ID_SEQ.NEXTVAL, 9, '0') as sysId from DUAL")
                               .uniqueResult();
    }

    @Override
    protected void setChild(Session session, AppSystem vo) throws SQLException {
        // do nothing
    }

    @Override
    protected void beforeSave(Session session, AppSystem vo) throws SQLException {
        // do nothing
    }

    @Override
    protected void saveChild(Session session, AppSystem vo) throws SQLException {
        // do nothing
    }

}
