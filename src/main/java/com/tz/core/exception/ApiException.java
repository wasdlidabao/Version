package com.tz.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * project: demo author: bins date: 18-4-3
 */
public  class ApiException extends RuntimeException {

  private ErrorCode errorCode;

  public ApiException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public ApiException(ErrorCode errorCode, String description) {
    this.errorCode  = errorCode;
    this.errorCode.setMessage(description);
  }


  public ApiException(String description) {
    this.errorCode  = ErrorCode.with(6002,description);
  }


  public ApiException(int code, String msg) {
    this.errorCode  = ErrorCode.with(code,msg);
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public ResponseEntity<ErrorCode> response() {
    return new ResponseEntity<>(errorCode, HttpStatus.OK);
  }

  @Override
  public String toString() {
    return "ApiException{" +
            "errorCode=" + errorCode.toString() +
            '}';
  }
}
