/*     */package runner;

/*     */
/*     */import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import result.CheckResult;
import result.ResultCollector;
import urlcheck.TaskConfig;
import urlcheck.UrlTask;
import urlcheck.WebClientWrapper;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;

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
/*     */
/*     */
/*     */
/*     */
/*     */public class UrlHtmlUnitCheckTask
/*     */implements Runnable
/*     */{

    /* 35 */private static final Log logger    = LogFactory.getLog(UrlHtmlUnitCheckTask.class);
    /*     */
    /* 37 */private static final Log urlLogger = LogFactory.getLog(UrlCheckLog.class);
    /*     */private UrlTask             urlTask;
    /*     */private HtmlUnitRunner      runner;
    /*     */private ResultCollector     resultCollector;

    /*     */
    /*     */public void setResultCollector(ResultCollector resultCollector)
    /*     */{
        /* 43 */this.resultCollector = resultCollector;
        /*     */}

    /*     */
    /*     */public void setRunner(HtmlUnitRunner runner) {
        /* 47 */this.runner = runner;
        /*     */}

    /*     */
    /*     */public void setUrlTask(UrlTask urlTask) {
        /* 51 */this.urlTask = urlTask;
        /*     */}

    /*     */
    /*     */public void run()
    /*     */{
        /* 56 */List urlvaluelist = new ArrayList();
        /*     */
        /* 58 */String url = this.urlTask.getSrc();
        /*     */
        /* 68 */if (this.urlTask.getLevel() > this.urlTask.getMaxLevel()) {
            /* 69 */urlLogger.info("[Skiped][Level]" + url);
            /* 70 */this.runner.delWaitTask();
            /* 71 */return;
            /*     */}
        /*     */
        /* 76 */boolean check = false;
        /* 77 */for (String domain : TaskConfig.getCheckDomains()) {
            /* 78 */if (StringUtils.contains(url, domain)) {
                /* 79 */check = true;
                /* 80 */break;
                /*     */}
            /*     */}
        /* 83 */if (!check) {
            /* 84 */urlLogger.info("[Skiped][Domain]" + url);
            /* 85 */this.runner.delWaitTask();
            /* 86 */return;
            /*     */}
        /*     */
        /* 90 */urlvaluelist.add(url);
        /* 91 */String groupName = this.urlTask.getGroupName();
        /* 92 */CheckResult checkResult = new CheckResult();
        /* 93 */checkResult.setUrlTask(this.urlTask);
        /* 94 */WebClient client = WebClientWrapper.getWebClient(groupName);
        /*     */try {
            /* 96 */Page page = client.getPage(this.urlTask.getSrc());
            /*     */
            /* 98 */WebResponse webResponse = page.getWebResponse();
            /* 99 */long loadTime = webResponse.getLoadTime();
            /* 100 */int statusCode = webResponse.getStatusCode();
            /* 101 */String msg = webResponse.getStatusMessage();
            /* 102 */checkResult.setLoadTime(loadTime);
            /* 103 */checkResult.setMsg(msg);
            /*     */
            /* 105 */checkResult.setStatusCode(statusCode);
            /* 106 */if (statusCode == 200) {
                /* 107 */if (loadTime > TaskConfig.getMaxLoadTime()) {
                    /* 108 */checkResult.setSuccess(false);
                    /* 109 */urlLogger.info("[Fail][TimeOut]" + url);
                    /* 110 */checkResult.setLoadFail(true);
                    /*     */} else {
                    /* 112 */urlLogger.info("[Success]" + url);
                    /* 113 */checkResult.setSuccess(true);
                    /* 114 */if (webResponse.getRequestUrl().getPath().contains("shtml/static/wrongpage.html")) {
                        /* 115 */checkResult.setMsg("被跳转到统一错误页面!");
                        /*     */}
                    /*     */
                    /*     */}
                /*     */
                /* 120 */if ((this.urlTask.isCheckChildren()) &&
                /* 121 */((page instanceof HtmlPage))) {
                    /* 122 */this.runner.addTasks(getFrameUrlTaskList((HtmlPage) page, this.urlTask));
                    /*     */}
                /*     */
                /* 127 */if ((this.urlTask.getLevel() < this.urlTask.getMaxLevel()) && (this.urlTask.isCheckChildren()))
                /*     */{
                    /* 129 */if ((page instanceof HtmlPage)) {
                        /* 130 */this.runner.addTasks(getLinkUrlTaskList((HtmlPage) page, this.urlTask));
                        /*     */
                        /* 132 */if (this.urlTask.getCheckCss().booleanValue()) {
                            /* 133 */this.runner.addTasks(getCssUrlTaskList((HtmlPage) page, this.urlTask));
                            /*     */}
                        /*     */
                        /* 136 */if (this.urlTask.getCheckImg().booleanValue()) {
                            /* 137 */this.runner.addTasks(getImgUrlTaskList((HtmlPage) page, this.urlTask));
                            /*     */}
                        /*     */
                        /* 140 */if (this.urlTask.getCheckImg().booleanValue()) {
                            /* 141 */this.runner.addTasks(getJsUrlTaskList((HtmlPage) page, this.urlTask));
                            /*     */}
                        /*     */}
                    /*     */}
                /*     */}
            /*     */else
            /*     */{
                /* 148 */urlLogger.info("[Fail]" + url);
                /* 149 */checkResult.setLoadFail(false);
                /* 150 */checkResult.setSuccess(false);
                /*     */}
            /*     */} catch (FailingHttpStatusCodeException e) {
            /* 153 */urlLogger.info("[Fail]" + url);
            /* 154 */int statusCode = e.getStatusCode();
            /* 155 */checkResult.setLoadFail(false);
            /* 156 */checkResult.setSuccess(false);
            /* 157 */checkResult.setStatusCode(statusCode);
            /* 158 */checkResult.setMsg(e.getStatusMessage());
            /*     */} catch (Throwable e) {
            /* 160 */urlLogger.info("[Fail]" + url);
            /* 161 */logger.error("Error get the url：" + this.urlTask.getSrc(), e);
            /* 162 */checkResult.setLoadFail(false);
            /* 163 */checkResult.setSuccess(false);
            /* 164 */checkResult.setStatusCode(-1);
            /* 165 */StringWriter sw = new StringWriter();
            /* 166 */PrintWriter pw = new PrintWriter(sw);
            /* 167 */e.printStackTrace(pw);
            /* 168 */checkResult.setMsg(e.getMessage() + "\n" + sw.toString());
            /*     */} finally {
            /* 170 */this.runner.delWaitTask();
            /* 171 */this.resultCollector.addResult(checkResult);
            /*     */
            /* 173 */client.closeAllWindows();
            /*     */}
        /*     */}

    /*     */
    /*     */private List<UrlTask> getJsUrlTaskList(HtmlPage page, UrlTask parentUrlTask)
    /*     */{
        /* 180 */List taskList = new ArrayList();
        /* 181 */DomNodeList elements = page.getElementsByTagName("script");
        /* 182 */for (HtmlElement element : elements) {
            /* 183 */UrlTask task = new UrlTask();
            /* 184 */task.setCheckChildren(false);
            /* 185 */task.setLevel(parentUrlTask.getLevel() + 1);
            /* 186 */task.setParentUrl(parentUrlTask.getSrc());
            /* 187 */task.setGroupName(parentUrlTask.getGroupName());
            /* 188 */task.setMaxLevel(parentUrlTask.getMaxLevel());
            /* 189 */if (null != parentUrlTask.getTopParentUrl())
            /* 190 */task.setTopParentUrl(parentUrlTask.getTopParentUrl());
            /*     */else
            /* 192 */task.setTopParentUrl(parentUrlTask.getSrc());
            /* 193 */HtmlScript script = (HtmlScript) element;
            /* 194 */String src = script.getSrcAttribute();
            /*     */try {
                /* 196 */task.setSrc(page.getFullyQualifiedUrl(src).toString());
                /*     */} catch (MalformedURLException e) {
                /* 198 */e.printStackTrace();
                /*     */}
            /* 200 */taskList.add(task);
            /*     */}
        /* 202 */return taskList;
        /*     */}

    /*     */
    /*     */private List<UrlTask> getCssUrlTaskList(HtmlPage page, UrlTask parentUrlTask) {
        /* 206 */List taskList = new ArrayList();
        /* 207 */DomNodeList elements = page.getElementsByTagName("link");
        /* 208 */for (HtmlElement element : elements) {
            /* 209 */UrlTask task = new UrlTask();
            /* 210 */task.setLevel(parentUrlTask.getLevel() + 1);
            /* 211 */task.setParentUrl(parentUrlTask.getSrc());
            /* 212 */task.setGroupName(parentUrlTask.getGroupName());
            /* 213 */task.setCheckChildren(false);
            /* 214 */task.setMaxLevel(parentUrlTask.getMaxLevel());
            /* 215 */if (null != parentUrlTask.getTopParentUrl())
            /* 216 */task.setTopParentUrl(parentUrlTask.getTopParentUrl());
            /*     */else
            /* 218 */task.setTopParentUrl(parentUrlTask.getSrc());
            /* 219 */HtmlLink link = (HtmlLink) element;
            /* 220 */String src = link.getHrefAttribute();
            /*     */try {
                /* 222 */task.setSrc(page.getFullyQualifiedUrl(src).toString());
                /*     */} catch (MalformedURLException e) {
                /* 224 */e.printStackTrace();
                /*     */}
            /* 226 */taskList.add(task);
            /*     */}
        /* 228 */return taskList;
        /*     */}

    /*     */
    /*     */private List<UrlTask> getImgUrlTaskList(HtmlPage page, UrlTask parentUrlTask) {
        /* 232 */List taskList = new ArrayList();
        /* 233 */DomNodeList elements = page.getElementsByTagName("img");
        /* 234 */for (HtmlElement element : elements) {
            /* 235 */UrlTask task = new UrlTask();
            /* 236 */task.setLevel(parentUrlTask.getLevel() + 1);
            /* 237 */task.setParentUrl(parentUrlTask.getSrc());
            /* 238 */task.setCheckChildren(false);
            /* 239 */task.setGroupName(parentUrlTask.getGroupName());
            /* 240 */task.setMaxLevel(parentUrlTask.getMaxLevel());
            /* 241 */if (null != parentUrlTask.getTopParentUrl())
            /* 242 */task.setTopParentUrl(parentUrlTask.getTopParentUrl());
            /*     */else
            /* 244 */task.setTopParentUrl(parentUrlTask.getSrc());
            /* 245 */HtmlImage img = (HtmlImage) element;
            /* 246 */String src = img.getSrcAttribute();
            /*     */try {
                /* 248 */task.setSrc(page.getFullyQualifiedUrl(src).toString());
                /*     */} catch (MalformedURLException e) {
                /* 250 */e.printStackTrace();
                /*     */}
            /* 252 */taskList.add(task);
            /*     */}
        /* 254 */return taskList;
        /*     */}

    /*     */
    /*     */private List<UrlTask> getLinkUrlTaskList(HtmlPage page, UrlTask parentUrlTask)
    /*     */{
        /* 259 */List taskList = new ArrayList();
        /* 260 */List elements = page.getAnchors();
        /* 261 */for (HtmlAnchor element : elements) {
            /* 262 */UrlTask task = new UrlTask();
            /* 263 */task.setLevel(parentUrlTask.getLevel() + 1);
            /* 264 */task.setParentUrl(parentUrlTask.getSrc());
            /* 265 */task.setCheckChildren(true);
            /* 266 */task.setMaxLevel(parentUrlTask.getMaxLevel());
            /* 267 */task.setCheckCss(parentUrlTask.getCheckCss());
            /* 268 */task.setCheckImg(parentUrlTask.getCheckImg());
            /* 269 */task.setCheckJs(parentUrlTask.getCheckJs());
            /* 270 */if (null != parentUrlTask.getTopParentUrl())
            /* 271 */task.setTopParentUrl(parentUrlTask.getTopParentUrl());
            /*     */else
            /* 273 */task.setTopParentUrl(parentUrlTask.getSrc());
            /* 274 */String src = element.getHrefAttribute();
            /*     */try {
                /* 276 */task.setSrc(page.getFullyQualifiedUrl(src).toString());
                /*     */} catch (MalformedURLException e) {
                /* 278 */e.printStackTrace();
                /*     */}
            /* 280 */taskList.add(task);
            /*     */}
        /* 282 */return taskList;
        /*     */}

    /*     */
    /*     */private List<UrlTask> getFrameUrlTaskList(HtmlPage page, UrlTask parentUrlTask)
    /*     */{
        /* 287 */List taskList = new ArrayList();
        /* 288 */DomNodeList elements = page.getElementsByTagName("frame");
        /* 289 */DomNodeList iframeelements = page.getElementsByTagName("iframe");
        /*     */
        /* 292 */for (HtmlElement element : elements) {
            /* 293 */UrlTask task = new UrlTask();
            /* 294 */task.setLevel(parentUrlTask.getLevel());
            /* 295 */task.setParentUrl(parentUrlTask.getSrc());
            /* 296 */task.setCheckChildren(true);
            /* 297 */task.setGroupName(parentUrlTask.getGroupName());
            /* 298 */task.setMaxLevel(parentUrlTask.getMaxLevel());
            /* 299 */task.setCheckCss(parentUrlTask.getCheckCss());
            /* 300 */task.setCheckImg(parentUrlTask.getCheckImg());
            /* 301 */task.setCheckJs(parentUrlTask.getCheckJs());
            /* 302 */if (null != parentUrlTask.getTopParentUrl())
            /* 303 */task.setTopParentUrl(parentUrlTask.getTopParentUrl());
            /*     */else
            /* 305 */task.setTopParentUrl(parentUrlTask.getSrc());
            /* 306 */HtmlFrame frame = (HtmlFrame) element;
            /* 307 */String src = frame.getSrcAttribute();
            /*     */try {
                /* 309 */task.setSrc(page.getFullyQualifiedUrl(src).toString());
                /*     */} catch (MalformedURLException e) {
                /* 311 */e.printStackTrace();
                /*     */}
            /* 313 */taskList.add(task);
            /*     */}
        /*     */
        /* 316 */for (HtmlElement element : iframeelements) {
            /* 317 */UrlTask task = new UrlTask();
            /* 318 */task.setLevel(parentUrlTask.getLevel());
            /* 319 */task.setParentUrl(parentUrlTask.getSrc());
            /* 320 */task.setCheckChildren(true);
            /* 321 */task.setGroupName(parentUrlTask.getGroupName());
            /* 322 */task.setMaxLevel(parentUrlTask.getMaxLevel());
            /* 323 */task.setCheckCss(parentUrlTask.getCheckCss());
            /* 324 */task.setCheckImg(parentUrlTask.getCheckImg());
            /* 325 */task.setCheckJs(parentUrlTask.getCheckJs());
            /* 326 */if (null != parentUrlTask.getTopParentUrl())
            /* 327 */task.setTopParentUrl(parentUrlTask.getTopParentUrl());
            /*     */else
            /* 329 */task.setTopParentUrl(parentUrlTask.getSrc());
            /* 330 */HtmlInlineFrame frame = (HtmlInlineFrame) element;
            /* 331 */String src = frame.getSrcAttribute();
            /*     */try {
                /* 333 */task.setSrc(page.getFullyQualifiedUrl(src).toString());
                /*     */} catch (MalformedURLException e) {
                /* 335 */e.printStackTrace();
                /*     */}
            /* 337 */taskList.add(task);
            /*     */}
        /*     */
        /* 340 */return taskList;
        /*     */}
    /*     */
}
