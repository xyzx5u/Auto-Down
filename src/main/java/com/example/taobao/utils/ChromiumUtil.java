package com.example.taobao.utils;

import com.example.taobao.entity.LoginInfo;
import com.ruiyun.jvppeteer.core.Puppeteer;
import com.ruiyun.jvppeteer.core.browser.Browser;
import com.ruiyun.jvppeteer.core.browser.BrowserFetcher;
import com.ruiyun.jvppeteer.core.page.Page;
import com.ruiyun.jvppeteer.core.page.Request;
import com.ruiyun.jvppeteer.core.page.Response;
import com.ruiyun.jvppeteer.options.LaunchOptions;
import com.ruiyun.jvppeteer.options.LaunchOptionsBuilder;
import com.ruiyun.jvppeteer.options.PageNavigateOptions;
import com.ruiyun.jvppeteer.options.Viewport;
import com.ruiyun.jvppeteer.protocol.network.Cookie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @创建人: lzh
 * @创建时间: 2022/3/7
 * @描述:
 */
public class ChromiumUtil {

    public static LoginInfo openChrome(){
        try {
            //自动下载
            BrowserFetcher.downloadIfNotExist(null);

            ArrayList<String> argList = new ArrayList<>();
            String join = String.join(",", argList);
            System.out.println(join);
            LaunchOptions options = new LaunchOptionsBuilder()
                    .withArgs(argList)
                    .withHeadless(false)
                    .build();
            //
            //options.setExecutablePath("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe");
            //options.setExecutablePath("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
            //argList.add("--no-sandbox");
            argList.add("--start-maximized");//最大化
            //argList.add("--disable-setuid-sandbox");
            //argList.add("--disable-blink-features=AutomationControlled");//关闭调试的条子 不能删
            //启动
            List<String> sss = new ArrayList<>();
            sss.add("--enable-automation");
            options.setIgnoreDefaultArgs(sss);
            Browser browser = Puppeteer.launch(options);
            Page page = createPage("https://sso.zxxk.com/login",browser);
            page.setDefaultTimeout(1000*60*30);

            //等待登录完成获取cookie
            Response res = page.waitForResponse(response -> {
                return response.url().equals("https://www.zxxk.com/zxxk/usertuijian");
                //return response.url().equals("https://auth.zxxk.com/callback?dynamicService=true&curl=https://auth.zxxk.com/html/logincomplete.html");
            });
            //Thread.sleep(5000);
            String userId = "";
//            List<String> urls = new ArrayList<>();
//            urls.add("https://sso.zxxk.com");
//            urls.add("http://auth.zxxk.com");
//            urls.add("http://user.zxxk.com");
//            urls.add("https://www.zxxk.com");
//            urls.add("http://io.xkw.com");
//            urls.add("https://oms.xkw.com");
            List<Cookie> cookies2 = page.cookies();
            LoginInfo loginInfo = new LoginInfo();
            parseCookie(cookies2,loginInfo);
            return loginInfo;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 创建一个页面
     * @param url
     * @return
     */
    public static Page createPage(String url,Browser browser){
        Page pages = browser.newPage();
        Viewport viewport = new Viewport();
        viewport.setHeight(1080);
        viewport.setWidth(1920);
        pages.setViewport(viewport);
        pages.setDefaultNavigationTimeout(300 * 1000);//设置5分钟的超时时间

        pages.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
        pages.evaluateOnNewDocument("() =>{ Object.defineProperties(navigator,{ webdriver:{ get: () => false } }) }");
        pages.evaluateOnNewDocument("() =>{ window.navigator.chrome = { runtime: {},  }; }");
        pages.evaluateOnNewDocument("() =>{ Object.defineProperty(navigator, 'languages', { get: () => ['en-US', 'en'] }); }");
        pages.evaluateOnNewDocument("() =>{ Object.defineProperty(navigator, 'plugins', { get: () => [1, 2, 3, 4, 5,6], }); }");
        try {
            PageNavigateOptions navigateOptions = new PageNavigateOptions();

            //如果不设置 domcontentloaded 算页面导航完成的话，那么goTo方法会超时，因为图片请求被拦截了，页面不会达到loaded阶段
            navigateOptions.setWaitUntil(Collections.singletonList("domcontentloaded"));
            pages.goTo(url,navigateOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pages;
    }

    public static String parseCookie(List<Cookie> cookieList,LoginInfo loginInfo){
        StringBuilder cookies = new StringBuilder();
        for (Cookie cookie : cookieList) {
            if (cookie.getName().equals("xk.passport.uid")){
                loginInfo.setUserId(cookie.getValue());
            }
            cookies.append( cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        loginInfo.setCookies(cookies.toString());
        return cookies.toString();
    }

}
