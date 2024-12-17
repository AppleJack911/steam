# Steam后端实现

## 创建项目

项目基于**jar17**实现，使用技术栈：ssm+springboot+mysql+redis

**创建项目**时候勾选两个插件，**分别为web层下的springweb和nosql层下的spring data redis**

## 技术栈说明

在开发前，先了解下各个技术栈作用

1. ssm：就是一个组合的名字，组合中包含mybatis、spring、springmvc
   1. mybatis：数据库访问层框架，**简化jdbc操作**
   2. spring：**spring所有相关的核心框架前置依赖**。可以快速实现代理模式，不修改源代码的情况下修改方法。（开发中一般用不上，基本都是开发结束后维护使用的）
   3. springmvc：网络层框架，**负责接受和发送前端数据**
2. Springboot：集成了大量开发常用框架组件，并且**内置tomcat服务器**
3. redis：**数据缓存**，类似于一个哈希表，操作数据库的时候，获取到的信息都会存储到redis，下次使用就可以直接从redis获取，**提升速度**

## 注册

根据steam官方流程：

前端发送注册信息（**邮箱，地区**）=>邮箱验证登录（点击确认）=>前端再次发送注册信息（**账户名称，密码**）

这里转化成后端具体实现逻辑：

1. 接受前端注册请求（电子邮箱和地区）
2. 后端生成临时确认对象给前端（id）
3. 前端修改确认对象返回后端，后端确认id无误后，重新生成临时id给前端
4. 前端拿着数据（账户和密码）并且携带重新生成好的临时id给后端
5. 后端检查数据库如果账户没有重复，存入数据库中。注册完成

ok，逻辑理顺，开始开发

1. 首先，先**整理好文件目录**

   ```
   com.作者.项目名
   	Cofig				//java程序的配置类，负责配置项目程序
   	Controller			//网络接口层，负责和前端交互
   		Result			//前后端交互统一对象
   	Pojo				//对象层
   		Bo				//其他对象
   		Dao				//数据库表的对象
   		Dto				//jdbc操作接口
   	Service				//业务层
   	Util				//常用公共方法的工具库
   ```

2. 然后创建统一对象

   ```java
   /**
    * 枚举在json化的时候，会转换成字符串<br>
    * A1:业务完成，只有状态码。A2:业务完成，有状态码和返回值。
    * A3:业务完成，有状态码、返回值、提示信息<br>
    * B1:业务失败，只有状态码。B2:业务失败，有状态码和提示信息。
    * B3:业务失败，有状态码、返回值、提示信息
    * @author Akemi0Homura
    */
   public enum Code {
       A1,A2,A3,
       B1,B2,B3;
   }
   
   
   /**
    * 前后端交互统一对象
    * 如果不需要data属性，泛型声明为String
    * @author Akemi0Homura
    */
   public class Result<T> {
       T data;             //泛型，传输对象
       Code code;          //枚举对象，状态码
       String msg;     //提示信息
       
       //无参有参构造函数、get和set、toString方法
   }
   ```

3. 后端开发，**从下往上**，也就是先从数据库=>业务层=>接口层

4. 先生成好Mysql数据库

   ```sql
   create database Steam;
   
   # 用来做测试的表
   create table Steam.test(
       id int primary key auto_increment,
       name varchar(20)
   );
   
   # 用来存放用户注册信息的表
   # uid、电子邮箱、国家地区id（对应购买时候的货币单位）、账户名、密码
   create table Steam.userInfo(
       id int primary key auto_increment,
       email varchar(40) NOT NULL ,
       addressId int NOT NULL ,
       username varchar(40) NOT NULL ,
       #密码需要加密保存，因为采用sha256算法，因此采用64。因为是固定长度，所以是char
       password char(64) NOT NULL
   );
   
   # uid、国家地区名字
   create table Steam.address(
       id int primary key auto_increment,
       #中文国家地区
       CNregion varchar(10) NOT NULL,
       #英文国家地区
       ENreqgion varchar(15) not null
   );
   
   ALTER TABLE Steam.userInfo ADD FOREIGN KEY (addressId) REFERENCES Steam.address(id);
   
   INSERT INTO Steam.address(CNregion, ENreqgion) value ('中国','China');
   INSERT INTO Steam.address(CNregion, ENreqgion) value ('美国','American');
   ```

5. 然后写好**数据库对象**和**数据库方法**

   ```java
   Dto层：
   public class Address {
       private Integer id;
       private String CNregion;
       private String ENreqgion;
       
       //无参有参构造函数、get和set、toString方法
   }
   
   public class Userinfo {
       private Integer id;
       private String email;
       private Integer addressId;
       private String username;
       private String password;
       
       //无参有参构造函数、get和set、toString方法
   }
   
   
   
   Dao层：
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
   ```

6. 然后就是业务逻辑层，业务逻辑层几乎所有方法都需要操作Redis，因此这里在Util层封装一个Redis工具类

   ```java
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
   ```

   这里面的代码用到了**StringLibrary类**，可以点进去自行研究，里面有注释写的很明白

7. 然后开始写业务层代码，首先解决第一个，也就是接受到前端的电子邮箱和地区后，发送确认对象给前端。代码如下

   ```java
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
   ```

8. 完成后，就是网络层的代码，**网络层**需要对前端传来的对象，**做对象合法校验**，避免传来对象不合法。因此**在util工具库层，封装一个检查注册表对象的类**

   ```java
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
   ```

9. 然后就开始编写网络层代码

   ```java
   /**
    * 负责接受注册的请求
    * @author Akemi0Homura
    */
   @RestController
   @RequestMapping("/enroll")
   public class EnrollWeb {
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
   }    
   ```

   为了方便区分和开发，我在这里，**业务层方法和网络层同名**。

10. ok，发出确认对象后，前期就会返回这个确认对象。那么开发过程同理，因为数据库层和方法工具等都开发完毕，因此这里**直接就能编写业务代码**

    ```java
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
    ```

11. 业务写完，就写个接口层调用即可

    ```java
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
    ```

12. ok，现在还剩下最后一环，就是接受前端发来的账户和密码，然后查询数据库是否重名，有就返回，没有就存入数据库。也是和上面一样，直接就能编写业务代码和接口。这里就不作讲解。

