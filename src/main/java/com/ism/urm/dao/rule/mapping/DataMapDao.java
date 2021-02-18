package com.ism.urm.dao.rule.mapping;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.ism.urm.dao.rule.RuleDao;
import com.ism.urm.dao.rule.data.DataDao;
import com.ism.urm.vo.rule.mapping.DataMap;
import com.ism.urm.vo.rule.mapping.DataMapLine;
import com.ism.urm.vo.rule.mapping.DataMapValue;

public class DataMapDao extends RuleDao<DataMap> {

    private DataMapLineDao lineDao;
    private DataMapValueDao valueDao;
    private DataDao dataDao;
    
    public DataMapDao() {
        super();
        entityName = "DATAMAP";
        lineDao = new DataMapLineDao();
        valueDao = new DataMapValueDao();
        dataDao = new DataDao();
    }

    public List<DataMap> search(Session session) throws Exception {
        String hql = "select b.id, b.name, b.sourceDataId, b.targetDataId, a.id, a.name, a.interfaceId"
                + " from REQUEST a right join DATAMAP b"
                + " on (a.reqDataMappingId = b.id or a.resDataMappingId = b.id)"
                + " order by b.id desc";
        List<Object[]> list = session.createQuery(hql).list();
        List<DataMap> rtn = null;
        if (list != null) {
            rtn = new ArrayList<>();
            for (Object[] o : list) {
                DataMap map = new DataMap();
                map.setId((String) o[0]);
                map.setName((String) o[1]);
                map.setSourceDataId((String) o[2]);
                map.setTargetDataId((String) o[3]);
                map.setRequestId((String) o[4]);
                map.setRequestName((String) o[5]);
                map.setInterfaceId((String) o[6]);
                rtn.add(map);
            }
        }
        return rtn;
    }  

    public void delete(Session session, String id) throws Exception {
        lineDao.deleteByMapId(session, id);
        valueDao.deleteByMapId(session, id);
        session.createQuery("delete from " + entityName + " a where a.id = :id")
               .setString("id", id).executeUpdate();
    }

    @Override
    public String createId(Session session) throws Exception {
        // TODO Auto-generated method stub
//        StringBuilder sb = new StringBuilder();
//        sb.append("MAP");
//        int seq = 0;
//        sb.append(String.format("%09d", seq));
        
        return (String) session.createSQLQuery("SELECT 'MAP' || LPAD(MAP_ID_SEQ.NEXTVAL,9,'0') AS dataId FROM DUAL")
                               .uniqueResult();
    }

    @Override
    protected void setChild(Session session, DataMap vo) throws Exception {
        vo.setMapLines(lineDao.listByMapId(session, vo.getId()));
        vo.setMapValues(valueDao.listByMapId(session, vo.getId()));

        vo.setSourceData(dataDao.get(session, vo.getSourceDataId()));
        vo.setTargetData(dataDao.get(session, vo.getTargetDataId()));
    }

    @Override
    protected void beforeSave(Session session, DataMap vo) throws Exception {
        lineDao.deleteByMapId(session, vo.getId());
        valueDao.deleteByMapId(session, vo.getId());
    }

    @Override
    protected void saveChild(Session session, DataMap vo) throws Exception {
        List<DataMapLine> lines = vo.getMapLines();
        for (int i = 0; lines != null && i < lines.size(); i++) {
            DataMapLine line = lines.get(i);
            line.setMapId(vo.getId());
            line.setLineSeq(i);
            lineDao.save(session, line);
        }

        List<DataMapValue> vals = vo.getMapValues();
        for (int i = 0; vals != null && i < vals.size(); i++) {
            DataMapValue val = vals.get(i);
            val.setMapId(vo.getId());
            val.setValueSeq(i);
            valueDao.save(session, val);
        }
    }

    public int getInfluence(Session session, String id) throws Exception {
        String hql = "select count(*) from REQUEST a"
                + " where a.resDataMappingId = :mapId or a.reqDataMappingId = :mapId";
        int res = ((Number) session.createQuery(hql).setString("mapId", id).uniqueResult()).intValue();
        return res;
    }
}
