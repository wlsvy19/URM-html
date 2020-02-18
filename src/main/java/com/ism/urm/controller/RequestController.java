package com.ism.urm.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ism.urm.service.rule.RequestService;
import com.ism.urm.vo.rule.Request;

@RestController
public class RequestController {

    RequestService service = new RequestService();

    @GetMapping("/request")
    public List<Request> search(@RequestParam Map<String, String> params) {
        List<Request> rtn = null;
        try {
            rtn = service.search(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    @GetMapping("/request/{id}")
    public Request get(@PathVariable String id) {
        Request rtn = null;
        try {
            rtn = service.get(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }
    

    @PostMapping("/request")
    public Request save(@RequestBody Request request) {
        Request rtn = null;
        try {
            rtn = service.save(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }
}
