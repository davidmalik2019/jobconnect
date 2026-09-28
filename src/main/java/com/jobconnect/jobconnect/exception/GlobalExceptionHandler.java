package com.jobconnect.jobconnect.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================================================
    // 400 - BAD REQUEST
    // ==================================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }


    // ==================================================
    // 400 - VALIDATION ERRORS
    // ==================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult()
          .getFieldErrors()
          .forEach(error ->
              errors.put(
                  error.getField(),
                  error.getDefaultMessage()
              )
          );

        Map<String, Object> response = new HashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", "Validation failed");
        response.put("errors", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    // ==================================================
    // 404 - RESOURCE NOT FOUND
    // ==================================================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }
 // ==================================================
 // 405 - METHOD NOT ALLOWED
 // ==================================================

 @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
 public ResponseEntity<Map<String, Object>> handleMethodNotSupportedException(
         HttpRequestMethodNotSupportedException ex) {

     return buildResponse(
             HttpStatus.METHOD_NOT_ALLOWED,
             "HTTP method not allowed for this endpoint."
     );
 }

//==================================================
//415 - UNSUPPORTED MEDIA TYPE
//==================================================

@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
public ResponseEntity<Map<String, Object>> handleMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException ex) {

  return buildResponse(
          HttpStatus.UNSUPPORTED_MEDIA_TYPE,
          "Content-Type is not supported for this endpoint."
  );
}
    // ==================================================
    // 500 - INTERNAL SERVER ERROR
    // ==================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception ex) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred."
        );
    }


    // ==================================================
    // STANDARD ERROR RESPONSE
    // ==================================================

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> response = new HashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);

        return ResponseEntity
                .status(status)
                .body(response);
    }
}