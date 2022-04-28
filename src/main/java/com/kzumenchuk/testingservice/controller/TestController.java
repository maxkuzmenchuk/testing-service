package com.kzumenchuk.testingservice.controller;

import com.kzumenchuk.testingservice.repository.model.TestEntity;
import com.kzumenchuk.testingservice.service.TestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get-all")
    public List<TestEntity> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/get-by-id")
    public TestEntity getTestByID(@RequestParam("id") Long id) {
        return testService.getTestByID(id);
    }
}
