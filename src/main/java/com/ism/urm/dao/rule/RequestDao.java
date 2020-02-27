package com.ism.urm.dao.rule;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Session;

import com.ism.urm.vo.rule.request.Request;

public class RequestDao extends RuleDao<Request> {
    
    private AppSystemDao sysDao;
    
    public RequestDao() {
        super();
        entityName = "REQUEST";
        sysDao = new AppSystemDao();
    }

    public void delete(Session session, String key) throws SQLException {
        session.delete(key, new Request());
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
        // TODO not use seq..
//        StringBuilder sb = new StringBuilder();
//        sb.append("REQ");
//        sb.append(new SimpleDateFormat("yyMMdd").format(new Date()));
//        int seq = 0;
//        sb.append(String.format("%05d", seq));
        
        return (String) session.createSQLQuery("SELECT 'REQ' || TO_CHAR(SYSDATE,'YYMMDD') || LPAD(REQ_ID_SEQ.NEXTVAL,5,'0') AS reqId FROM DUAL")
                               .uniqueResult();
    }

    @Override
    protected void setChild(Session session, Request vo) throws SQLException {
        vo.setSendSystem(sysDao.get(session, vo.getSendSystemId()));
        vo.setRcvSystem(sysDao.get(session, vo.getRcvSystemId()));
    }

}
