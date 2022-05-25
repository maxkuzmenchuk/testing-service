package com.kzumenchuk.testingservice.controller;

import com.kzumenchuk.testingservice.repository.model.TestResultEntity;
import com.kzumenchuk.testingservice.repository.model.dto.TestResultDTO;
import com.kzumenchuk.testingservice.service.TestResultService;
import com.kzumenchuk.testingservice.util.CustomResponse;
import com.kzumenchuk.testingservice.util.requests.DeleteRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/results")
public class TestResultController {
    private final TestResultService testResultService;

    private Map<String, Object> successResponseBody = new HashMap<>();

    private Map<String, Object> errorResponseBody = new HashMap<>();

    public TestResultController(TestResultService testResultService) {
        this.testResultService = testResultService;
    }

    @PostMapping("/save-result")
    public TestResultEntity saveResult(@RequestBody TestResultDTO testResultDTO) {
        return testResultService.saveTestResults(testResultDTO);
    }

    @GetMapping("/get-test-results")
    public ResponseEntity<Map<String, Object>> getTestResults(@RequestParam("id") Long testID) {
        try {
            successResponseBody.clear();
            List<TestResultDTO> resultsList = testResultService.getResultsByTestID(testID);

            if (resultsList.size() > 0) {
                successResponseBody = CustomResponse.createSuccessResponse("Success", resultsList);
            } else {
                successResponseBody = CustomResponse.createSuccessResponse("Success", "There are no results for this test");
            }

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
    public ResponseEntity<Map<String, Object>> deleteTest(@RequestBody DeleteRequest deleteRequest) {
        try {
            testResultService.deleteTestResult(deleteRequest);
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Results deleted successfully", "");

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
}
