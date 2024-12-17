package com.applejack.steamspringbootredis.Util;

import com.Akemi0Homura.StringLibrary;
import com.applejack.steamspringbootredis.Controller.Result.Result;
import com.applejack.steamspringbootredis.Pojo.Dto.Userinfo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用来做对象属性值的合法性检查
 * @author Akemi0Homura
 */
@Component
public class Verify {
    @Autowired
    private com.applejack.steamspringbootredis.Pojo.Dao.AddressDao addressDao;
    private static com.applejack.steamspringbootredis.Pojo.Dao.AddressDao staticAddressDao;

    @PostConstruct
    public void init() {
        staticAddressDao = addressDao;
    }

    /**
     * 判断Result是否为null
     * @param result 传输对象
     * @return 如果有，返回true。反之返回false
     */
    public static boolean Result(Result result) {
        if(result == null)return false;
        return result.getData() != null;
    }

    /**
     * 这个方法是专门用来验证Userinfo这个对象里面属性的合法性。<br>
     * 1.只检查email和addressId属性。2.只检查username和password属性。
     * @param userinfo 验证对象
     * @param x 业务场景，不同的数字代表不同的业务场景，也代表着不同的处理情况
     * @return true为合法，false为违法
     */
    public static boolean Userinfo(Userinfo userinfo,int x) {
        switch (x){
            //只检查email和addressId属性
            case 1:{
                //检查电子邮箱
                if(!StringLibrary.EmailFormat(userinfo.getEmail()))return false;

                //检查国家和地区
                String id=userinfo.getAddressId().toString();
                //首先检查Redis中是否存在
                if(Redis.get("address:id:"+id,Integer.class)==null){
                    //如果不存在，则去mysql查询
                    if(staticAddressDao.selectById(Integer.parseInt(id))!=null){
                        //如果mysql存在，就把这个数据存入redis，设置30分钟过期时间
                        Redis.put("address:id:"+id,userinfo.getAddressId(),30);
                        return true;
                    }else{
                        //如果连mysql都没有，就返回false
                        return false;
                    }
                }else{
                    //存在，那就说明这个数据是从mysql获取的，因此就直接返回true
                    return true;
                }

            }
            //只检查username和password属性
            case 2:{
                //账户，必须是任意大小写字母+数字。不允许特殊符号
                if(!StringLibrary.AnyAlphabetNumbers(userinfo.getUsername()))return false;
                //密码允许任意大小写字母数字和特殊符号，因此这里只做为null和空字符串等判断
                return !StringLibrary.NoNull(userinfo.getPassword()) || StringLibrary.NoSpace(userinfo.getUsername());
            }
            default:throw new RuntimeException("没有你需要的业务场景");
        }
    }
}
