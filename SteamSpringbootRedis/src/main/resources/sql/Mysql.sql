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