/*     */package runner;

/*     */
/*     */import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import result.CheckResult;
import result.ResultCollector;
import urlcheck.TaskConfig;
import urlcheck.UrlTask;
import utils.HtmlParser;
import utils.HtmlResponse;
import utils.HttpUtils;

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
/*     */public class UrlCheckTask
/*     */implements Runnable
/*     */{

    /* 39 */private static final Log logger          = LogFactory.getLog(UrlCheckTask.class);
    /*     */private UrlTask             urlTask;
    /*     */private HttpUnitRunner      urlCheckRunner;
    /*     */private ResultCollector     resultCollector;
    /*     */private HttpClient          client;
    /* 44 */String                   CONTENT_CHARSET = "UTF-8";

    /*     */
    /*     */public UrlCheckTask(HttpClient client){
        /* 47 */this.client = client;
        /*     */}

    /*     */
    /*     */public void setResultCollector(ResultCollector resultCollector) {
        /* 51 */this.resultCollector = resultCollector;
        /*     */}

    /*     */
    /*     */public void setUrlCheckRunner(HttpUnitRunner urlCheckRunner) {
        /* 55 */this.urlCheckRunner = urlCheckRunner;
        /*     */}

    /*     */
    /*     */public void setUrlTask(UrlTask urlTask) {
        /* 59 */this.urlTask = urlTask;
        /*     */}

    /*     */
    /*     */public void run()
    /*     */{
        /* 68 */if (this.urlTask.getLevel() > TaskConfig.getMaxLevel()) {
            /* 69 */return;
            /*     */}
        /* 71 */CheckResult checkResult = new CheckResult();
        /* 72 */checkResult.setUrlTask(this.urlTask);
        /*     */
        /* 74 */HttpGet httpget = new HttpGet(this.urlTask.getSrc());
        /*     */try
        /*     */{
            /* 77 */long start = System.currentTimeMillis();
            /* 78 */HtmlResponse response = HttpUtils.getResponse(this.client, httpget);
            /* 79 */long stop = System.currentTimeMillis();
            /*     */
            /* 81 */checkResult.setLoadTime(stop - start);
            /* 82 */StatusLine status = response.getHttpResponse().getStatusLine();
            /*     */
            /* 84 */checkResult.setStatusCode(status.getStatusCode());
            /* 85 */checkResult.setMsg(status.getReasonPhrase());
            /* 86 */if (status.getStatusCode() == 200) {
                /* 87 */checkResult.setSuccess(true);
                /*     */
                /* 94 */if ((this.urlTask.getLevel() < TaskConfig.getMaxLevel()) && (this.urlTask.isCheckChildren())) {
                    /* 95 */HtmlParser parser = new HtmlParser(response, this.urlTask);
                    /*     */
                    /* 98 */this.urlCheckRunner.addTasks(parser.getFrameList());
                    /* 99 */this.urlCheckRunner.addTasks(parser.getLinkList());
                    /* 100 */this.urlCheckRunner.addTasks(parser.getImageList());
                    /* 101 */this.urlCheckRunner.addTasks(parser.getCssList());
                    /* 102 */this.urlCheckRunner.addTasks(parser.getJsList());
                    /*     */}
                /*     */} else {
                /* 105 */checkResult.setSuccess(false);
                /*     */}
            /*     */}
        /*     */catch (Exception e) {
            /* 109 */logger.error("Error get the url：" + this.urlTask.getSrc(), e);
            /* 110 */checkResult.setSuccess(false);
            /* 111 */checkResult.setStatusCode(-1);
            /* 112 */checkResult.setMsg(e.getMessage());
            /*     */} finally {
            /* 114 */httpget.abort();
            /*     */}
        /*     */
        /* 117 */this.resultCollector.addResult(checkResult);
        /*     */}
    /*     */
}
