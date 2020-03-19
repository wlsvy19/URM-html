package com.ism.urm.dao.rule.mapping;

import java.util.List;

import org.hibernate.Session;

import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.rule.mapping.DataMapValue;

public class DataMapValueDao extends BasicDao<DataMapValue> {

    public DataMapValueDao() {
        super();
        entityName = "DATAMAPVALUE";
    }

    public List<DataMapValue> listByMapId(Session session, String mapId) throws Exception {
        List<DataMapValue> rtn = session.createQuery("from " + entityName + " a where a.mapId = :mapId order by valueSeq")
                                 .setString("mapId", mapId).list();
        return rtn;
    }

    public void deleteByMapId(Session session, String mapId) throws Exception {
        session.createQuery("delete from " + entityName + " a where a.mapId = :mapId")
               .setString("mapId", mapId).executeUpdate();
    }

    @Override
    public DataMapValue get(Session session, String id) throws Exception {
        return null;
    }

    @Override
    public void delete(Session session, String id) throws Exception {
        // do nothing
    }

}
