package com.ism.urm.service.rule;

import java.util.List;

import org.hibernate.Session;

import com.ism.urm.dao.rule.mapping.DataMapDao;
import com.ism.urm.vo.rule.mapping.DataMap;


public class DataMapService extends RuleService<DataMap> {

    public DataMapService() {
        super();
        dao = new DataMapDao();
    }

    public List<DataMap> search() throws Exception {
        List<DataMap> rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            rtn = ((DataMapDao) dao).search(session);
        } catch (Exception e) {
            logger.error("Failed to search Datamap. ", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    @Override
    protected int getInfluence(String id) throws Exception {
        int res = 0;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            res = ((DataMapDao) dao).getInfluence(session, id);
        } catch (Exception e) {
            logger.error("Failed to getInfluence", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return res;
    }

}
