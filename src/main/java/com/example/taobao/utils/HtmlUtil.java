package com.example.taobao.utils;

import com.example.taobao.entity.FileInfo;
import com.example.taobao.entity.FilePage;
import com.example.taobao.entity.ResourceInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @创建人: lzh
 * @创建时间: 2022/2/16
 * @描述:
 */
public class HtmlUtil {

    /**
     * 获取文件名和文件id
     * @param html
     * @return
     */
    public static FilePage getFile(String html,String path){
        Document document = Jsoup.parse(html);
        FilePage filePage = new FilePage();
        filePage.setHashNext(true);
        List<FileInfo> fileInfos = new ArrayList<>();
        try {
            FileInfo fileInfo = null;
            Element ele = null;
            for (Element element : document.select(".list-cont .clearfix")) {
                fileInfo = new FileInfo();
                ele = element.selectFirst("a[data-softid]");
                //文件名
                fileInfo.setFileName(ele.html());
                //文件id
                fileInfo.setSoftid(ele.attr("data-softid"));
                //文件更新时间
                fileInfo.setDate(element.selectFirst("em").html());
                //点数
                if (element.selectFirst(".point").html().equals("免费")){
                    fileInfo.setFree(true);
                }else {
                    fileInfo.setFree(false);
                }
                //路径
                fileInfo.setDir(path);
                //下载状态
                fileInfo.setStatus("未下载");
                //不是第三方 file-label file-label-2
                if (element.selectFirst(".file-label-2") == null){
                    //只添加今天的文件
                    if (fileInfo.getDate().hashCode() == getToday()) {
                        fileInfos.add(fileInfo);
                    } else {
                        //是否有下一页 这一页出现非今天的文件说明不需要下一页
                        filePage.setHashNext(false);
                    }
                }

            }
            filePage.setFileInfos(fileInfos);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return filePage;
    }

    /**
     * 获取下载链接
     * @param html
     * @return
     */
    public static String getDownUrl(String html){
        Document document = Jsoup.parse(html);
        Element element = document.selectFirst(".suc");
        if (element != null){
            return element.html();
        }
        return null;
    }

    /**
     * vip预览链接转下载链接
     * @param url
     * @return
     */
    public static String getVipUrl(String url){
        long time = System.currentTimeMillis();
        String down = "https://downloadweb.zxxk.com/FileDownLoad/GetFileAddress?callback=jQuery110208286841578081416_1646297958094&";
        url = url.replaceAll("&amp;","&");
        String param = url.substring(url.indexOf("?")+1, url.length())+"&_="+time;
        return down+param;
    }

    /**
     * 获取今天时间的字符串
     * @return
     */
    public static int getToday(){
        Calendar ca1 = Calendar.getInstance();
        return DateFormatUtils.format(ca1,"yyyy/MM/dd").hashCode();
    }



    public static String getHeaderFileName(String header){
        header = header.substring(header.lastIndexOf("''"), header.length());
        return header.replaceAll("''","");
    }


//    public static void main(String[] args) throws Exception {
//        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\Administrator\\Desktop\\html.txt"));
//        StringBuilder stringBuilder = new StringBuilder();
//        String line = null;
//        while ((line = bufferedReader.readLine())!=null)stringBuilder.append(line);
//        Document document = Jsoup.parse(stringBuilder.toString());
//        Element ele = null;
//        for (Element element : document.select(".list-cont .clearfix")) {
//            ele = element.selectFirst("a[data-softid]");
//            //文件名
//            System.out.println(ele.html());
//            //文件id
//            System.out.println(ele.attr("data-softid"));
//            //时间 em
//            System.out.println(element.selectFirst("em").html());
//        }
//    }

//    public FilePage getFile(String html){
//        Document document = Jsoup.parse(html);
//        FilePage filePage = new FilePage();
//        List<FileInfo> fileInfos = new ArrayList<>();
//        FileInfo fileInfo = null;
//        for (Element element : document.select(".list-cont .clearfix")) {
//            fileInfo = new FileInfo();
//            fileInfo.setFileName(element.html());
//            fileInfo.setSoftid(element.attr("data-softid"));
//            fileInfos.add(fileInfo);
//        }
//        //是否有下一页
//        filePage.setHashNext(false);
//        filePage.setFileInfos(fileInfos);
//        return filePage;
//    }

}
