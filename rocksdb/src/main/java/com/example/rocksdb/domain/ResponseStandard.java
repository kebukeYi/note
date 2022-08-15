package com.example.rocksdb.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName ResponseStandard
 * @Author kebukeyi
 * @Date 2022/8/15 10:56
 * @Description
 * @Version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStandard<T> implements Serializable {

    private int code;
    private String message;
    private T data;
    private int total;

    public static <T> ResponseStandard<T> successResponse(T t) {
        ResponseStandard<T> response = new ResponseStandard<>();
        response.setCode(0);
        response.setMessage("success!!");
        response.setData(t);
        response.setTotal(0);
        return response;
    }

    public static <T> ResponseStandard<T> failureResponse(T t) {
        ResponseStandard<T> response = new ResponseStandard<>();
        response.setCode(-1);
        response.setMessage("failure!!");
        response.setData(t);
        response.setTotal(0);
        return response;
    }
}
