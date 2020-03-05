package com.ism.urm.service.rule;

import java.util.List;

import com.ism.urm.dao.rule.DataMapDao;
import com.ism.urm.vo.rule.mapping.DataMap;
import com.ism.urm.vo.rule.mapping.DataMapLine;
import com.ism.urm.vo.rule.mapping.DataMapValue;


public class DataMapService extends RuleService<DataMap> {

    public DataMapService() {
        super();
        dao = new DataMapDao();
    }

    public DataMap save(DataMap vo, boolean includeChild) throws Exception {
        DataMap rtn = null;
        try {
            if (includeChild) {
                List<DataMapLine> lines = vo.getMapLines();
                for (int i = 0; lines != null && i < lines.size(); i++) {
                    DataMapLine line = lines.get(i);
                    line.setMapId(vo.getId());
                    line.setLineSeq(i);
                }
    
                List<DataMapValue> vals = vo.getMapValues();
                for (int i = 0; vals != null && i < vals.size(); i++) {
                    DataMapValue val = vals.get(i);
                    val.setMapId(vo.getId());
                    val.setValSeq(i);
                }
            }
            
            rtn = save(vo);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }
}
