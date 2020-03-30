package com.ism.urm.controller;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("/logout")
    public String logout() {
        SessionUtil.invalidate();
        return "redirect:/login/login.page";
    }
    

    /* default page */
    @GetMapping(value = {"/request", "/data", "/system", "/user", "/biz"})
    public String redirect() {
        return "forward:/index.html";
    }

}
