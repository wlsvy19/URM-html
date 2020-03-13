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

import com.ism.urm.service.rule.DataService;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.RelationOp.OpType;
import com.ism.urm.vo.RelationOp.ValueType;
import com.ism.urm.vo.rule.data.Data;

@RestController
public class DataController {

    DataService service = new DataService();

    @GetMapping("/data")
    public List<Data> search(@RequestParam Map<String, String> params) throws Exception {
    	System.out.println("************************DataController : search()************************");
		
        List<Data> rtn = null;
        List<RelationOp> filter = new ArrayList<>();
        
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String val = params.get(key);

            if (val == null || val.length() == 0) {
                continue;
            }
            
            RelationOp op = null;
            if ("type".equals(key)) {
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

    @GetMapping("/data/{id}")
    public Data get(@PathVariable String id) throws Exception {
    	System.out.println("DataController get()");
        Data rtn = null;
        try {
            rtn = service.get(id);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }
    
    @PostMapping("/data")
    public Data save(@RequestBody Data data) throws Exception {
        Data rtn = null;
        try {
            rtn = service.save(data);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }
}
