package com.medical.frontend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements  WebMvcConfigurer {
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/Admin").setViewName("/Admin/AdminDashboard.html");
        registry.addViewController("/AddDoctor").setViewName("/Admin/AddDoctor.html");
    }
}
