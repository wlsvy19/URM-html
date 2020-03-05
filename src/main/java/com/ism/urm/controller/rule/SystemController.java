package com.ism.urm.controller.rule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ism.urm.service.rule.AppSystemService;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.RelationOp.OpType;
import com.ism.urm.vo.RelationOp.ValueType;
import com.ism.urm.vo.rule.system.AppSystem;

@RestController
public class SystemController {

    AppSystemService service = new AppSystemService();

    @GetMapping("/system")
    public List<AppSystem> search(@RequestParam Map<String, String> params) throws Exception {
        List<AppSystem> rtn = null;
        List<RelationOp> filter = new ArrayList<>();
        
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String val = params.get(key);

            if (val == null || val.length() == 0) {
                continue;
            }
            
            RelationOp op = null;
            if ("type".equals(key) || "devType".equals(key)) {
                op = RelationOp.get(key, OpType.EQ, val, ValueType.STRING);
            } else {
                op = RelationOp.get(key, OpType.LIKE, val, ValueType.STRING);
            }
            filter.add(op);
        }
        
        try {
            rtn = service.search(filter);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/system/{id}")
    public AppSystem get(@PathVariable String id) throws Exception {
        AppSystem rtn = null;
        try {
            rtn = service.get(id);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }
    

    @PostMapping("/system")
    public AppSystem save(@RequestBody AppSystem system) throws Exception {
        AppSystem rtn = null;
        try {
            rtn = service.save(system);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }
}
