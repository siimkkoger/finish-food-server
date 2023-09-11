package com.example.finishfoodserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestController {

    public TestController() {
        System.out.println("MyBean instance created");
    }

    @GetMapping(value = "/test")
    String test() {
        return "Server is working 123!";
    }

}
