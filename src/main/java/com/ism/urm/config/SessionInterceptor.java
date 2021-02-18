package com.ism.urm.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ism.urm.util.SessionUtil;

public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        
        // TODO : login session check... temporary
//        String userid = (String)req.getSession().getAttribute(SessionUtil.USER_ID);
//        String ajaxCall = req.getHeader("AjaxCall");
//        if (ajaxCall != null && ajaxCall.equals("true")) {
//            if (userid == null) {
//                resp.sendError(403); //SC_FORBIDDEN
//                return false;
//            }
//        } else {
//            if (userid == null) {
//                resp.setHeader("Location", req.getContextPath() + "/login/login.page");
//                resp.sendRedirect(req.getContextPath() + "/login/login.page");
//                return false;
//            }
//        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView mv) 
            throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception e) 
            throws Exception {
    }

}
