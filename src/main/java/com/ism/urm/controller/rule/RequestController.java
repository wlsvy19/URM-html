package com.ism.urm.controller.rule;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ism.urm.service.rule.RequestService;
import com.ism.urm.vo.PagingResult;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.RelationOp.OpType;
import com.ism.urm.vo.RelationOp.ValueType;
import com.ism.urm.vo.rule.request.Request;
import com.ism.urm.vo.rule.request.RequestHistory;

@RestController
public class RequestController {

    RequestService service = new RequestService();

    @GetMapping("/request")
    public PagingResult<Request> search(@RequestParam(value="page") int page,
            @RequestParam(value="size") int size,
            @RequestParam Map<String, String> params) throws Exception {
        PagingResult<Request> rtn = null;
        List<RelationOp> filter = new ArrayList<>();
        
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String val = params.get(key);
            if ("page".equals(key) || "size".equals(key)) {
                continue;
            }
            if (val == null || val.length() == 0) {
                continue;
            }
            
            RelationOp op = null;
            if ("interfaceId".equals(key) || "id".equals(key) || "name".equals(key)) {
                op = RelationOp.get(key, OpType.LIKE, val, ValueType.STRING);
            } else if ("chgDate".equals(key)) {
                String[] arr = val.split(",");
                op = RelationOp.get(key, OpType.GE, new Date(Long.parseLong(arr[0])), ValueType.DATE);
                op = RelationOp.get(key, OpType.LE, new Date(Long.parseLong(arr[1])), ValueType.DATE);
            } else {
                op = RelationOp.get(key, OpType.EQ, val, ValueType.STRING);
            }
            filter.add(op);
        }
        
        try {
            rtn = service.search(page, size, filter, true);
        } catch (Exception e) {
            throw new Exception("Failed to Search", e);
        }
        return rtn;
    }

    @GetMapping("/request/{id}")
    public Request get(@PathVariable String id) throws Exception {
        Request rtn = null;
        try {
            rtn = service.get(id);
        } catch (Exception e) {
            throw new Exception("Failed to Get", e);
        }
        return rtn;
    }

    @GetMapping("/request/{id}/history")
    public List<RequestHistory> getHistory(@PathVariable String id) throws Exception {
        List<RequestHistory> rtn = null;
        try {
            rtn = service.getHistroy(id);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @PostMapping("/request")
    public Request save(@RequestBody Request request) throws Exception {
        Request rtn = null;
        try {
            rtn = service.save(request);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }
}
