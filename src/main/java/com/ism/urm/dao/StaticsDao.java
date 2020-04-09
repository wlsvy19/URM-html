package com.ism.urm.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.ism.urm.vo.statics.RequestChangeCount;
import com.ism.urm.vo.statics.RequestProcessCount;
import com.ism.urm.vo.statics.Statics;

public class StaticsDao {

    public StaticsDao() {
        super();
    }

    public List<RequestProcessCount> getRequestProcessCount(Session session, int type, Map<String, String> map) {
        List<RequestProcessCount> rtn = new ArrayList<>();
        String sql1 = "SELECT " +
                "chgDate, " +
                "NVL(stat1, 0) stat1, " +
                "NVL(stat2, 0) stat2, " +
                "NVL(stat3, 0) stat3, " +
                "NVL(stat4, 0) stat4, " +
                "NVL(stat5, 0) stat5, " +
                "NVL(stat6, 0) stat6, " +
                "NVL(stat7, 0) stat7, " +
                "NVL(stat8, 0) stat8, " +
                "NVL(stat9, 0) stat9, " +
                "NVL(stat10, 0) stat10, " +
                "NVL(stat11, 0) stat11, " +
                "NVL(stat12, 0) stat12, " +
                "NVL(count, 0) count FROM (SELECT " +
                "'TOTAL' as chgDate, " +
                "sum(DECODE(PRC_STAT, '1', 1, 0))    as stat1, " +
                "sum(DECODE(PRC_STAT, '2', 1, 0))    as stat2, " +
                "sum(DECODE(PRC_STAT, '3', 1, 0))    as stat3, " +
                "sum(DECODE(PRC_STAT, '4', 1, 0))    as stat4, " +
                "sum(DECODE(PRC_STAT, '5', 1, 0))    as stat5, " +
                "sum(DECODE(PRC_STAT, '6', 1, 0))    as stat6, " +
                "sum(DECODE(PRC_STAT, '7', 1, 0))    as stat7, " +
                "sum(DECODE(PRC_STAT, '8', 1, 0))    as stat8, " +
                "sum(DECODE(PRC_STAT, '9', 1, 0))    as stat9, " +
                "sum(DECODE(PRC_STAT, '10', 1, 0))   as stat10, " +
                "sum(DECODE(PRC_STAT, '11', 1, 0))   as stat11, " +
                "sum(DECODE(PRC_STAT, '12', 1, 0))   as stat12, " +
                "sum(1)                          as count, " +
                "100                 align " +
                "FROM TB_REQ_MST ";
        String sql2 = "    sum(DECODE(PRC_STAT, '1', 1, 0))    as stat1, " +
                "    sum(DECODE(PRC_STAT, '2', 1, 0))    as stat2, " +
                "    sum(DECODE(PRC_STAT, '3', 1, 0))    as stat3, " +
                "    sum(DECODE(PRC_STAT, '4', 1, 0))    as stat4, " +
                "    sum(DECODE(PRC_STAT, '5', 1, 0))    as stat5, " +
                "    sum(DECODE(PRC_STAT, '6', 1, 0))    as stat6, " +
                "    sum(DECODE(PRC_STAT, '7', 1, 0))    as stat7, " +
                "    sum(DECODE(PRC_STAT, '8', 1, 0))    as stat8, " +
                "    sum(DECODE(PRC_STAT, '9', 1, 0))    as stat9, " +
                "    sum(DECODE(PRC_STAT, '10', 1, 0))   as stat10, " +
                "    sum(DECODE(PRC_STAT, '11', 1, 0))   as stat11, " +
                "    sum(DECODE(PRC_STAT, '12', 1, 0))   as stat12, " +
                "    sum(1)                          as count, " +
                "    200                 align " +
                "FROM TB_REQ_MST ";
        
        StringBuilder sb = new StringBuilder();
        sb.append(sql1);
        if(type == 1) {
            sb.append("WHERE TO_CHAR(CHG_DATE, 'YYYYMMDD') BETWEEN :startDate and :endDate " +
                "  AND IF_TYPE like DECODE(:interfaceType,    NULL,'%%', :interfaceType) " +
                "  AND CHG_STAT <> '4' AND DEL_YN <> 'Y' " +
                "UNION ALL SELECT " +
                "    TO_CHAR(CHG_DATE, 'YYYY-MM-DD') as chgDate, ");
            sb.append(sql2);
            sb.append("WHERE TO_CHAR(CHG_DATE, 'YYYYMMDD') BETWEEN :startDate and :endDate " +
                "  AND IF_TYPE like DECODE(:interfaceType,    NULL,'%%', :interfaceType) " +
                "  AND CHG_STAT <> '4' AND DEL_YN <> 'Y' " +
                "GROUP BY TO_CHAR(CHG_DATE, 'YYYY-MM-DD')    , 100 " +
                "ORDER BY align, chgDate" +
                ")");
            
            List<Object[]> list = session.createSQLQuery(sb.toString()).setString("startDate", map.get("startDate"))
                   .setString("endDate", map.get("endDate"))
                   .setString("interfaceType", map.get("interfaceType"))
                   .list();
            for (Object[] o : list) {
                RequestProcessCount rpc = new RequestProcessCount();
                rpc.setChgDate((String) o[0]);
                rpc.setStat1(((BigDecimal)o[1]).intValue());
                rpc.setStat2(((BigDecimal)o[2]).intValue());
                rpc.setStat3(((BigDecimal)o[3]).intValue());
                rpc.setStat4(((BigDecimal)o[4]).intValue());
                rpc.setStat5(((BigDecimal)o[5]).intValue());
                rpc.setStat6(((BigDecimal)o[6]).intValue());
                rpc.setStat7(((BigDecimal)o[7]).intValue());
                rpc.setStat8(((BigDecimal)o[8]).intValue());
                rpc.setStat9(((BigDecimal)o[9]).intValue());
                rpc.setStat10(((BigDecimal)o[10]).intValue());
                rpc.setStat11(((BigDecimal)o[11]).intValue());
                rpc.setStat12(((BigDecimal)o[12]).intValue());
                rpc.setCount(((BigDecimal)o[13]).intValue());
                rtn.add(rpc);
            }
        } else {
            sb.append("WHERE TO_CHAR(CHG_DATE, 'YYYYMM') BETWEEN :startDate and :endDate " +
                "  AND IF_TYPE like DECODE(:interfaceType,    NULL,'%%', :interfaceType) " +
                "  AND CHG_STAT <> '4' AND DEL_YN <> 'Y' " +
                "UNION ALL SELECT " +
                "    TO_CHAR(CHG_DATE, 'YYYY-MM') as chgDate, ");
            sb.append(sql2);
            sb.append("WHERE TO_CHAR(CHG_DATE, 'YYYYMM') BETWEEN :startDate and :endDate " +
                "  AND IF_TYPE like DECODE(:interfaceType,    NULL,'%%', :interfaceType) " +
                "  AND CHG_STAT <> '4' AND DEL_YN <> 'Y' " +
                "GROUP BY TO_CHAR(CHG_DATE, 'YYYY-MM')    , 100 " +
                "ORDER BY align, chgDate" +
                ")");
                    
            List<Object[]> list = session.createSQLQuery(sb.toString()).setString("startDate", map.get("startDate"))
                   .setString("endDate", map.get("endDate"))
                   .setString("interfaceType", map.get("interfaceType"))
                   .list();
            for (Object[] o : list) {
                RequestProcessCount rpc = new RequestProcessCount();
                rpc.setChgDate((String) o[0]);
                rpc.setStat1(((BigDecimal)o[1]).intValue());
                rpc.setStat2(((BigDecimal)o[2]).intValue());
                rpc.setStat3(((BigDecimal)o[3]).intValue());
                rpc.setStat4(((BigDecimal)o[4]).intValue());
                rpc.setStat5(((BigDecimal)o[5]).intValue());
                rpc.setStat6(((BigDecimal)o[6]).intValue());
                rpc.setStat7(((BigDecimal)o[7]).intValue());
                rpc.setStat8(((BigDecimal)o[8]).intValue());
                rpc.setStat9(((BigDecimal)o[9]).intValue());
                rpc.setStat10(((BigDecimal)o[10]).intValue());
                rpc.setStat11(((BigDecimal)o[11]).intValue());
                rpc.setStat12(((BigDecimal)o[12]).intValue());
                rpc.setCount(((BigDecimal)o[13]).intValue());
                rtn.add(rpc);
            }
        }
        return rtn;
    }
    
