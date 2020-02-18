package com.ism.urm.dao.rule;

import java.sql.SQLException;

import org.hibernate.Session;

import com.ism.urm.vo.rule.AppSystem;

public class AppSystemDao extends RuleDao<AppSystem> {
    
    public AppSystemDao() {
        super();
        entityName = "APPSYSTEM";
    }

    public void delete(Session session, String key) throws SQLException {
        //session.delete(key, new Request());
    }

//    public int insertHistory(String reqId) throws SQLException {
//        int res = 0 ; 
//        getSqlMapClientTemplate().insert("Request.history.insert",reqId);
//        res = 1;
//        return res;
//    }

//    public List<RequestExcel> listExcel(Map map)throws SQLException {
//        List<RequestExcel> list = getSqlMapClientTemplate().queryForList("Request.listExcel",map);
//        return list;
//    }


//    public List<Request> getHistory(String key)throws SQLException {
//        
//        List<Request> list = getSqlMapClientTemplate().queryForList("Request.history",key);
//        return list;
//    }

    @Override
    public String createId(Session session) throws SQLException {
        // TODO Auto-generated method stub
//        StringBuilder sb = new StringBuilder();
//        sb.append("SYS");
//        int seq = 0;
//        sb.append(String.format("%09d", seq));
        
        return (String) session.createSQLQuery("SELECT 'SYS' || LPAD(SYS_ID_SEQ.NEXTVAL, 9, '0') as sysId from DUAL").uniqueResult();
    }

    @Override
    protected void setChild(Session session, AppSystem vo) throws SQLException {
        // do nothing
    }

}
