package com.ism.urm.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtil {
    private static final String USER_ID = "userid";

    public static void setUserID(String userID) {
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = (attr != null ? attr.getRequest() : null);
        req.getSession().setAttribute(USER_ID, userID); 
    }

    public static String getUserID() throws Exception {
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = (attr != null ? attr.getRequest() : null);
        String userID = (String) req.getSession().getAttribute(USER_ID);
        
        if (userID == null) {
            return "eai";//"unknown-user";
        }
        return userID;
    }

}