    public List<RequestChangeCount> getRequestChangeCount(Session session, int type, Map<String, String> map) {
        List<RequestChangeCount> rtn = new ArrayList<>();
        String st = "SELECT "
                + "'TOTAL' as chgDate, ";
        String field = "sum(DECODE(CHG_STAT, '1', 1, 0))    as stat1, "
                + "sum(DECODE(CHG_STAT, '1', DECODE(IF_TYPE, '1', 1, 0), 0))   as stat1o, "
                + "sum(DECODE(CHG_STAT, '1', DECODE(IF_TYPE, '2', 1, 0), 0))   as stat1b, "
                + "sum(DECODE(CHG_STAT, '1', DECODE(IF_TYPE, '3', 1, 0), 0))   as stat1d, "
                + "sum(DECODE(CHG_STAT, '2', 1, 0))    as stat2, "
                + "sum(DECODE(CHG_STAT, '2', DECODE(IF_TYPE, '1', 1, 0), 0))   as stat2o, "
                + "sum(DECODE(CHG_STAT, '2', DECODE(IF_TYPE, '2', 1, 0), 0))   as stat2b, "
                + "sum(DECODE(CHG_STAT, '2', DECODE(IF_TYPE, '3', 1, 0), 0))   as stat2d, "
                + "sum(DECODE(CHG_STAT, '3', 1, 0))    as stat3, "
                + "sum(DECODE(CHG_STAT, '3', DECODE(IF_TYPE, '1', 1, 0), 0))   as stat3o, "
                + "sum(DECODE(CHG_STAT, '3', DECODE(IF_TYPE, '2', 1, 0), 0))   as stat3b, "
                + "sum(DECODE(CHG_STAT, '3', DECODE(IF_TYPE, '3', 1, 0), 0))   as stat3d, "
                + "sum(DECODE(CHG_STAT, '4', 1, 0))    as stat4, "
                + "sum(DECODE(CHG_STAT, '4', DECODE(IF_TYPE, '1', 1, 0), 0))   as stat4o, "
                + "sum(DECODE(CHG_STAT, '4', DECODE(IF_TYPE, '2', 1, 0), 0))   as stat4b, "
                + "sum(DECODE(CHG_STAT, '4', DECODE(IF_TYPE, '3', 1, 0), 0))   as stat4d, "
                + "sum(1) as total "
                + "FROM TB_REQ_MST ";
        
        StringBuilder sb = new StringBuilder();
        sb.append(st).append(field);
        if(type == 1) {
            sb.append("WHERE TO_CHAR(CHG_DATE, 'YYYYMMDD') BETWEEN :startDate and :endDate " +
                "  GROUP BY 'TOTAL' " +
                "UNION ALL SELECT " +
                "    TO_CHAR(CHG_DATE, 'YYYY-MM-DD') as chgDate, ");
            sb.append(field);
            sb.append("WHERE TO_CHAR(CHG_DATE, 'YYYYMMDD') BETWEEN :startDate and :endDate " +
                "GROUP BY TO_CHAR(CHG_DATE, 'YYYY-MM-DD')    , 100 " +
                "ORDER BY chgDate");
            
            List<Object[]> list = session.createSQLQuery(sb.toString()).setString("startDate", map.get("startDate"))
                   .setString("endDate", map.get("endDate"))
                   .list();
            for (Object[] o : list) {
                RequestChangeCount rpc = new RequestChangeCount();
                rpc.setChgDate((String) o[0]);
                rpc.setStat1(((BigDecimal)o[1]).intValue());
                rpc.setStat1o(((BigDecimal)o[2]).intValue());
                rpc.setStat1b(((BigDecimal)o[3]).intValue());
                rpc.setStat1d(((BigDecimal)o[4]).intValue());
                rpc.setStat2(((BigDecimal)o[5]).intValue());
                rpc.setStat2o(((BigDecimal)o[6]).intValue());
                rpc.setStat2b(((BigDecimal)o[7]).intValue());
                rpc.setStat2d(((BigDecimal)o[8]).intValue());
                rpc.setStat3(((BigDecimal)o[9]).intValue());
                rpc.setStat3o(((BigDecimal)o[10]).intValue());
                rpc.setStat3b(((BigDecimal)o[11]).intValue());
                rpc.setStat3d(((BigDecimal)o[12]).intValue());
                rpc.setStat4(((BigDecimal)o[13]).intValue());
                rpc.setStat4o(((BigDecimal)o[14]).intValue());
                rpc.setStat4b(((BigDecimal)o[15]).intValue());
                rpc.setStat4d(((BigDecimal)o[16]).intValue());
                rpc.setCount(((BigDecimal)o[17]).intValue());
                rtn.add(rpc);
            }
        } else {
            sb.append("WHERE TO_CHAR(CHG_DATE, 'YYYYMM') BETWEEN :startDate and :endDate " +
                    "  GROUP BY 'TOTAL' " +
                    "UNION ALL SELECT " +
                    "    TO_CHAR(CHG_DATE, 'YYYY-MM') as chgDate, ");
                sb.append(field);
                sb.append("WHERE TO_CHAR(CHG_DATE, 'YYYYMM') BETWEEN :startDate and :endDate " +
                    "GROUP BY TO_CHAR(CHG_DATE, 'YYYY-MM')    , 100 " +
                    "ORDER BY chgDate");
                
                List<Object[]> list = session.createSQLQuery(sb.toString()).setString("startDate", map.get("startDate"))
                       .setString("endDate", map.get("endDate"))
                       .list();
                for (Object[] o : list) {
                    RequestChangeCount rpc = new RequestChangeCount();
                    rpc.setChgDate((String) o[0]);
                    rpc.setStat1(((BigDecimal)o[1]).intValue());
                    rpc.setStat1o(((BigDecimal)o[2]).intValue());
                    rpc.setStat1b(((BigDecimal)o[3]).intValue());
                    rpc.setStat1d(((BigDecimal)o[4]).intValue());
                    rpc.setStat2(((BigDecimal)o[5]).intValue());
                    rpc.setStat2o(((BigDecimal)o[6]).intValue());
                    rpc.setStat2b(((BigDecimal)o[7]).intValue());
                    rpc.setStat2d(((BigDecimal)o[8]).intValue());
                    rpc.setStat3(((BigDecimal)o[9]).intValue());
                    rpc.setStat3o(((BigDecimal)o[10]).intValue());
                    rpc.setStat3b(((BigDecimal)o[11]).intValue());
                    rpc.setStat3d(((BigDecimal)o[12]).intValue());
                    rpc.setStat4(((BigDecimal)o[13]).intValue());
                    rpc.setStat4o(((BigDecimal)o[14]).intValue());
                    rpc.setStat4b(((BigDecimal)o[15]).intValue());
                    rpc.setStat4d(((BigDecimal)o[16]).intValue());
                    rpc.setCount(((BigDecimal)o[17]).intValue());
                    rtn.add(rpc);
                }
        }
        return rtn;
    }

//    public List<Statics> staticsPrcStatOfDay(Map<String, String> map) {
//        
//        return getSqlMapClientTemplate().queryForList("RequestStatics.prcStatOfDay", map);
//    }
//
//    public List<Statics> staticsPrcStatOfMonth(Map<String, String> map) {
//        
//        return getSqlMapClientTemplate().queryForList("RequestStatics.prcStatOfMonth", map);
//    }
//
//    public List<Statics> staticsIfTypeOfDay(Map<String, String> map) {
//        
//        return getSqlMapClientTemplate().queryForList("RequestStatics.ifTypeOfDay", map);
//    }
//
//    public List<Statics> staticsIfTypeOfMonth(Map<String, String> map) {
//        
//        return getSqlMapClientTemplate().queryForList("RequestStatics.ifTypeOfMonth", map);
//    }
//
//    public List<Statics> staticsJobType() {
//        return getSqlMapClientTemplate().queryForList("RequestStatics.jobType");
//    }

}
