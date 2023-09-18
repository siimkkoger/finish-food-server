package com.ffreaky.foodservice.controller;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/food")
public class TestController {

    @GetMapping(value = "/test")
    boolean test() {
        return true;
    }

    @PostMapping(value = "/test")
    boolean testPostDto(@RequestBody TestDto testDto) {
        return testDto.id() == 0 && testDto.name().equals("testname");
    }

    private record TestDto(int id, String name) {
    }
}
