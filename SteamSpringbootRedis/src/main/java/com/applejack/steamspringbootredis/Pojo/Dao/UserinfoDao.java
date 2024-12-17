package com.applejack.steamspringbootredis.Pojo.Dao;

import com.applejack.steamspringbootredis.Pojo.Dto.Userinfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * 表的userinfoDao操作接口
 * @author Akemi0Homura
 */

//标记为mybatis的mapper对象，在调用方法的时候，mybatis会自动实现方法
@Mapper
public interface UserinfoDao {
    //Insert就是标注这是一个添加的方法，如果用@delete就是标注为删除方法
    //写sql代码，并且表需要用架构名点出来
    //value里面填写的#{传参对象里的属性}
    @Insert("insert into steam.userinfo(email, addressId, username, password)" +
            "value (#{email},#{addressId},#{username},#{password})")
    //插入数据后，携带自动增长的id返回过来，返回的id是存在传参中的userinfo对象里的id属性。
    //返回值布尔类型是判断操作是否成功
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Boolean addUserinfo(Userinfo userinfo);

    //根据username查询表中内容，返回对象
    @Select("select * from steam.userinfo where username=#{username}")
    Userinfo selectByUsername(String username);
}
