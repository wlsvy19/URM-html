package com.ism.urm.controller.rule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ism.urm.service.rule.DataMapService;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.RelationOp.OpType;
import com.ism.urm.vo.RelationOp.ValueType;
import com.ism.urm.vo.rule.mapping.DataMap;

@RestController
@RequestMapping("/api")
public class DataMapController {

    DataMapService service = new DataMapService();

    @GetMapping("/datamap")
    public List<DataMap> search(@RequestParam Map<String, String> params) throws Exception {
        List<DataMap> rtn = null;
        List<RelationOp> filter = new ArrayList<>();
        
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String val = params.get(key);

            if (val == null || val.length() == 0) {
                continue;
            }
            
            RelationOp op = null;
            if ("id".equals(key) || "name".equals(key)) {
                op = RelationOp.get(key, OpType.LIKE, val, ValueType.STRING);
            } else {
                op = RelationOp.get(key, OpType.EQ, val, ValueType.STRING);
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

    @GetMapping("/datamap/{id}")
    public DataMap get(@PathVariable String id) throws Exception {
        DataMap rtn = null;
        try {
            rtn = service.get(id);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @PostMapping("/datamap")
    public DataMap save(@RequestBody DataMap data) throws Exception {
        DataMap rtn = null;
        try {
            rtn = service.save(data);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @PostMapping("/datamap/delete")
    public int delete(@RequestBody List<String> ids) throws Exception {
        int rtn = 0;
        try {
            rtn = service.delete(ids);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }
}
