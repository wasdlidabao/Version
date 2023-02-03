package com.tz.core.commons;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yyc
 * @Date 2021-11-22 15:41
 */
@Data
public class Result<T> implements Serializable {
    private Integer code;
    private String message;
    private boolean success = true;
    private T data;


    private Result() {
        this.code = 200;
        this.message = "成功";
        this.data = null;
    }


    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(data);
        return result;
    }
}
