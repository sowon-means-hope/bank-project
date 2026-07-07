package com.example.bank.exception;

import com.example.bank.dto.ApiResponse;
import com.example.bank.exception.auth.DuplicateLoginIdException;
import com.example.bank.exception.member.MemberNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException e
    ){
        FieldError fieldError = e.getBindingResult().getFieldError();

        String message = fieldError != null
                ? fieldError.getDefaultMessage()
                : "잘못된 요청";

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(400, message));
    }

    @ExceptionHandler(DuplicateLoginIdException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicationLoginId(
            DuplicateLoginIdException e
    ){
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(
                        400,
                        e.getMessage()
                ));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public  ResponseEntity<ApiResponse<Void>> handleMemberNotFound(
            MemberNotFoundException e
    ){
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(
                        404,
                        e.getMessage()
                ));
    }
}
