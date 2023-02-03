package com.tz.core.exception;

/**
 * 服务端出现问题，抛出此异常返回给调用接口方错误描述
 */
public  class ServerException extends ApiException {


    public ServerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ServerException(String description) {
        super(ErrorCode.CommonServerException,description);
    }
}
