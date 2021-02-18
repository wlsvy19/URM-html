package com.ism.urm.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ism.urm.config.URMProperties;
import com.ism.urm.service.manage.UserService;
import com.ism.urm.util.SessionUtil;
import com.ism.urm.vo.JobResult;
import com.ism.urm.vo.manage.User;

@Controller
public class LoginController {
    
    UserService service = new UserService();

    @GetMapping("/login/login.page")
    public String loginPage(HttpServletRequest req) {
        String loginPage = URMProperties.get("login.page");
        if(loginPage == null || loginPage.trim().length() == 0) {
            return "/login";
        } else {
            return loginPage;
        }
    }

    @PostMapping("/login/process")
    @ResponseBody
    public JobResult login(@RequestBody User user, HttpServletRequest req) throws Exception {
        JobResult rtn = null;
        try {
            String userId = (String) req.getSession().getAttribute(SessionUtil.USER_ID);
            rtn = service.login(user, userId);
        } catch (Exception e) {
            rtn = new JobResult(99, e.getMessage());
        }
        return rtn;
    }

    @PostMapping("/check/login")
    @ResponseBody
    public JobResult checkLogin(HttpServletRequest req) throws Exception {
        JobResult rtn = null;
        HashMap<String, String> map = new HashMap<>();
        try {
            String userId = (String) req.getSession().getAttribute(SessionUtil.USER_ID);
            User vo = service.get(userId);
            
            if (vo == null) {
                rtn = new JobResult(99, "Session User - null!!!");
                rtn.setObj(req.getContextPath() + "/login/login.page");
                SessionUtil.invalidate();
                return rtn;
            } else {
                map.put("id", vo.getId());
                map.put("name", vo.getName());
                map.put("authId", vo.getAuthId());
                rtn = new JobResult(0, "login success");
                rtn.setObj(map);
            }
        } catch (Exception e) {
            rtn = new JobResult(99, e.getMessage());
            rtn.setObj(req.getContextPath() + "/login/login.page");
        }
        return rtn;
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest req, HttpServletResponse resp) {
        SessionUtil.invalidate();
        resp.setHeader("Location", req.getContextPath() + "/login/login.page");
    }
    

    /* default page */
    @GetMapping(value = {"/request", "/data", "/system", "/user", "/manage/*", "/stat/*", "/process/*"})
    public String redirect() {
        return "forward:/index.html";
    }

}
