package com.ism.urm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ism.urm.service.manage.UserService;
import com.ism.urm.util.SessionUtil;
import com.ism.urm.vo.JobResult;
import com.ism.urm.vo.manage.User;

@Controller
public class LoginController {
    
    UserService service = new UserService();

    @GetMapping("/")
    public String page(HttpServletRequest req) {
        System.out.println("default path");
        return loginPage(req);
    }

    @GetMapping("/login/login.page")
    public String loginPage(HttpServletRequest req) {
        //req.setAttribute("version", WEB_VERSION);
        String loginPage = ""; //Common.getProperty("common.loginpage");
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
            if (userId == null || userId.length() == 0) {
                if (user.getPassword() == null || user.getPassword().length() == 0) {
                    throw new Exception("session expired!!!");
                }
                userId = user.getId();
                User org = service.get(userId);
                
                if (org == null) {
                    throw new Exception("user not exist."); //code 1
                } else {
                    String passwd = SessionUtil.getDecString(org.getPassword());
                    if (!passwd.equals(user.getPassword())) {
                        throw new Exception("invalid password."); //code 2
                    }
                }
                
                SessionUtil.setUserID(userId);
                User obj = new User();
                obj.setId(userId);
                obj.setName(org.getName());
                obj.setAuthId(org.getAuthId());
                
                rtn = new JobResult(0, "login success.");
                rtn.setObj(obj);
            } else {
                rtn = new JobResult(0, "already logged!!!");
            }
        } catch (Exception e) {
            rtn = new JobResult(99, e.getMessage());
        }
        return rtn;
    }

    @GetMapping("/logout")
    public void logout() {
        SessionUtil.invalidate();
    }

}
