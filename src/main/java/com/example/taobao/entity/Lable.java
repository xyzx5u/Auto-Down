package com.example.taobao.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @创建人: lzh
 * @创建时间: 2022/2/16
 * @描述:
 */
public class Lable implements Serializable {

    private String name;

    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Lable{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
