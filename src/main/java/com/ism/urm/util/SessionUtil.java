package com.ism.urm.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtil {
    public static final String USER_ID = "userid";

    private final static String ARIA_KEY = "12345678901234567890123456789012";

    public static String getEncString(String dec) {
        return ARIAEngine.getEncString(dec, ARIA_KEY);
    }

    public static String getDecString(String enc) {
        return ARIAEngine.getDecString(enc, ARIA_KEY);
    }

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
            return "eai"; //throw new Exception("empty session.")
        }
        return userID;
    }
    
    public static void invalidate() {
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = (attr != null ? attr.getRequest() : null);
        req.getSession().invalidate();
    }

}
