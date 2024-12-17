package com.applejack.steamspringbootredis.Cofig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 开启跨域，能够接受其他端口传进来的参数
 * @author Akemi0Homura
 */
//@Configuration
public class CORS implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // /** 表示此CORS配置将应用于所有路径。
        registry.addMapping("/**")
                // 允许的来源
                .allowedOrigins("http://localhost:3000/")
                // 允许的方法
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // 允许的头部
                .allowedHeaders("*") // 允许的头部
                // 是否允许凭据
                .allowCredentials(true);
    }
}