package com.example.bank.dto;

public record ApiResponse<T> (
        int status,
        String message,
        T data
){
    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<T>(200, message, data);
    }

    public static ApiResponse<Void> success(String message){
        return new ApiResponse<>(200, message, null);
    }

    public static ApiResponse<Void> fail(int status, String message){
        return new ApiResponse<>(status, message, null);
    }
}
