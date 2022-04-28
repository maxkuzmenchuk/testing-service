package com.kzumenchuk.testingservice.controller;

import com.kzumenchuk.testingservice.repository.model.TestEntity;
import com.kzumenchuk.testingservice.service.TestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/add-new")
    public TestEntity addTest(@RequestBody TestEntity test) {
        return testService.addNewTest(test);
    }
}
