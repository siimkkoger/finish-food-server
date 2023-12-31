package com.ffreaky.shoppingservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class ShoppingServiceApplication {

    static Logger logger = LoggerFactory.getLogger(ShoppingServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ShoppingServiceApplication.class, args);
    }

    @Scheduled(fixedRate = 20000)
    private void healthCheck() {
        logger.info("food-service is running...");
    }

}
