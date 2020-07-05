package com.github.bruce95a.file.share.interceptor;

import com.github.bruce95a.file.share.util.CfgUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class InitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!CfgUtil.isUserInit()) {
            response.sendRedirect("/register");
            return false;
        }
        HttpSession session = request.getSession();
        if (session.getAttribute("User") == null) {
            String requestURI = request.getRequestURI();
            if ("/".equals(requestURI) || "/index".equals(requestURI)) {
                return true;
            }
            response.sendRedirect("/");
            return false;
        }
        if (!CfgUtil.isPathInit() || !CfgUtil.isSiteInit()) {
            response.sendRedirect("/config");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
