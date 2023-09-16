package com.ffreaky.apigw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@SpringBootApplication
public class ApiGwApplication {

    static Logger logger = LoggerFactory.getLogger(ApiGwApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiGwApplication.class, args);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    logger.info("api-gw-info");
                    logger.debug("api-gw-debug");
                    logger.error("api-gw-error");

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @GetMapping(value = "/api-gw/test")
    boolean test() {
        return true;
    }

}
