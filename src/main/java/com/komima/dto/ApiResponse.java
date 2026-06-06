
package com.komima.dto;

import lombok.Data;

/**
 * API统一响应类
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@Data
public class ApiResponse<T> {

    private Integer code;
    
    private String message;
    
    private T data;
    
    public ApiResponse() {
    }
    
    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }
    
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null);
    }
}
