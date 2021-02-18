package com.ism.urm.controller.manage;

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

import com.ism.urm.service.manage.UserService;
import com.ism.urm.vo.JobResult;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.RelationOp.OpType;
import com.ism.urm.vo.RelationOp.ValueType;
import com.ism.urm.vo.manage.User;

@RestController
@RequestMapping("/api")
public class UserController {

    UserService service = new UserService();

    @GetMapping("/user/check")  
    public int idCheck(@RequestParam("id") String userID) throws Exception {
        int rtn = 0;
        try{
            rtn = service.idCheck(userID);
        }catch(Exception e) {
            throw e;
        }
        return rtn;
    }

    @GetMapping("/user")
    public List<User> search(@RequestParam Map<String, String> params) throws Exception {
        List<User> rtn = null;
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

    @GetMapping("/user/{id}")
    public User get(@PathVariable String id) throws Exception {
        User rtn = null;
        try {
            rtn = service.get(id);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @PostMapping("/user")  
    public User save(@RequestBody User user) throws Exception {
        User rtn = null;
        try {
            rtn = service.save(user);
        } catch (Exception e) {
            throw e;
        }
        return rtn;
    }

    @PostMapping("/user/delete")
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