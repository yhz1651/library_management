package com.example.springboot.common;

import lombok.Data;

@Data
public class Result {
    private static final String SUCCESS_CODE = "200"; // 正确时的code
    private static final String ERROR_CODE = "-1"; // 出错时的code

    private String code; // 成功或者失败返回的代码
    private Object data; // 后台数据
    private String msg; // 错误信息

    public static Result success() { // 成功，不返回数据
        Result result = new Result();
        result.setCode(SUCCESS_CODE);
        return result;
    }

    // Object类是所有类的父类，利用Object类可以接收全部类的对象，因为可以向上自动转型
    public static Result success(Object data) { // 成功，返回数据
        Result result = new Result();
        result.setCode(SUCCESS_CODE);
        result.setData(data);
        return result;
    }

    public static Result error(String msg) { // 出错，返回错误信息
        Result result = new Result();
        result.setCode(ERROR_CODE);
        result.setMsg(msg);
        return result;
    }

    public static Result error(String code,String msg) { // 出错，返回错误信息
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
