package com.ism.urm.dao;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;

public abstract class BasicDao<T> {

    protected Class<T> entityClass;
    protected String entityName;

    public BasicDao() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        entityClass = (Class<T>) type.getActualTypeArguments()[0];
    }

    public void save(Session session, T vo) throws SQLException {
        session.saveOrUpdate(entityName, vo);
    }

    public List<T> list(Session session) throws SQLException {
        return session.createCriteria(entityClass).list();
    }

    public abstract T get(Session session, String id) throws SQLException;

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public String getEntityName() {
        return entityName;
    }

}
