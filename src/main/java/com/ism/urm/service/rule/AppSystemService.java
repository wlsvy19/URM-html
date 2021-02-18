package com.ism.urm.service.rule;

import org.hibernate.Session;

import com.ism.urm.dao.rule.AppSystemDao;
import com.ism.urm.vo.rule.system.AppSystem;


public class AppSystemService extends RuleService<AppSystem> {

    public AppSystemService() {
        super();
        dao = new AppSystemDao();
    }

    @Override
    protected int getInfluence(String id) throws Exception {
        int res = 0;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            res = ((AppSystemDao) dao).getInfluence(session, id);
        } catch (Exception e) {
            logger.error("Failed to getInfluence", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return res;
    }

}
