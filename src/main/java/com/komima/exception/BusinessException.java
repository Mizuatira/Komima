package com.komima.exception;

/**
 * 业务异常类
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public int getCode() {
        return code;
    }
}
