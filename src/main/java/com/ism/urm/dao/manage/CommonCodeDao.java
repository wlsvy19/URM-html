package com.ism.urm.dao.manage;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.manage.CommonCode;

public class CommonCodeDao extends BasicDao<CommonCode> {

    public CommonCodeDao() {
       entityName = "COMMONCODE";
    }

    public List<String> getKnd(Session session) throws SQLException {
        return session.createCriteria(CommonCode.class).setProjection(Projections.property("knd")).list();
    }

    public List<CommonCode> list(Session session, String knd) throws SQLException {
        return session.createCriteria(CommonCode.class).add(Restrictions.eq("knd", knd)).list();
    }

    @Override
    public CommonCode get(Session session, String id) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
    
//    public List<AuthCode> listAuthCode(Session session) throws SQLException {
//        return getSqlMapClientTemplate().queryForList("AuthCode.list");
//    }

}
