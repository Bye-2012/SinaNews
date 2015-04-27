package com.moon.db.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/25
 */
@Table(name = "userinfo")
public class UserInfo {
    @Id(column = "_id")
    private long id;

    @Column(column = "username")
    private String username;

    @Column(column = "password")
    private String password;

    @Column( column = "nickname",defaultValue = "")
    private String nickname;

    @Column( column = "sex",defaultValue = "")
    private String sex;

    @Column( column = "createtime",defaultValue = "")
    private String createtime;

    @Column( column = "token",defaultValue = "")
    private String token;

    @Column( column = "headimage",defaultValue = "")
    private int headimage;

    @Column( column = "phonenumber",defaultValue = "")
    private int phonenumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getHeadimage() {
        return headimage;
    }

    public void setHeadimage(int headimage) {
        this.headimage = headimage;
    }

    public int getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(int phonenumber) {
        this.phonenumber = phonenumber;
    }
}
