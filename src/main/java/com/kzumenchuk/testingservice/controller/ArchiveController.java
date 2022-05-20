package com.kzumenchuk.testingservice.controller;

import com.kzumenchuk.testingservice.service.ArchiveService;
import com.kzumenchuk.testingservice.util.CustomResponse;
import com.kzumenchuk.testingservice.util.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/archive")
@RequiredArgsConstructor
public class ArchiveController {

    private final ArchiveService archiveService;
    private Map<String, Object> successResponseBody = new HashMap<>();
    private Map<String, Object> errorResponseBody = new HashMap<>();

    @PostMapping("/move-to-archive")
    public ResponseEntity<Map<String, Object>> moveToArchive(@RequestParam("id") Long id) {
        try {
            archiveService.moveToArchive(id);
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Test moved to archive successfully", "");

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

    @PostMapping("/restore-from-archive")
    public ResponseEntity<Map<String, Object>> restoreFromArchive(@RequestParam("id") Long id) {
        try {
            archiveService.restoreFromArchive(id);
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Test restored from archive successfully", "");

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
    public ResponseEntity<Map<String, Object>> deleteFromArchive(@RequestParam("id") Long id) {
        try {
            archiveService.deleteFromArchive(id, OperationType.DELETE);
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Test deleted from archive successfully", "");

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
    public ResponseEntity<Map<String, Object>> getArchiveByID(@RequestParam("id") Long id) {
        try {
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Success", archiveService.getArchiveById(id));

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
    public ResponseEntity<Map<String, Object>> getAllArchive() {
        try {
            successResponseBody.clear();
            successResponseBody = CustomResponse.createSuccessResponse("Success", archiveService.getAllArchive());

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
