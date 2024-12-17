package com.applejack.steamspringbootredis.Util;

import com.Akemi0Homura.StringLibrary;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 封装了Redis相关的函数，
 * @author Akemi0Homura
 */

@Component
public class Redis {
    //日志对象
    private static final Logger logger= LoggerFactory.getLogger(Redis.class);

    @Autowired
    private StringRedisTemplate template;
    private static StringRedisTemplate StaticTemplate;

    @PostConstruct
    public void init() {
        StaticTemplate = template;
    }

    /**
     * 向Redis添加和修改，value值为String
     * @param key key值
     * @param value value值
     * @param time 分钟,如果输入负数，则永不过期
     * @return 添加或者修改成功返回true，反之返回false
     */
    public static boolean put(String key,Object value,int time){
        try {
            //序列化对象
            String str= StringLibrary.SetJson(value);
            //往Redis添加数据
            if(time>0){
                StaticTemplate.opsForValue().set(key,str,time, TimeUnit.MINUTES);
            }else{
                StaticTemplate.opsForValue().set(key,str);
            }
            //Redis的set方法是void，这里通过抛异常来判断是否添加成功
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 从Redis中获取数据
     * @param key key值
     * @param clazz 对象类型
     * @return 返回对象
     * @param <T> 泛型
     */
    public static <T> T get(String key,Class<T> clazz){
        //从Redis获取数据
        String str= StaticTemplate.opsForValue().get(key);
        if(str==null)return null;
        return StringLibrary.GetJson(str,clazz);
    }

    /**
     * 删除redis中的数据
     * @param key key值
     * @return true删除成功，反之失败
     */
    public static boolean del(String key){
        try {
            StaticTemplate.delete(key);
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }
}
