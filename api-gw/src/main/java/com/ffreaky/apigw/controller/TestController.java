package com.ffreaky.apigw.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api-gw")
public class TestController {

    @GetMapping(value = "/test")
    boolean test() {
        return true;
    }
}
