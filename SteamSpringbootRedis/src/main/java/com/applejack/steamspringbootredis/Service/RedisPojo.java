package com.applejack.steamspringbootredis.Service;

/**
 * 用来存放一些Redis的前缀
 * @author Akemi0Homura
 */
public class RedisPojo {
    //注册流程中使用的前缀
    public static final String ENROLL_ID = "EnrollService:";
    //注册完成后，和登录成功后使用的前缀
    public static final String USER = "User:";
}
