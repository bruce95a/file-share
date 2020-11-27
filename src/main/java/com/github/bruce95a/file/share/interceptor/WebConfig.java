package com.github.bruce95a.file.share.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor()).excludePathPatterns("/reports/*", "/login", "/logout", "/info.html", "/*/*.*", "/*.ico");
        registry.addInterceptor(new AppInterceptor()).addPathPatterns("/", "/index.html", "/login");
    }
}
