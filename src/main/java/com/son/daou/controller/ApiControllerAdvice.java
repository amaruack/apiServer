package com.son.daou.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.son.daou.common.ErrorCode;
import com.son.daou.common.exception.ApiException;
import com.son.daou.dto.ErrorBody;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiControllerAdvice {

    private Gson gson = new GsonBuilder().create();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBody> handleValidationExceptions(MethodArgumentNotValidException ex){
        List<ErrorBody.CustomError> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(c ->
                {
                    if (c.getClass().toString().contains("ViolationObjectError")) {
                        errors.add(ErrorBody.CustomError.builder()
                                .code(ErrorCode.BAD_REQUEST.getErrorCode())
                                .message(ErrorCode.BAD_REQUEST.getErrorMessage())
                                .detailMessage( Arrays.stream(c.getArguments()).filter(o -> o.getClass().toString().contains("ResolvableAttribute")) .map(Object::toString).collect(Collectors.joining(",")) + " - " + c.getDefaultMessage())
                                .build());
                    } else {
                        errors.add(ErrorBody.CustomError.builder()
                                .code(ErrorCode.BAD_REQUEST.getErrorCode())
                                .message(ErrorCode.BAD_REQUEST.getErrorMessage())
                                .detailMessage(((FieldError) c).getField() + " - " + c.getDefaultMessage())
                                .build());
                    }
                }
//                Error in object 'rulesetMasterCreateRequest': codes [TimeConditionValidation.rulesetMasterCreateRequest,TimeConditionValidation]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [rulesetMasterCreateRequest.,]; arguments []; default message [],beginTime,closeTime,timeConditionType]; default message [시간 조건 타입에 맞는 시작/종료 날짜시간을 입력해 주세요]
        );
        ErrorBody errorBody = new ErrorBody();
        errorBody.setErrors(errors);
        return ResponseEntity.badRequest().body(errorBody);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorBody> handleValidationExceptions(MethodArgumentTypeMismatchException ex){
        ErrorBody errorBody = new ErrorBody();
        errorBody.setErrors(
                List.of(
                        ErrorBody.CustomError.builder()
                                .code(ErrorCode.BAD_REQUEST.getErrorCode())
                                .message(ErrorCode.BAD_REQUEST.getErrorMessage())
                                .detailMessage(ex.getMessage())
                                .build())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorBody);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorBody> handleValidationExceptions(HttpMessageNotReadableException ex){
        ErrorBody errorBody = new ErrorBody();
        errorBody.setErrors(
                List.of(
                        ErrorBody.CustomError.builder()
                                .code(ErrorCode.BAD_REQUEST.getErrorCode())
                                .message(ErrorCode.BAD_REQUEST.getErrorMessage())
                                .detailMessage(ex.getMessage())
                                .build())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorBody);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorBody> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
        ErrorBody errorBody = new ErrorBody();
        errorBody.setErrors(
                List.of(
                    ErrorBody.CustomError.builder()
                        .code(ErrorCode.METHOD_NOT_ALLOWED.getErrorCode())
                        .message(ErrorCode.METHOD_NOT_ALLOWED.getErrorMessage())
                        .detailMessage(ex.getMessage())
                        .build())
                );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED.value()).body(errorBody);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorBody> propertyReferenceExceptionException(RuntimeException ex){
        ErrorCode errorCode = null;
        if (ex instanceof ApiException) {
            errorCode = ((ApiException)ex).getErrorCode();
        } else if (ex instanceof PropertyReferenceException) {
            errorCode = ErrorCode.NOT_VALID_PROPERTY;
        } else if (ex instanceof IllegalArgumentException) {
            errorCode = ErrorCode.BAD_REQUEST;
        } else if (ex instanceof ConversionFailedException) {
            errorCode = ErrorCode.BAD_REQUEST;
        } else if (ex instanceof NullPointerException) {
            errorCode = ErrorCode.BAD_REQUEST;
        } else {
            errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        }

        ErrorBody errorBody = new ErrorBody();
        errorBody.setErrors(
                List.of(ErrorBody.CustomError.builder()
                    .code(errorCode.getErrorCode())
                    .message(errorCode.getErrorMessage())
                    .detailMessage(ex.getMessage())
                    .build())
        );

        return ResponseEntity.status(errorCode.getHttpStatus().value()).body(errorBody);
    }

}