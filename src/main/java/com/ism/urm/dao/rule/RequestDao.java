package com.ism.urm.dao.rule;

import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.ism.urm.dao.manage.BusinessCodeDao;
import com.ism.urm.dao.manage.UserDao;
import com.ism.urm.vo.rule.request.Request;
import com.ism.urm.vo.rule.request.RequestHistory;

public class RequestDao extends RuleDao<Request> {

    private String historyEntity;
    private AppSystemDao sysDao;
    private BusinessCodeDao bizDao;
    private UserDao usrDao;
    
    public RequestDao() {
        super();
        entityName = "REQUEST";
        historyEntity = "REQUESTHISTORY";
        sysDao = new AppSystemDao();
        bizDao = new BusinessCodeDao();
        usrDao = new UserDao();
    }

    public List<Request> listByManager(Session session, String userId) throws Exception {
        Criteria crit = session.createCriteria(entityName);
        crit.add(Restrictions.disjunction(Restrictions.eq("regId", userId),
                                          Restrictions.eq("sendAdminId", userId),
                                          Restrictions.eq("rcvAdminId", userId)));
        crit.add(Restrictions.ne("chgStat", "4"));
        crit.add(Restrictions.ne("delYN", false));
        
        crit.addOrder(Order.desc("chgDate"));
        
        List<Request> list = crit.list();
        return list;
    }

    public void delete(Session session, String id) throws Exception {
        session.createQuery("update " + entityName + " a set a.delYN = :delYN where a.id = :id")
               .setBoolean("delYN", true).setString("id", id).executeUpdate();
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


    public List<RequestHistory> getHistory(Session session, String key)throws Exception {
        List<RequestHistory> list = session.createCriteria(historyEntity)
                                           .add(Restrictions.eq("id", key))
                                           .addOrder(Order.desc("chgDate"))
                                           .list();
        return list;
    }

    @Override
    public String createId(Session session) throws Exception {
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
    protected void setChild(Session session, Request vo) throws Exception {
        vo.setSendSystem(sysDao.get(session, vo.getSendSystemId()));
        vo.setRcvSystem(sysDao.get(session, vo.getRcvSystemId()));
        
        vo.setSendJobCode(bizDao.get(session, vo.getSendJobCodeId()));
        vo.setRcvJobCode(bizDao.get(session, vo.getRcvJobCodeId()));
        
        vo.setSendAdmin(usrDao.get(session, vo.getSendAdminId()));
        vo.setRcvAdmin(usrDao.get(session, vo.getRcvAdminId()));
    }

    @Override
    protected void beforeSave(Session session, Request vo) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        vo.setOpenYMD(format.format(vo.getChgDate()));
    }

    @Override
    protected void saveChild(Session session, Request vo) throws Exception {
        RequestHistory history = new RequestHistory(vo);
        String newId = createHistoryId(session);
        if (newId == null) {
            throw new Exception("failed to create history ID");
        }
        history.setChgSeq(newId);
        session.saveOrUpdate(historyEntity, history);
    }

    private String createHistoryId(Session session) {
//        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
//        String ymd = format.format(new Date());
//        
//        RequestHistory last =
//                (RequestHistory) session.createCriteria(RequestHistory.class)
//                                        .add(Restrictions.like("chgSeq", "CHG"+ymd, MatchMode.START))
//                                        .addOrder(Order.desc("chgDate"))
//                                        .setMaxResults(1)
//                                        .uniqueResult();
//        String lastId = last.getChgSeq();
//        lastId.substring(lastId.length()-5);
        String id = (String) session.createSQLQuery("SELECT 'CHG' || TO_CHAR(SYSDATE,'YYMMDD') || LPAD(REQ_CHG_SEQ.NEXTVAL,5,'0') FROM DUAL")
                                    .uniqueResult();
        return id;
    }

}
