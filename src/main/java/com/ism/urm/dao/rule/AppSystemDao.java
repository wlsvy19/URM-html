package com.ism.urm.dao.rule;

import java.util.List;

import org.hibernate.Session;

import com.ism.urm.vo.rule.system.AppSystem;

public class AppSystemDao extends RuleDao<AppSystem> {
    
    public AppSystemDao() {
        super();
        entityName = "APPSYSTEM";
    }

    public void delete(Session session, String id) throws Exception {
        session.createQuery("delete from " + entityName + " a where a.id = :id")
               .setString("id", id).executeUpdate();
    }

    @Override
    public String createId(Session session) throws Exception {
        // TODO Auto-generated method stub
//        StringBuilder sb = new StringBuilder();
//        sb.append("SYS");
//        int seq = 0;
//        sb.append(String.format("%09d", seq));
        
        return (String) session.createSQLQuery("SELECT 'SYS' || LPAD(SYS_ID_SEQ.NEXTVAL, 9, '0') as sysId from DUAL")
                               .uniqueResult();
    }

    @Override
    protected void setChild(Session session, AppSystem vo) throws Exception {
        // do nothing
    }

    @Override
    protected void beforeSave(Session session, AppSystem vo) throws Exception {
        // do nothing
    }

    @Override
    protected void saveChild(Session session, AppSystem vo) throws Exception {
        // do nothing
    }

    public int getInfluence(Session session, String id) throws Exception {
        String hql = "select count(*) from REQUEST a"
                + " where a.sendSystemId = :systemId or a.rcvSystemId = :systemId";
        int res = ((Number) session.createQuery(hql).setString("systemId", id).uniqueResult()).intValue();
        return res;
    }
}
