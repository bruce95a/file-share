package com.github.bruce95a.file.share.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor()).excludePathPatterns("/config", "/download", "/login", "/logout", "/info", "/*/*.*", "/*.ico");
        registry.addInterceptor(new AppInterceptor()).addPathPatterns("/", "/login", "/logout");
    }
}
