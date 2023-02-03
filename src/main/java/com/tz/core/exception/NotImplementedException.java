package com.tz.core.exception;

public class NotImplementedException extends ApiException {
  public NotImplementedException() {
    super(ErrorCode.NotImplemented);
  }
}
