package com.ism.urm.controller.rule;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ism.urm.service.rule.AppSystemService;
import com.ism.urm.vo.rule.system.AppSystem;

@RestController
public class SystemController {

    AppSystemService service = new AppSystemService();

    @GetMapping("/system")
    public List<AppSystem> search(@RequestParam Map<String, String> params) {
        List<AppSystem> rtn = null;
        try {
            rtn = service.search(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    @GetMapping("/system/{id}")
    public AppSystem get(@PathVariable String id) {
        AppSystem rtn = null;
        try {
            rtn = service.get(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }
    

    @PostMapping("/system")
    public AppSystem save(@RequestBody AppSystem system) {
        AppSystem rtn = null;
        try {
            rtn = service.save(system);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }
}
