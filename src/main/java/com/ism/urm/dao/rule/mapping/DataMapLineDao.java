package com.ism.urm.dao.rule.mapping;

import java.util.List;

import org.hibernate.Session;

import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.rule.mapping.DataMapLine;

public class DataMapLineDao extends BasicDao<DataMapLine> {

    public DataMapLineDao() {
        super();
        entityName = "DATAMAPLINE";
    }

    public List<DataMapLine> listByMapId(Session session, String mapId) throws Exception {
        List<DataMapLine> rtn = session.createQuery("from " + entityName + " a where a.mapId = :mapId order by lineSeq")
                                 .setString("mapId", mapId).list();
        return rtn;
    }

    public void deleteByMapId(Session session, String mapId) throws Exception {
        session.createQuery("delete from " + entityName + " a where a.mapId = :mapId")
               .setString("mapId", mapId).executeUpdate();
    }

    @Override
    public DataMapLine get(Session session, String id) throws Exception {
        return null;
    }

    @Override
    public void delete(Session session, String id) throws Exception {
        // do nothing
    }

}
