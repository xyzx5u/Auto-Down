package com.example.taobao.entity;

/**
 * @创建人: lzh
 * @创建时间: 2022/3/7
 * @描述:
 */
public class HttpResult {

    private String cookie;

    private String respose;

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getRespose() {
        return respose;
    }

    public void setRespose(String respose) {
        this.respose = respose;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "cookie='" + cookie + '\'' +
                ", respose='" + respose + '\'' +
                '}';
    }
}
