package com.ism.urm.controller.rule;

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
import com.ism.urm.vo.JobResult;
import com.ism.urm.vo.rule.mapping.DataMap;

@RestController
@RequestMapping("/api")
public class DataMapController {

    DataMapService service = new DataMapService();

    @GetMapping("/datamap")
    public List<DataMap> search(@RequestParam Map<String, String> params) throws Exception {
        List<DataMap> rtn = null;
        try {
            rtn = service.search();
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
    public JobResult delete(@RequestBody List<String> ids) {
        JobResult rtn = null;
        try {
            rtn = service.delete(ids);
        } catch (Exception e) {
            rtn = new JobResult(99, e.getMessage());
        }
        return rtn;
    }
}
