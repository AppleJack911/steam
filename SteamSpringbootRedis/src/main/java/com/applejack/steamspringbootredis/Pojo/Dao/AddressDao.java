package com.applejack.steamspringbootredis.Pojo.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Address表的jdbc操作接口
 * @author Akemi0Homura
 */

//标记为mybatis的mapper对象，在调用方法的时候，mybatis会自动实现方法
@Mapper
public interface AddressDao {
    //Select就是标注这是一个添加的方法，如果用@delete就是标注为删除方法
    //写sql代码，并且表需要用架构名点出来
    //value里面填写的#{传参对象}
    //返回值为布尔类型，也就是如果什么都查不到，就是false
    @Select("select * from steam.address where id=#{id}")
    Boolean selectById(Integer id);
}
