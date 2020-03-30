package com.ism.urm.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ism.urm.util.SessionUtil;

public class LoginPageInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        String userid = (String) req.getSession().getAttribute(SessionUtil.USER_ID);
        if (userid != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return false;
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView modelAndView) throws Exception {
        // do nothing
    }

}
