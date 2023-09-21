package com.ffreaky.paymentservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class PaymentServiceApplication {

    static Logger logger = LoggerFactory.getLogger(PaymentServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

    @Scheduled(fixedRate = 20000)
    private void healthCheck() {
        logger.info("payment-service is running...");
    }

}
