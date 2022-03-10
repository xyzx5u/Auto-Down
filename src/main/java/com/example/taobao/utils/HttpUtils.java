package com.example.taobao.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.taobao.entity.FileInfo;
import com.example.taobao.entity.FilePage;
import com.example.taobao.entity.HttpResult;
import okhttp3.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 通用http发送方法
 *
 * @author lzh
 */
public class HttpUtils
{

    public static final OkHttpClient.Builder builder = new OkHttpClient.Builder();


    public static final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");


    public static OkHttpClient client = builder
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            //https
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            //移动的代理
            //.proxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress(ip,port)))
            .build();


    public static RequestBody createBody(String data){
        return FormBody.create(data,mediaType);
    }

    /**
     * 创建Request
     * @param url
     * @param body
     * @param header
     * @return
     */
    public static Request createRequest(String url,RequestBody body,Map<String,String> header){
        Request.Builder requestBuilder = new Request.Builder();
        //url
        requestBuilder.url(url);
        //body
        if (body != null){
            requestBuilder.post(body);
        }
        //header
        for (Map.Entry<String, String> entry : header.entrySet()) {
            requestBuilder.addHeader(entry.getKey(),entry.getValue());
        }
        return requestBuilder.build();
    }



    /**
     *
     * 免费参数 free=1
     * @param km 科目
     * @param type 类型
     */
    public static List<FileInfo> getFile(String km,String type,String path) {
        List<FileInfo> fileInfos = new ArrayList<>();
        String url = "https://"+km+".zxxk.com"+type+"index-%s.html?order=updatetime_desc";
        FilePage filePage = null;
        int pageIndex = 1;
        do {
            System.out.println(pageIndex);
            filePage = HtmlUtil.getFile(getListHtml(String.format(url, pageIndex)),path);
            fileInfos.addAll(filePage.getFileInfos());
            pageIndex++;
        }while (filePage.getHashNext());
        return fileInfos;
    }

    /**
     * km: yy,yw
     * paper: papers,books
     * type:试卷/3103,课件/3101,教案/3102
     * index:分页，第一页为空，第二页/index-2.html
     * https://{km}.zxxk.com/h/{paper}-type{type}/{index}?order=updatetime_desc
     * 第一页：https://yy.zxxk.com/h/papers-type3103/
     * 翻页：https://yy.zxxk.com/h/papers-type3103/index-2.html
     * 时间排序 https://yy.zxxk.com/h/papers-type3103/index-2.html?order=updatetime_desc
     * @param url
     * @return
     */
    public static String getListHtml(String url){
        Map<String,String> headers = new HashMap<>();
        Request request = createRequest(url,null,headers);
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    免费下载
    会员下载
     */

    /**
     * 获取Nbkey 非会员获取不到
     * userid
     * @return
     */
    public static String getNbKey(String softId,String userId) {
        String url = "https://downloadnew.zxxk.com/SettlementConfirm.ashx?jsonp=jsonp&userid=%s&softid=%s&key=0.9129249957975534&callback=jQuery110207936889147255879_1646278695957&_=%s";
        //时间戳
        Long time = System.currentTimeMillis();
        url = String.format(url,userId,softId,time);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36")
                .build();
        String res = null;
        try (Response response = client.newCall(request).execute()) {
            res = response.body().string();
            res = res.substring(res.indexOf("{"), res.lastIndexOf("}") + 1);
            JSONObject object = JSON.parseObject(res);
            if (object.get("NbKey")!=null){
                return object.get("NbKey").toString();
            }
        } catch (IOException e) {
            System.out.println(res);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取生成链接js
     *
     * @param cookie  登录cookie
     * @param softIds 文件id
     * @param NbKey   文件Nbkey
     * @return
     */
    public static HttpResult getScripts(String cookie, String softIds, String NbKey) {
        String url = "https://resourcedownload.zxxk.com/download/getscripts?softids=%s&iscart=0&source=0&ext=&isGaoKao=false&resType=0&multipackage=0&r=0.9052795016027293&product=1&nbkey=%s";
        url = String.format(url, softIds, NbKey);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36")
                //.addHeader("Cookie", cookie)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();

            HttpResult httpResult = new HttpResult();
            List<String> cookies = response.headers("Set-Cookie");
            httpResult.setCookie(cookieS2Str(cookies));
            httpResult.setRespose(getScriptUrl(res));
            return httpResult;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行js获取url
     * @param script
     * @return
     */
    public static String getScriptUrl(String script){
        try {
            script = script.replaceAll("document.getElementById\\('downloadframe'\\).src=", "url = ");
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
            engine.eval("var url=''; " + script);
            return engine.get("url").toString();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取生成链接js
     *
     * @param cookie cookie
     * @return
     */
    public static String getFileUrl(String cookie, String url,boolean free) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36")
                .addHeader("Cookie", cookie)
                .addHeader("Connection", " keep-alive")
                .addHeader("Accept", "*/*")
                .build();
        try (Response response = client.newCall(request).execute()) {
            //免费
            if (free){
                return HtmlUtil.getDownUrl(response.body().string());
            }else{
                /*
                 * 返回的链接： https://downloadweb.zxxk.com/filedownload/ShowDownLoadView?mkey=7ce2d8b88073a70081ca9ceba0b3dfd1&amp;timestamp=1646747392.58244&amp;sn=0c4e70baf67c9937c6b6dba40f84695a
                 * 真实下载链接：https://downloadweb.zxxk.com/FileDownLoad/GetFileAddress?callback=jQuery110208286841578081416_1646297958094&mkey=7ce2d8b88073a70081ca9ceba0b3dfd1&timestamp=1646747392.58244&sn=0c4e70baf67c9937c6b6dba40f84695a&_=1646298436885
                 */
                String filedownload = HtmlUtil.getDownUrl(response.body().string());
                //后台可能在生成文件
                Thread.sleep(1000);
                String trueUrl = HtmlUtil.getVipUrl(filedownload);
                return getVipFile(cookie,trueUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVipFile(String cookie,String url){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36")
                .addHeader("Cookie", cookie)
                .addHeader("Connection", " keep-alive")
                .addHeader("Accept", "*/*")
                .build();
        try (Response response = client.newCall(request).execute()) {
                String res = response.body().string();
            if (!res.contains("null")){
                res = res.substring(res.indexOf("{"), res.lastIndexOf("}")+1);
                JSONObject jsonObject = JSON.parseObject(res);
               return jsonObject.get("Info").toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 下载文件
     * @param url
     * @return
     */
    public static boolean download(String url,String path){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.0 Safari/537.36")
                .build();
        ByteArrayOutputStream baos = null;
        try (okhttp3.Response response = client.newCall(request).execute()) {

            String Disposition = URLDecoder.decode(response.header("Content-Disposition"),"UTF-8");

            String fileName = HtmlUtil.getHeaderFileName(Disposition);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path+"/"+fileName));

            InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
            byte[] buffer = new byte[2048];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
            fileOutputStream.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null!=baos){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }




    /**
     * 获取cookie
     * @param cookies
     * @return
     */
    public static String cookieS2Str(List<String> cookies){
        StringBuilder sb = new StringBuilder();
        for (String cookie : cookies) {
            cookie = cookie.substring(0,cookie.indexOf(";"));
            String[] coos = cookie.split("=");
            sb.append(coos[0]).append("=").append(coos[1]).append(";");
        }
        return sb.toString();
    }
}
