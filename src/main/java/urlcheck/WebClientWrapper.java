/*     */package urlcheck;

/*     */
/*     */import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.ScriptException;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */public class WebClientWrapper
/*     */{

    /* 33 */private static ConcurrentHashMap<String, CookieManager> staticPool         = new ConcurrentHashMap();
    /* 34 */private static String                                   DEFAULT_GROUP_NAME = "_default_group_name";

    /*     */
    /*     */public static WebClient getWebClient(String groupName) {
        /* 37 */if (StringUtils.isEmpty(groupName))
        /* 38 */groupName = DEFAULT_GROUP_NAME;
        /* 39 */CookieManager cookieManager = (CookieManager) staticPool.get(groupName);
        /* 40 */WebClient client = getDefaultWebCleint();
        /* 41 */if (null != cookieManager) {
            /* 42 */client.setCookieManager(cookieManager);
            /*     */}
        /* 44 */return client;
        /*     */}

    /*     */
    /*     */public static void initPool(List<UrlCheckGroup> urlCheckGroupList)
    /*     */throws IOException
    /*     */{
        /* 53 */for (UrlCheckGroup urlCheckGroup : urlCheckGroupList) {
            /* 54 */WebClient client = getDefaultWebCleint();
            /* 55 */String name = urlCheckGroup.getName();
            /*     */try {
                /* 57 */LoginTask loginTask = urlCheckGroup.getLoginTask();
                /* 58 */if (null != loginTask) {
                    /* 59 */if (loginTask.getIntlLogin().booleanValue()) {
                        /* 60 */loginIntl(client, loginTask);
                        /*     */} else {
                        /* 62 */HtmlPage page = (HtmlPage) client.getPage(loginTask.getQueryUrl());
                        /* 63 */for (HttpFormParam param : loginTask.getParams()) {
                            /* 64 */if (StringUtils.isNotEmpty(param.getId())) {
                                /* 65 */System.out.println(page.asXml());
                                /* 66 */page.getElementById(param.getId()).type(param.getValue());
                                /* 67 */} else if (StringUtils.isNotEmpty(param.getName())) {
                                /* 68 */page.getElementByName(param.getName()).type(param.getValue());
                                /*     */}
                            /*     */}
                        /* 71 */HttpFormSubmit submit = loginTask.getSubmit();
                        /* 72 */HtmlPage submitPage = null;
                        /* 73 */if (null != submit) {
                            /* 74 */if (StringUtils.isNotEmpty(submit.getId())) {
                                /* 75 */submitPage = (HtmlPage) page.getElementById(submit.getId()).click();
                                /* 76 */} else if (StringUtils.isNotEmpty(submit.getName())) {
                                /* 77 */submitPage = (HtmlPage) page.getElementByName(submit.getName()).click();
                                /*     */}
                            /* 79 */else if (StringUtils.isNotEmpty(submit.getTagName())) {
                                /* 80 */DomNodeList elements = page.getElementsByTagName(submit.getTagName());
                                /* 81 */for (HtmlElement e : elements) {
                                    /* 82 */if ((e.getAttribute("type").equalsIgnoreCase(submit.getType()))
                                                && (e.getTextContent().equalsIgnoreCase(submit.getText())))
                                    /*     */{
                                        /* 84 */submitPage = (HtmlPage) e.click();
                                        /* 85 */break;
                                        /*     */}
                                    /*     */}
                                /*     */
                                /*     */}
                            /*     */
                            /*     */}
                        /*     */
                        /* 93 */if (StringUtils.isNotEmpty(loginTask.getCheckText())) {
                            /* 94 */if (null != submitPage) {
                                /* 95 */if (submitPage.asText().contains(loginTask.getCheckText())) {
                                    /* 96 */System.out.println("Group[" + name + "] login success!");
                                    /*     */} else {
                                    /* 98 */System.err.println(submitPage.asXml());
                                    /* 99 */throw new RuntimeException("Group[" + name + "] login failed!");
                                    /*     */}
                                /*     */}
                            /* 102 */else throw new RuntimeException("Group[" + name
                                                                     + "] login failed! The submit page is null!");
                            /*     */}
                        /*     */}
                    /*     */
                    /*     */}
                /*     */
                /* 108 */staticPool.put(name, client.getCookieManager());
                /*     */} catch (Throwable e) {
                /* 110 */e.printStackTrace();
                /* 111 */throw new RuntimeException("Group[" + name + "] login failed! ");
                /*     */}
            /*     */}
        /*     */}

    /*     */
    /*     */private static void loginIntl(WebClient client, LoginTask loginTask)
    /*     */throws FailingHttpStatusCodeException, MalformedURLException, IOException, ScriptException
    /*     */{
        /* 130 */String loginUrl = loginTask.getQueryUrl();
        /* 131 */List params = new ArrayList();
        /* 132 */for (HttpFormParam param : loginTask.getParams()) {
            /* 133 */String name = param.getId();
            /* 134 */if (StringUtils.isEmpty(param.getId())) {
                /* 135 */name = param.getName();
                /*     */}
            /* 137 */params.add(name + "=" + param.getValue());
            /*     */}
        /* 139 */loginUrl = loginUrl + "?" + StringUtils.join(params, "&");
        /* 140 */Page page = client.getPage(loginUrl);
        /* 141 */String result = page.getWebResponse().getContentAsString();
        /* 142 */result = StringUtils.substringAfter(result, "=");
        /* 143 */JSONObject object = JSON.parseObject(result);
        /* 144 */JSONArray jsonArray = object.getJSONArray("xlogin_urls");
        /* 145 */if (null == jsonArray) {
            /* 146 */throw new RuntimeException("login failed!");
            /*     */}
        /* 148 */for (Iterator i$ = jsonArray.iterator(); i$.hasNext();) {
            Object o = i$.next();
            /* 149 */String xloginUrl = (String) o;
            /* 150 */client.getPage(xloginUrl);
            /*     */}
        /*     */}

    /*     */
    /*     */public static WebClient getDefaultWebCleint()
    /*     */{
        /* 161 */WebClient client = new WebClient(BrowserVersion.INTERNET_EXPLORER_8);
        /* 162 */HttpWebConnection httpwebconnection = getConnection(client);
        /* 163 */client.setRedirectEnabled(true);
        /* 164 */client.setWebConnection(httpwebconnection);
        /*     */
        /* 166 */client.setCssEnabled(false);
        /*     */
        /* 168 */client.setJavaScriptEnabled(false);
        /* 169 */client.addRequestHeader("Accept-Language", "zh-cn,zh");
        /* 170 */client.addRequestHeader("Accept-Charset", "GB2312,utf-8");
        /* 171 */client.addRequestHeader("Keep-Alive", "115");
        /* 172 */client.addRequestHeader("Connection", "keep-alive");
        /* 173 */client.addRequestHeader("Accept-Encoding", "gzip,deflate");
        /*     */
        /* 175 */return client;
        /*     */}

    /*     */
    /*     */private static HttpWebConnection getConnection(WebClient client)
    /*     */{
        /* 185 */HttpWebConnection httpwebconnection = new HttpWebConnection(client);
        /*     */try {
            /* 187 */httpwebconnection.setUseInsecureSSL(true);
            /* 188 */client.setUseInsecureSSL(true);
            /*     */} catch (GeneralSecurityException e) {
            /* 190 */e.printStackTrace();
            /*     */}
        /* 192 */return httpwebconnection;
        /*     */}
    /*     */
}
