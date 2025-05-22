package com.phone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PhoneManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhoneManagementApplication.class, args);
    }
} 