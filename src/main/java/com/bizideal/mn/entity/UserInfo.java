package com.bizideal.mn.entity;

import java.io.Serializable;

/**
 * @author: liulq
 * @Date: 2018/3/10 13:37
 * @Description:
 * @version: 1.0
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 963061236984604666L;

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public UserInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserInfo setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
