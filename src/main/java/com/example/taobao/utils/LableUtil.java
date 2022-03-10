package com.example.taobao.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.taobao.entity.Lable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @创建人: lzh
 * @创建时间: 2022/2/16
 * @描述:
 */
public class LableUtil {

    public static final List<Lable> subjects;
    public static final List<Lable> filters;
    public static final List<Lable> navigations;

    static {
        String json = LableUtil.getJson();
        JSONObject jsonObject = JSON.parseObject(json);
        subjects = LableUtil.getLableByTitle("subject",jsonObject);//
        filters = LableUtil.getLableByTitle("filter",jsonObject);//
        navigations = LableUtil.getLableByTitle("navigation",jsonObject);//
    }

//    public static void main(String[] args) {
//        List<Lable> lables = new ArrayList<>();
//        Lable lable = null;
//        for (Lable subject : filters) {
//            lable = new Lable();
//            lable.setName(subject.getName());
//            //lable.setUrl(subject.getUrl().substring(8,subject.getUrl().indexOf(".")));
//            System.out.println(subject.getUrl());
//            lables.add(lable);
//        }
//        //System.out.println(JSON.toJSONString(lables));
//    }

    /**
     * 根据title找到对应的标签
     * @param title
     * @return
     */
    public static List<Lable> getLableByTitle(String title,JSONObject jsonObject){
        return JSON.parseArray( jsonObject.get(title).toString(),Lable.class);
    }

    /**
     * 获取json文件
     * @return
     */
    public static String getJson(){
        try {
            String basePath = new File("").getAbsolutePath();
            File file = new File(basePath + "/data/lables.json");
            BufferedReader reader = new BufferedReader(new FileReader(file, Charset.forName("UTF-8")));
            StringBuilder sbu = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                sbu.append(str);
            }
            return sbu.toString();
        }catch (Exception e){
            return null;
        }
    }

}
