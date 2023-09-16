package com.ffreaky.foodservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class FoodServiceApplication {

    static Logger logger = LoggerFactory.getLogger(FoodServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FoodServiceApplication.class, args);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    logger.info("food-service-info");
                    logger.debug("food-service-debug");
                    logger.error("food-service-error");

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
