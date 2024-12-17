package com.applejack.steamspringbootredis.Pojo.Dto;

/**
 * address表对象<br>
 * id：国家地区ID。region：国家地区名字。前面的CN和EN只是对应国家的语言名称而已
 * @author Akemi0Homura
 */
public class Address {
    private Integer id;
    private String CNregion;
    private String ENreqgion;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", CNregion='" + CNregion + '\'' +
                ", ENreqgion='" + ENreqgion + '\'' +
                '}';
    }

    public Address(Integer id, String CNregion) {
        this.id = id;
        this.CNregion = CNregion;
    }

    public Address(Integer id, String CNregion, String ENreqgion) {
        this.id = id;
        this.CNregion = CNregion;
        this.ENreqgion = ENreqgion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCNregion() {
        return CNregion;
    }

    public void setCNregion(String CNregion) {
        this.CNregion = CNregion;
    }

    public String getENreqgion() {
        return ENreqgion;
    }

    public void setENreqgion(String ENreqgion) {
        this.ENreqgion = ENreqgion;
    }

    public Address() {
    }
}