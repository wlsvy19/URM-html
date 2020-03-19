package com.ism.urm.service.rule;

import com.ism.urm.dao.rule.mapping.DataMapDao;
import com.ism.urm.vo.rule.mapping.DataMap;


public class DataMapService extends RuleService<DataMap> {

    public DataMapService() {
        super();
        dao = new DataMapDao();
    }

}
