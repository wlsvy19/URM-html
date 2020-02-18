package com.ism.urm.dao.rule;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.ism.urm.vo.rule.RuleVo;

public abstract class RuleDao<T extends RuleVo> {
    
    protected Class<T> entityClass;
    protected String entityName;

    public RuleDao() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        entityClass = (Class<T>) type.getActualTypeArguments()[0];
    }
    
    public void save(Session session, T vo) throws SQLException {
        session.saveOrUpdate(entityName, vo);
    }
    
    public T get(Session session, String id) throws SQLException {
        T rtn = (T) session.get(entityName, id);
        setChild(session, rtn);
        return rtn;
    }
    
    public List<T> list(Session session) throws SQLException {
        return session.createCriteria(entityClass).list();
    }

    public List<T> search(Session session, Map<String, String> map)throws SQLException {
        Criteria crit = session.createCriteria(entityClass);
        
        if (map != null) {
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                crit.add(Restrictions.like(key, map.get(key)));
            }
        }
        
        List<T> list = crit.list();
        return list;
    }

    public abstract String createId(Session session) throws SQLException; 
    protected abstract void setChild(Session session, T vo) throws SQLException;
}
