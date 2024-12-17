package com.applejack.steamspringbootredis.Controller.Result;

/**
 * 前后端交互统一对象
 * 如果不需要data属性，泛型声明为String
 * @author Akemi0Homura
 */
public class Result<T> {
    T data;             //泛型，传输对象
    Code code;          //枚举对象，状态码
    String msg;     //提示信息

    public Result(T data, Code code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public Result(T data,Code code) {
        this.code = code;
        this.data = data;
    }

    public Result(Code code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Code code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result() {
    }
}
