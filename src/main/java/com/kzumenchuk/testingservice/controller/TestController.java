package com.kzumenchuk.testingservice.controller;

import com.kzumenchuk.testingservice.repository.model.TestEntity;
import com.kzumenchuk.testingservice.service.TestService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @PostMapping("/edit")
    public TestEntity editTest(@RequestBody TestEntity test) {
        return testService.editTest(test);
    }

    @GetMapping("/get-all")
    public List<TestEntity> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/get-by-id")
    public TestEntity getTestByID(@RequestParam("id") Long id) {
        return testService.getTestByID(id);
    }

    @GetMapping("/get-by-title")
    public List<TestEntity> getByTitle(@RequestParam("title") String title) {
        return testService.getTestsByTitle(title);
    }

    @GetMapping("/get-by-category")
    public List<TestEntity> getByCategory(@RequestParam("category") String category) {
        return testService.getTestsByCategory(category);
    }

    @GetMapping("/get-by-date")
    public List<TestEntity> getByDate(@RequestParam("date") String dateSearch) {
        LocalDate date = LocalDate.parse(dateSearch);
        return testService.getTestsByCreateDate(date);
    }

}
