package com.ffreaky.apigw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class ApiGwApplication {

    static Logger logger = LoggerFactory.getLogger(ApiGwApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiGwApplication.class, args);
    }

    @Scheduled(fixedRate = 20000)
    private void healthCheck() {
        logger.info("api-gw is running...");
    }

}
