package com.ffreaky.paymentservice;

import com.ffreaky.utilities.exceptions.FinishFoodExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class PaymentServiceExceptionHandler extends FinishFoodExceptionHandler {

    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    public String getServiceName() {
        return serviceName;
    }
}
