package com.mms.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;

@Configuration
@EnableAsync
public class EmailServiceConfig extends AsyncSupportConfigurer {

}
