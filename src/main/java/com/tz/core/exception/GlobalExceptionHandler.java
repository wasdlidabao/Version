package com.tz.core.exception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

// 全局异常类
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorCode.UNKNOWN;
    }

    @ExceptionHandler(RuntimeException.class)
    public Object RunExceptionHandler(RuntimeException e) {
        log.error(e.getMessage(), e);
        return getMessage(e);
    }

    @ExceptionHandler(ArithmeticException.class)
    public Object ArithmeticExceptionHandler(ArithmeticException e) {
        log.error(e.getMessage(), e);
        return ErrorCode.NotImplemented;
    }

    @ExceptionHandler(ApiException.class)
    public Object ApiExceptionHandler(ApiException e) {
        log.error(e.getMessage(), e);
        return e.response();
    }

    @ExceptionHandler(NotImplementedException.class)
    public Object NotImplementedExceptionHandler(NotImplementedException e) {
        log.error(e.getMessage(), e);
        return e.response();
    }

    @ExceptionHandler(ServerException.class)
    public Object ServerExceptionHandler(ServerException e) {
        log.error(e.getMessage(), e);
        return e.response();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                FieldError fieldError = (FieldError) errors.get(0);
                return ErrorCode.with(ErrorCode.ValidationFailed, fieldError.getDefaultMessage());
            }
        }
        return ErrorCode.ValidationFailed;
    }

    public JSONObject getMessage(RuntimeException e) {
        JSONObject resJson = new JSONObject();
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        JSONArray jsonArray = new JSONArray();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            String declaringClass = stackTraceElement.getClassName();
            if (declaringClass.startsWith("com.tz")) {
                String methodName = stackTraceElement.getMethodName();
                int lineNumber = stackTraceElement.getLineNumber();
                StringBuilder sb = new StringBuilder();
                sb = sb.append(declaringClass).append(":").append(lineNumber).append("--").append(methodName);
                jsonArray.add(sb.toString());
            }
        }
        resJson.put("code", "6601");
        resJson.put("message", jsonArray);
        return resJson;
    }
}
