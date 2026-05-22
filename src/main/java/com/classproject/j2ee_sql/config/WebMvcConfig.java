package com.classproject.j2ee_sql.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                // 拦截所有 /api/** 请求
                .addPathPatterns("/api/**")
                // 放行登录、注册接口
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/api/register")
                // 放行 Knife4j / Swagger 相关路径
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/v3/api-docs/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/swagger-resources/**");
    }
}
