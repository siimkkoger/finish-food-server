package com.ffreaky.foodservice.controller;

import com.ffreaky.foodservice.entity.Test;
import com.ffreaky.foodservice.model.FoodDto;
import com.ffreaky.foodservice.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/food")
public class TestController {

    private final TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping(value = "/test")
    boolean test() {
        return true;
    }
    @GetMapping(value = "/testdb")
    List<Test> getTest() {
        System.out.println("getTest");
        System.out.println("getTest");
        System.out.println("getTest");
        System.out.println("getTest");
        System.out.println("getTest");
        return StreamSupport
                .stream(testRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
