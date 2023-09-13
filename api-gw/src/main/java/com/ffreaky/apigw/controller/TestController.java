package com.ffreaky.apigw.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class TestController {

    @PostMapping(value = "/test")
    boolean test() {
        return true;
    }

}
