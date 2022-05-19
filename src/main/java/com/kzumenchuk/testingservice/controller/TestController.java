package com.kzumenchuk.testingservice.controller;

import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;
import com.kzumenchuk.testingservice.service.TestService;
import com.kzumenchuk.testingservice.util.CustomResponse;
import com.kzumenchuk.testingservice.util.requests.DeleteTestRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tests")
public class TestController {
    private final TestService testService;
    private Map<String, Object> successResponseBody = new HashMap<>();
    private Map<String, Object> errorResponseBody = new HashMap<>();

    @PostMapping("/add-new")
    public ResponseEntity<Map<String, Object>> addTest(@RequestBody TestDTO testDTO) {
        try {

            TestDTO newTest = testService.addNewTest(testDTO);
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
    public ResponseEntity<Map<String, Object>> editTest(@RequestBody TestDTO testDTO) {
        try {
            successResponseBody.clear();
            successResponseBody = testService.editTest(testDTO);

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

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteTest(@RequestBody DeleteTestRequest deleteTestRequest) {
        try {
            testService.deleteTestById(deleteTestRequest.getDeleteIDs());
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Tests deleted successfully", "");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(successResponseBody);
        } catch (DataAccessException e) {
            errorResponseBody.clear();
            errorResponseBody = CustomResponse.createErrorResponse(HttpStatus.FORBIDDEN.getReasonPhrase(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(errorResponseBody);
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
            successResponseBody = CustomResponse.createSuccessResponse("Success", testService.getTests("all", ""));

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
            successResponseBody = CustomResponse.createSuccessResponse("Success", testService.getTests("title", title));

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
            successResponseBody = CustomResponse.createSuccessResponse("Success", testService.getTests("category", category));

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
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Success", testService.getTests("createDate", dateSearch));

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
