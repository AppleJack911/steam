package com.applejack.steamspringbootredis.Controller;

import com.applejack.steamspringbootredis.Controller.Result.Code;
import com.applejack.steamspringbootredis.Controller.Result.Result;
import com.applejack.steamspringbootredis.Pojo.Bo.Confirm;
import com.applejack.steamspringbootredis.Pojo.Dto.Userinfo;
import com.applejack.steamspringbootredis.Util.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 负责接受注册的请求
 * @author Akemi0Homura
 */
//RestFul风格，限定前端get和post等请求，不能乱发
//也就是说，get方法，前端用post形式发过来，后端是接不到的，必须是get方法
@RestController
//接口方法地址的前缀
@RequestMapping("/enroll")
public class EnrollWeb {
    //注解注入
    @Autowired
    com.applejack.steamspringbootredis.Service.EnrollService enrollService;

    /**
     * 接受前端数据，并且做对象规范性检查
     * @param userinfo 用户对象
     * @return 确认对象
     */
    @PostMapping("/1")
    public Result<Confirm> UserEnroll1(@RequestBody Result<Userinfo> userinfo) {
        Result<Confirm> r=new Result<>(null,Code.B2,"用户输入的数据不和规范");

        //判断前端传来是否为null
        if(!Verify.Result(userinfo))return r;

        //前端传输的对象进行规范进检查
        if(!Verify.Userinfo(userinfo.getData(),1)){
            //不合格就返回给前端
            return r;
        }
        return enrollService.UserEnroll1(userinfo);
    }

    @PostMapping("/2")
    public Result<Confirm> UserEnroll2(@RequestBody Result<Confirm> confirm) {
        Result<Confirm> r=new Result<>(null,Code.B2,"用户输入的数据不和规范");

        //判断前端传来是否为null
        if(!Verify.Result(confirm))return r;

        //如果前端返回的true，则执行业务方法
        //否则说明数据有问题，直接返回去
        if(confirm.getData().getState()){
            return enrollService.UserEnroll2(confirm);
        }else{
            return r;
        }
    }

    @PostMapping("/3")
    public Result<String> UserEnroll3(@RequestBody Result<Userinfo> userinfo) {
        Result<String> r=new Result<>(null,Code.B2,"用户输入的数据不和规范");

        //判断前端传来是否为null
        if(!Verify.Result(userinfo))return r;

        //判断下账户和密码是否正确符合规范
        if(!Verify.Userinfo(userinfo.getData(),2)){
            //不合格就返回给前端
            return r;
        }

        return enrollService.UserEnroll3(userinfo);
    }
}
