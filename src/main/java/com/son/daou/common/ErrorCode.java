package com.son.daou.common;


import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 200
//    ACCEPTED_WAIT(HttpStatus.ACCEPTED, "ERROR-2001", "Already Accepted, Please Wait"),

    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "ERROR-4001", "Bad Request Error", "%s"),
    NOT_VALID_PROPERTY(HttpStatus.BAD_REQUEST, "ERROR-4002", "Not Valid property", "%s"),
    UN_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "ERROR-4011", "unuthentication error", "%s"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ERROR-4031", "Access Denied", "%s"),
    NOT_FOUND_DATA(HttpStatus.NOT_FOUND, "ERROR-4041", "Not Found Data", "not found data using [%s]"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "ERROR-4051", "Method Not Allowed", "%s"),
    CONFLICT(HttpStatus.CONFLICT, "ERROR-4091", "Already Registration Data", "already registration data by id [%s]"),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR-5001", "INTERNAL_SERVER_ERROR", "%s");

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;
    private String detailMessageFormat;

    ErrorCode(HttpStatus httpStatus, String errorCode, String errorMessage, String detailMessageFormat) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.detailMessageFormat = detailMessageFormat;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getDetailMessageFormat() {
        return detailMessageFormat;
    }
}