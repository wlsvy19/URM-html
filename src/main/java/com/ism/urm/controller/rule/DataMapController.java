package com.ism.urm.controller.rule;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ism.urm.service.rule.DataService;
import com.ism.urm.vo.rule.data.Data;

@RestController
public class DataMapController {

    DataService service = new DataService();

    @GetMapping("/datamap")
    public List<Data> search(@RequestParam Map<String, String> params) {
        List<Data> rtn = null;
        try {
            rtn = service.search(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    @GetMapping("/datamap/{id}")
    public Data get(@PathVariable String id) {
        Data rtn = null;
        try {
            rtn = service.get(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }
    

    @PostMapping("/datamap")
    public Data save(@RequestBody Data data) {
        Data rtn = null;
        try {
            rtn = service.save(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }
}
