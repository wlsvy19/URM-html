package com.ism.urm.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ism.urm.util.SessionUtil;

public class SessionInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        /* TODO : login session check... temporary
        String userid = (String)req.getSession().getAttribute(SessionUtil.USER_ID);
        String ajaxCall = req.getHeader("AjaxCall");
        if(ajaxCall !=null && ajaxCall.equals("true")) {
            if(userid == null) {
                resp.sendError(403); //SC_FORBIDDEN
                return false;
            }
        } else {
            if(userid == null) {
                resp.sendRedirect(req.getContextPath()+"/login/login");
                return false;
            }
        }
        */

        /* TODO : 페이지 권한
        if(!isPermittedView(req)) {
            resp.sendRedirect(req.getContextPath()+"/");
            return false;
        }
        */
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView mv) 
            throws Exception {
        
        String uri = req.getRequestURI();
        if(uri != null && uri.endsWith(".page")) {
            //locale

            // Link..

        }
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object obj, Exception e) 
            throws Exception {
    }


    /*
    private boolean isPermittedView(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        
        if(uri.endsWith(".page") && !uri.endsWith("main.page") && !uri.endsWith("myinfo.page")) {
            if(uri.endsWith("properties.page"))  return true;
            
            int idx1 = uri.lastIndexOf("/") + 1;
            int idx2 = uri.lastIndexOf(".page");
            String comp = uri.substring(idx1, idx2);
            if(comp.equals("realtime") || comp.equals("batch") || comp.equals("deferred")) {
                if(uri.startsWith(contextPath+"/stts/hour/")) {
                    return isContainView(req, "hour"+comp);
                } else if (uri.startsWith(contextPath+"/stts/day/")) {
                    return isContainView(req, "day"+comp);
                } else {
                    return isContainView(req, "log"+comp);
                }
            } else if(uri.startsWith(contextPath+"/simulator")) {
                return isContainView(req, "simulator");
            } else if(uri.startsWith(contextPath+"/scheduler")) {
                return isContainView(req, "batchscheduler");
            } else {
                return isContainView(req, comp);
            }
        }
        return true;
    }

    private boolean isContainView(HttpServletRequest req, String viewName) {
        Map<String,Boolean> views = (Map<String,Boolean>)req.getSession().getAttribute("views");
        if(views != null) {
            Boolean isContain = views.get(viewName);
            if(isContain != null && isContain == true) {
                return true;
            }
        }
        return false;
    }
     */


}
