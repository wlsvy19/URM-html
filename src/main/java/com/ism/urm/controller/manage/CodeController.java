package com.ism.urm.controller.manage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ism.urm.service.manage.CodeService;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.RelationOp.OpType;
import com.ism.urm.vo.RelationOp.ValueType;
import com.ism.urm.vo.manage.Auth;
import com.ism.urm.vo.manage.BusinessCode;
import com.ism.urm.vo.manage.CommonCode;


@RestController
public class CodeController {

    CodeService service = new CodeService();
    
    @GetMapping("/code/common")
    public List<CommonCode> getCommonCode() {
        List<CommonCode> rtn = null;
        try {
            rtn = service.getCommonCodeList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    @GetMapping("/code/user")
    public List<Auth> getAuth() {
        List<Auth> rtn = null;
        try {
            rtn = service.getAuthList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    @GetMapping("/code/business")
    public List<BusinessCode> getBusinessCode(@RequestParam Map<String, String> params) {
        List<BusinessCode> rtn = null;
        List<RelationOp> filter = new ArrayList<>();
        
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String val = params.get(key);
            
            if (val == null || val.length() == 0) {
                continue;
            }
            
            RelationOp op = RelationOp.get(key, OpType.LIKE, val, ValueType.STRING);
            filter.add(op);
        }
        
        try {
            rtn = service.getBusinessCodeList(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    @PostMapping("/code/business")
    public List<BusinessCode> save(@RequestBody List<BusinessCode> codes) throws Exception {
        List<BusinessCode> rtn = null;
        try {
            rtn = service.modifyBusinessCode(codes);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }
}

