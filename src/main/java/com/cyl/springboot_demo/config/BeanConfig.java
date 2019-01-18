package com.cyl.springboot_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class BeanConfig {

    @Bean
    public InetAddress inetAddress() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }
}
