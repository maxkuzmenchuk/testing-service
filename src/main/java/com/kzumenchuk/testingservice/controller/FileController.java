package com.kzumenchuk.testingservice.controller;

import com.kzumenchuk.testingservice.exception.GeneratingPDFException;
import com.kzumenchuk.testingservice.repository.model.FileDataEntity;
import com.kzumenchuk.testingservice.service.FileDataDataService;
import com.kzumenchuk.testingservice.util.enums.FileKind;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileDataDataService fileDataService;

//    private Map<String, Object> successResponseBody = new HashMap<>();
//
//    private Map<String, Object> errorResponseBody = new HashMap<>();

//    @GetMapping("/test/generate-pdf")
//    public ResponseEntity<Map<String, Object>> generatePDF(@RequestParam("id") Long id) {
//        try {
//            fileDataService.generateTestDataToPDF(id);
//            successResponseBody.clear();
//            successResponseBody = CustomResponse.createSuccessResponse("File generated successfully", "");
//
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(successResponseBody);
//        } catch (Exception exception) {
//            errorResponseBody.clear();
//            errorResponseBody = CustomResponse.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());
//
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(errorResponseBody);
//        }
//    }

    @GetMapping(value = "/test/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> downloadTestFile(@RequestParam("id") Long testID) {
        try {
            FileDataEntity file;
            try {
                file = fileDataService.getTestFile(testID, FileKind.TEST);
            } catch (FileNotFoundException e) {
                file = fileDataService.generateTestDataToPDF(testID); // create a file if it has not been created yet or has been deleted
            }

            Path sourcePath = Paths.get(file.getFileSourceUrl());
            Resource resource = fileDataService.returnFileAsResource(sourcePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(resource);
        } catch (GeneratingPDFException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/result/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> downloadResultFile(@RequestParam("id") Long resultID) {
        try {
            FileDataEntity file;
            try {
                file = fileDataService.getTestResultFile(resultID);
            } catch (FileNotFoundException e) {
                file = fileDataService.generateTestResultToPDF(resultID); // create a file if it has not been created yet or has been deleted
            }

            Path sourcePath = Paths.get(file.getFileSourceUrl());
            Resource resource = fileDataService.returnFileAsResource(sourcePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(resource);
        } catch (GeneratingPDFException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
