package com.applejack.steamspringbootredis.Service;

import com.Akemi0Homura.StringLibrary;
import com.applejack.steamspringbootredis.Controller.Result.Code;
import com.applejack.steamspringbootredis.Controller.Result.Result;
import com.applejack.steamspringbootredis.Pojo.Bo.Confirm;
import com.applejack.steamspringbootredis.Pojo.Dao.UserinfoDao;
import com.applejack.steamspringbootredis.Pojo.Dto.Userinfo;
import com.applejack.steamspringbootredis.Util.Redis;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 负责注册页面的业务代码
 * @author Akemi0Homura
 */

//相当于把整个对象注入到spring容器中
@Service
public class EnrollService {
    @Autowired
    com.applejack.steamspringbootredis.Pojo.Dao.UserinfoDao userinfoDao;

    //注册流程中使用的前缀
    public final String ENROLL_ID = RedisPojo.ENROLL_ID;
    //注册完成后，和登录成功后使用的前缀
    public final String USER = RedisPojo.USER;

    //邮箱验证过期时间
    private final int TIME1 = 5;
    //邮箱验证完成后，给与用户输入账户和密码的时间
    private final int TIME2 = 15;
    //注册好的账户或者查询到重名的账户，存入Redis时间
    private final int TIME3 = 30;

    //随机数生成长度
    private final int RADNDOM_LENGTH = 9;
    //随机数类型
    private final int RADNDOM_TYPE=5;

    /**
     * 生成一个Confirm对象，发送给前端<br>
     * 然后将前端发送的对象存入Redis中，并把Confirm对象的id作为key值绑定
     * @param userinfo 用户对象，不过仅有地区和电子邮箱
     * @return 返回确认对象
     */
    public Result<Confirm> UserEnroll1(Result<Userinfo> userinfo) {
        //生成确认对象，id为随机12位 数字+字母+字符 生成
        String id=StringLibrary.RandomString(RADNDOM_LENGTH,RADNDOM_TYPE);

        //将确认对象存入Redis中，并且做一个存入成功判断
        if(Redis.put(ENROLL_ID+id,userinfo.getData(),TIME1)){
            Confirm x=new Confirm(id,"用来确认用户是否登录",false);
            return new Result<>(x, Code.A2);
        }else{
            //走到这里，可能是Json的原因？或者Redis的原因？
            return new Result<>(Code.B2,"服务器繁忙");
        }
    }

    /**
     * 查询Redis，确认用户是确定注册
     * 然后把Redis中，用确认对象的id作为key值的Userinfo对象，延长15分钟
     * @param confirm 确认对象
     * @return 返回确认对象
     */
    public Result<Confirm> UserEnroll2(Result<Confirm> confirm) {
        //首先，先检查一下确认对象是否还过期
        Userinfo x=Redis.get(ENROLL_ID+confirm.getData().getId(),Userinfo.class);
        if(x!=null){
            //重新生成一个随机数
            String id=StringLibrary.RandomString(RADNDOM_LENGTH,RADNDOM_TYPE);

            //将之前的Redis的信息删除
            if(!Redis.del(ENROLL_ID+confirm.getData().getId()))return new Result<>(Code.B2,"邮箱验证已过期，请重新注册");

            //重新存入Redis，并且过期时间是15分钟
            Redis.put(ENROLL_ID+id,x,TIME2);

            //返回的给前端，并且携带新的ID
            return new Result<>(Code.A2,id);
        }else{
            //如果等于空，说明用户验证过期
            return new Result<>(Code.B2,"邮箱验证已过期，请重新注册");
        }
    }

    /**
     * Result中msg携带随机id，将这个id拿去Redis中查找
     * 如果找到，就把之前存入对象的电子邮箱和国家地区和这个对象的账户密码合体
     * 先查询Redis，是否有重名的账户名。
     * 然后查询mysql，看是否有重复的账户名，如果有就不允许注册。
     * 如果没有重复账户名，那么就把存入mysql中，然后拿到mysql的自动增长id
     * 把id赋给对象，形成完整的账户对象后，存入Redis中，方便后续登录和查找使用
     * 使用账户名作为key值
     * @param userinfo 账户对象
     * @return null
     */
    @Transactional
    public Result<String> UserEnroll3(Result<Userinfo> userinfo){
        //首先去查找下Redis库中，看下旧对象是否还存在。随机id是存放在msg中
        Userinfo old=Redis.get(ENROLL_ID+userinfo.getMsg(),Userinfo.class);

        //如果不为null，说明旧对象没过期
        if(old!=null){
            //接下来就该检查下，账户名是否重复。先查一遍Redis
            if(Redis.get(USER+userinfo.getData().getUsername(),Userinfo.class)==null){
                //如果Redis不存在，就查询下Mysql
                Userinfo x=userinfoDao.selectByUsername(userinfo.getData().getUsername());
                if(x==null){
                    //如果mysql也不存在，那就可以存入mysql
                    old.setUsername(userinfo.getData().getUsername());
                    //密码要做SHA256哈希加密
                    String str=StringLibrary.SHA256Encrypt(userinfo.getData().getPassword());
                    old.setPassword(str);

                    //添加进mysql中,然后获取自动增长的id
                    if(userinfoDao.addUserinfo(old)){
                        //OK,把这个账户存入redis中，方便后续登录等操作
                        Redis.put(USER+old.getUsername(),old,TIME3);
                        //然后删除掉之前的确认对象
                        Redis.del(ENROLL_ID+userinfo.getMsg());

                        //到这，注册完成
                        return new Result<>(Code.A1);
                    }else{
                        return new Result<>(Code.B2,"服务器繁忙");
                    }

                }else{
                    Redis.put(USER+x.getUsername(),x,TIME3);
                    return new Result<>(Code.B2,"账户名已存在");
                }
            }else{
                return new Result<>(Code.B2,"账户名已存在");
            }
        }else{
            return new Result<>(Code.B2,"注册时间超时。");
        }
    }
}
