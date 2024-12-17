package com.applejack.steamspringbootredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//启动Spring事务注解
@EnableTransactionManagement
public class SteamSpringbootRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SteamSpringbootRedisApplication.class, args);
    }

}
