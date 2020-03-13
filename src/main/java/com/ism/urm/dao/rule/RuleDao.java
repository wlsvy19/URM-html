package com.ism.urm.dao.rule;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import com.ism.urm.dao.BasicDao;
import com.ism.urm.vo.PagingResult;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.rule.RuleVo;

public abstract class RuleDao<T extends RuleVo> extends BasicDao<T> {

    public RuleDao() {
    }

    public T get(Session session, String id) throws SQLException {
        T rtn = getById(session, id);
        setChild(session, rtn);
        System.out.println("rtn: " + rtn);
        return rtn;
    }

    public PagingResult<T> searchPage(Session session, int page, int size, List<RelationOp> filter) {
        PagingResult<T> ret = new PagingResult<T>();
        ret.setPageSize(Integer.valueOf(0));
        ret.setCurPage(page);
        if (size <= 0) {
            size = 30;
        }

        Criteria critCount = session.createCriteria(entityName);
        if (filter != null) {
            for (RelationOp op : filter) {
                critCount.add(op.getCriterion());
            }
        }
        long totalCount = ((Number) critCount.setProjection(Projections.rowCount()).uniqueResult()).longValue();
        ret.setTotalCount(totalCount);

        if (totalCount > 0) {
            int mod = (int)(totalCount % size);
            int div = (int)(totalCount / size);

            if (mod == 0) {
                ret.setPageSize(div);
            } else {
                ret.setPageSize(div + 1);
            }

            Criteria crit = session.createCriteria(entityName);
            if (filter != null) {
                for (RelationOp op : filter) {
                    crit.add(op.getCriterion());
                }
            }
            
            crit.setFirstResult((page - 1) * size);
            crit.setMaxResults(size);
            
            List<T> list = crit.list();
            if (list == null) {
                list = new ArrayList<>();
            }
            ret.setList(list);
        }
        session.flush();
        session.clear();
        return ret;
    }

    public abstract String createId(Session session) throws SQLException;
    
    protected abstract void setChild(Session session, T vo) throws SQLException;
}
