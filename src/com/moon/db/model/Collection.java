package com.moon.db.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/25
 */

@Table(name = "collect")
public class Collection {
    @Id(column = "_id")
    private long id;

    @Column(column = "user")
    private String user;

    @Column(column = "item_id")
    private int item_id;

    @Column(column = "delete_flag",defaultValue = "N")
    private String delete_flag;

    @Column(column = "title")
    private String title;

    @Column(column = "content")
    private String content;

    @Column(column = "create_time")
    private String create_time;

    @Column(column = "head_img")
    private String head_img;

    @Column(column = "cmt_count")
    private int cmt_count;

    @Column(column = "type")
    private String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(String delete_flag) {
        this.delete_flag = delete_flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public int getCmt_count() {
        return cmt_count;
    }

    public void setCmt_count(int cmt_count) {
        this.cmt_count = cmt_count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
