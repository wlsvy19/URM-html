package com.ism.urm.vo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class RelationOp {

    public static enum ValueType {
        STRING, CHAR, NUMBER, DATE, STRING_CASE_SENSITIVE, LIST, BOOLEAN
    }

    public static enum OpType {
        EQ, LIKE, SLIKE, NE, LT, LE, GT, GE, ISNULL, ISNOTNULL, IN
    }

    public String fieldName;

    public Object val;

    public OpType opType = OpType.EQ;

    public ValueType valType = ValueType.STRING;

    public static RelationOp get(String fieldName, OpType opType, Object val, ValueType valType) {
        RelationOp rop = new RelationOp();
        rop.setFieldName(fieldName);
        rop.setVal(val);
        rop.setOpType(opType);
        rop.setValType(valType);
        return rop;
    }

    public ValueType getValType() {
        return valType;
    }
    public void setValType(ValueType valType) {
        this.valType = valType;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public OpType getOpType() {
        return opType;
    }
    public void setOpType(OpType opType) {
        this.opType = opType;
    }
    public Object getVal() {
        return val;
    }
    public void setVal(Object val) {
        this.val = val;
    }

    public Criterion getCriterion() {
        if (val == null) {
            if (opType == OpType.NE) {
                return Restrictions.isNotNull(fieldName);
            }
            return Restrictions.isNull(fieldName);
        }

        Object value = null;
        if (valType == ValueType.NUMBER) {
            value = new BigDecimal(val.toString());
        } else if (valType == ValueType.STRING) {
            value = val.toString();
        } else if (valType == ValueType.STRING_CASE_SENSITIVE) {
            value = val.toString().toUpperCase();
        } else if (valType == ValueType.DATE) {
            value = (Date) val;
            if (opType == OpType.LIKE || opType == OpType.SLIKE) {
                opType = OpType.EQ;
            }
        } else if (valType == ValueType.LIST) {
            value = val;
            opType = OpType.IN;
        } else if (valType == ValueType.BOOLEAN) {
            value = (boolean) val;
        } else {
            value = val;
        }

        Criterion criterion = null;
        if (opType == OpType.NE) {
            criterion = Restrictions.ne(fieldName, value);
        } else if (opType == OpType.GE) {
            criterion = Restrictions.ge(fieldName, value);
        } else if (opType == OpType.GT) {
            criterion = Restrictions.gt(fieldName, value);
        } else if (opType == OpType.LE) {
            criterion = Restrictions.le(fieldName, value);
        } else if (opType == OpType.LT) {
            criterion = Restrictions.lt(fieldName, value);
        } else if (opType == OpType.LIKE) {
            if (valType == ValueType.STRING_CASE_SENSITIVE) {
                criterion = Restrictions.ilike(fieldName, (String) value, MatchMode.ANYWHERE);
            } else {
                criterion = Restrictions.like(fieldName, (String) value, MatchMode.ANYWHERE);
            }
        } else if (opType == OpType.SLIKE) {
            if (valType == ValueType.STRING_CASE_SENSITIVE) {
                criterion = Restrictions.ilike(fieldName, (String) value, MatchMode.START);
            } else {
                criterion = Restrictions.like(fieldName, (String) value, MatchMode.START);
            }
        } else if (opType == OpType.ISNULL) {
            criterion = Restrictions.isNull(fieldName);
        } else if (opType == OpType.ISNOTNULL) {
            criterion = Restrictions.isNotNull(fieldName);
        } else if (opType == OpType.IN) {
            criterion = Restrictions.in(fieldName, (Collection<?>) value);
        } else {
            criterion = Restrictions.eq(fieldName, value);
        }
        return criterion;
    }
}
