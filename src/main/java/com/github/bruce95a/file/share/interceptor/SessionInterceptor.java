package com.github.bruce95a.file.share.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String requestURI = request.getRequestURI();
        if (session.getAttribute("User") != null) {
            if ("/".equals(requestURI) || "/index.html".equals(requestURI)) {
                response.sendRedirect("/report.html");
                return false;
            }
            return true;
        }
        if ("/".equals(requestURI) || "/index.html".equals(requestURI) || "/login".equals(requestURI)) {
            return true;
        }
        if (requestURI.contains(".html")) {
            response.sendRedirect("/");
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
