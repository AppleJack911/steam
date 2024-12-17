package com.applejack.steamspringbootredis.Pojo.Bo;

/**
 * 确认对象，用来作为某件事情的确认，<br>
 * 用来前端在做确认操作时候，传送的对象。<br>
 * id:该对象的唯一id。msg：提示信息，可以用来做该对象解释，也能用来携带信息数据。<br>
 * state：true为确认，false为没有确认。
 * @author Akemi0Homura
 */
public class Confirm {
    private String id;
    private String msg;
    private Boolean state;

    @Override
    public String toString() {
        return "Confirm{" +
                "id='" + id + '\'' +
                ", msg='" + msg + '\'' +
                ", state=" + state +
                '}';
    }

    public Confirm(String id, String msg, Boolean state) {
        this.id = id;
        this.msg = msg;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Confirm() {
    }
}
