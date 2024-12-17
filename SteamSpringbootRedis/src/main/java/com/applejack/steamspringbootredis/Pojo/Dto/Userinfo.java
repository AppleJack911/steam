package com.applejack.steamspringbootredis.Pojo.Dto;

/**
 * userinfo表对象<br>
 * id：uid。email：电子邮箱。addressId：国家地区ID<br>
 * username：账户名称。password：账户密码
 * @author Akemi0Homura
 */
public class Userinfo {
    private Integer id;
    private String email;
    private Integer addressId;
    private String username;
    private String password;

    @Override
    public String toString() {
        return "Userinfo{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", addressId=" + addressId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Userinfo(String email, Integer addressId, String username, String password) {
        this.email = email;
        this.addressId = addressId;
        this.username = username;
        this.password = password;
    }

    public Userinfo(Integer id, String email, Integer addressId, String username, String password) {
        this.id = id;
        this.email = email;
        this.addressId = addressId;
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Userinfo() {
    }
}