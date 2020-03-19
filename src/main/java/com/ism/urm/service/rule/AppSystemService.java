package com.ism.urm.service.rule;

import com.ism.urm.dao.rule.AppSystemDao;
import com.ism.urm.vo.rule.system.AppSystem;


public class AppSystemService extends RuleService<AppSystem> {

    public AppSystemService() {
        super();
        dao = new AppSystemDao();
    }

}
