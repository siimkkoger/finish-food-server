package com.ffreaky.foodservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class FoodServiceApplication {

    static Logger logger = LoggerFactory.getLogger(FoodServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FoodServiceApplication.class, args);
    }

    @Scheduled(fixedRate = 10000)
    private void healthCheck() {
        logger.info("food-service is running...");
    }

}
