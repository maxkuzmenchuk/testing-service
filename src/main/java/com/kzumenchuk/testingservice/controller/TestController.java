package com.kzumenchuk.testingservice.controller;

import com.kzumenchuk.testingservice.repository.model.TestEntity;
import com.kzumenchuk.testingservice.service.TestService;
import com.kzumenchuk.testingservice.util.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tests")
public class TestController {
    private final TestService testService;
    private Map<String, Object> successResponseBody = new HashMap<>();
    private Map<String, Object> errorResponseBody = new HashMap<>();

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/add-new")
    public ResponseEntity<Map<String, Object>> addTest(@RequestBody TestEntity test) {
        try {
            TestEntity newTest = testService.addNewTest(test);
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Test created successfully", newTest);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(successResponseBody);
        } catch (Exception exception) {
            errorResponseBody.clear();
            errorResponseBody = CustomResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponseBody);
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<Map<String, Object>> editTest(@RequestBody TestEntity test) {
        try {
            TestEntity editedTest = testService.editTest(test);
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Test updated successfully", editedTest);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(successResponseBody);
        } catch (Exception exception) {
            errorResponseBody.clear();
            errorResponseBody = CustomResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponseBody);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<Map<String, Object>> getAllTests() {
        try {
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Success", testService.getAllTests());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(successResponseBody);
        } catch (Exception exception) {
            errorResponseBody.clear();
            errorResponseBody = CustomResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponseBody);
        }
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<Map<String, Object>> getTestByID(@RequestParam("id") Long id) {
        try {
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Success", testService.getTestByID(id));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(successResponseBody);
        } catch (Exception exception) {
            errorResponseBody.clear();
            errorResponseBody = CustomResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponseBody);
        }
    }

    @GetMapping("/get-by-title")
    public ResponseEntity<Map<String, Object>> getByTitle(@RequestParam("title") String title) {
        try {
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Success", testService.getTestsByTitle(title));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(successResponseBody);
        } catch (Exception exception) {
            errorResponseBody.clear();
            errorResponseBody = CustomResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponseBody);
        }
    }

    @GetMapping("/get-by-category")
    public ResponseEntity<Map<String, Object>> getByCategory(@RequestParam("category") String category) {
        try {
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Success", testService.getTestsByCategory(category));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(successResponseBody);
        } catch (Exception exception) {
            errorResponseBody.clear();
            errorResponseBody = CustomResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponseBody);
        }
    }

    @GetMapping("/get-by-date")
    public ResponseEntity<Map<String, Object>> getByDate(@RequestParam("date") String dateSearch) {
        try {
            LocalDate date = LocalDate.parse(dateSearch);

            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Success", testService.getTestsByCreateDate(date));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(successResponseBody);
        } catch (Exception exception) {
            errorResponseBody.clear();
            errorResponseBody = CustomResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponseBody);
        }
    }
}
