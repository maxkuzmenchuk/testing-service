package com.kzumenchuk.testingservice.util;

import java.util.HashMap;
import java.util.Map;

public class CustomResponse {
    public static Map<String, Object> createSuccessResponse(String message, Object entity) {
        Map<String, Object> body = new HashMap<>();

        body.put("message", message);
        body.put("result", entity);

        return body;
    }

    public static Map<String, Object> createErrorResponse(String message, Object bodyValue) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("error_message", bodyValue);

        return body;
    }
}
