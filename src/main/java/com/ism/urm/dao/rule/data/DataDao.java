package com.ism.urm.dao.rule.data;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.ism.urm.dao.rule.RuleDao;
import com.ism.urm.vo.rule.data.Data;
import com.ism.urm.vo.rule.data.Field;
import com.ism.urm.vo.rule.data.UsedData;

public class DataDao extends RuleDao<Data> {
    
    private FieldDao fieldDao;
    
    public DataDao() {
        super();
        entityName = "DATA";
        fieldDao = new FieldDao();
    }

    public void delete(Session session, String id) throws Exception {
        fieldDao.deleteByDataId(session, id);
        session.createQuery("delete from " + entityName + " a where a.id = :id")
               .setString("id", id).executeUpdate();
    }

    public List<UsedData> usedList(Session session, String id) throws Exception {
        String hql = "select a.id, a.name, b.id, b.name from REQUEST a, DATAMAP b"
               + " where (a.reqDataMappingId = b.id or a.resDataMappingId = b.id)"
               + " and (b.sourceDataId = :dataId or b.targetDataId = :dataId)";
        List<Object[]> list = session.createQuery(hql).setString("dataId", id).list();
        List<UsedData> rtn = null;
        if (list != null) {
            rtn = new ArrayList<>();
            for (Object[] o : list) {
                UsedData vo = new UsedData();
                vo.setReqId((String) o[0]);
                vo.setReqName((String) o[1]);
                vo.setMapId((String) o[2]);
                vo.setMapName((String) o[3]);
                
                rtn.add(vo);
            }
        }
        return rtn;
    }

    @Override
    public String createId(Session session) throws Exception {
        // TODO Auto-generated method stub
//        StringBuilder sb = new StringBuilder();
//        sb.append("DAT");
//        int seq = 0;
//        sb.append(String.format("%09d", seq));
        
        return (String) session.createSQLQuery("SELECT 'DAT' || LPAD(DATA_ID_SEQ.NEXTVAL,9,'0') AS dataId FROM DUAL")
                               .uniqueResult();
    }

    @Override
    protected void setChild(Session session, Data vo) throws Exception {
        vo.setFields(fieldDao.listByDataId(session, vo.getId()));
    }

    @Override
    protected void beforeSave(Session session, Data vo) throws Exception {
        if (vo.getSubId()  == null || vo.getSubId().length() == 0) {
            vo.setSubId(vo.getId());
        }
    }

    @Override
    protected void saveChild(Session session, Data vo) throws Exception {
        List<Field> fields = vo.getFields();
        if (fields != null) {
            fieldDao.deleteByDataId(session, vo.getId());
            for (Field field : fields) {
                field.setDataId(vo.getId());
                if (field.getFieldId() == null || field.getFieldId().trim().length() == 0) {
                    String newId = fieldDao.createId(session);
                    if (newId == null) {
                        throw new Exception("failed to create ID");
                    }
                    field.setFieldId(newId);
                }
                
                fieldDao.save(session, field);
            }
        }
    }

}
