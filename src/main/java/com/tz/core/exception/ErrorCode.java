package com.tz.core.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class ErrorCode implements Serializable {

    private int code;
    private String message;


    public static ErrorCode with(ErrorCode code, String message) {
        return new ErrorCode(code.code, message);
    }

    public static ErrorCode with(int code, String message) {
        return new ErrorCode(code, message);
    }

    public static final ErrorCode UNKNOWN = ErrorCode.with(-1, "服务器发生了未知异常，请稍后尝试或联系管理员");
    public static final ErrorCode NotImplemented = ErrorCode.with(-2, "当前接口尚未实现，后期会实现该接口");
    public static final ErrorCode Unauthorized = ErrorCode.with(401, "访问该接口需要经过认证");
    public static final ErrorCode LoginFails = ErrorCode.with(402, "用户名或密码错误");
    public static final ErrorCode Forbidden = ErrorCode.with(403, "权限不足，禁止访问接口");
    public static final ErrorCode InvalidToken = ErrorCode.with(4011, "token不正确，请重新获取");
    public static final ErrorCode TokenExpired = ErrorCode.with(4012, "token过期，请刷新token或重新获取");
    public static final ErrorCode CommonClientException = ErrorCode.with(6001, "客户端异常");
    public static final ErrorCode CommonServerException = ErrorCode.with(6002, "服务端异常");
    public static final ErrorCode remoteCallFail = ErrorCode.with(6003, "远程回调失败");
    public static final ErrorCode ValidationFailed = ErrorCode.with(6004, "参数校验失败");
    public static final ErrorCode RunTimeFailed = ErrorCode.with(601, "运行时异常");
    public static final ErrorCode ASRFailed = ErrorCode.with(701, "语音转写出现问题");

    public static List<ErrorCode> getCodes() {
        final List<ErrorCode> CODES = new ArrayList<>();
        for (Field field : ErrorCode.class.getFields()) {
            if (field.getType().isAssignableFrom(ErrorCode.class)) {
                try {
                    CODES.add((ErrorCode) field.get(ErrorCode.class));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        CODES.sort(Comparator.comparingInt(o -> o.code));
        return CODES;
    }
}
