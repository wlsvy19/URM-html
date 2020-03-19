package com.ism.urm.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.ism.urm.vo.RelationOp;

public abstract class BasicDao<T> {

    protected Class<T> entityClass;
    protected String entityName;


    public BasicDao() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        entityClass = (Class<T>) type.getActualTypeArguments()[0];
    }

    public void save(Session session, T vo) throws Exception {
        session.saveOrUpdate(entityName, vo);
    }

    public T getById(Session session, String id) throws Exception {
        T rtn = (T) session.get(entityName, id);
        return rtn;
    }

    public List<T> list(Session session) throws Exception {
        return search(session, null);
    }

    public List<T> search(Session session, List<RelationOp> filter) throws Exception {
        Criteria crit = session.createCriteria(entityName);

        if (filter != null) {
            for (RelationOp op : filter) {
                crit.add(op.getCriterion());
            }
        }

        return crit.list();
    }  

    public abstract T get(Session session, String id) throws Exception;
    public abstract void delete(Session session, String id) throws Exception;

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public String getEntityName() {
        return entityName;
    }

}
