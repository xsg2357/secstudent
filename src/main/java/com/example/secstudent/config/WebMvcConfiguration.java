package com.example.secstudent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/signOut").setViewName("signOut");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/getUserList").setViewName("userList");
        registry.addViewController("/getOrderList").setViewName("orderList");
    }
}

