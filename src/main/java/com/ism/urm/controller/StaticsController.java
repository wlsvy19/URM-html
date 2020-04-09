package com.ism.urm.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ism.urm.service.StaticsService;
import com.ism.urm.vo.statics.RequestChangeCount;
import com.ism.urm.vo.statics.RequestProcessCount;

@RestController
@RequestMapping("/api")
public class StaticsController {

    StaticsService service = new StaticsService();

    @GetMapping("/stat/process/{type}")
    public List<RequestProcessCount> getRequestProcessCount(
            @PathVariable String type,
            @RequestParam Map<String, String> params) throws Exception {
        List<RequestProcessCount> rtn = null;
        try {
            int typeNo = 0;
            if (type.equals("day")) {
                typeNo = 1;
            } else if (type.equals("month")) {
                typeNo = 2;
            }
            rtn = service.getRequestProcessCount(typeNo, params);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/stat/change/{type}")
    public List<RequestChangeCount> getRequestChangeCount(
            @PathVariable String type,
            @RequestParam Map<String, String> params) throws Exception {
        List<RequestChangeCount> rtn = null;
        try {
            int typeNo = 0;
            if (type.equals("day")) {
                typeNo = 1;
            } else if (type.equals("month")) {
                typeNo = 2;
            }
            rtn = service.getRequestChangeCount(typeNo, params);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

}
