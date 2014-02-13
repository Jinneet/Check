/*     */package runner;

/*     */
/*     */import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import result.ResultCollector;
import result.ResultCreator;
import urlcheck.TaskConfig;
import urlcheck.UrlTask;

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
/*     */public class HttpUnitRunner
/*     */{

    /*     */private final ExecutorService exec;
    /*     */private TaskStatusPool        taskStatusPool;
    /* 57 */private long               lastTaskTime    = System.currentTimeMillis();
    /* 58 */private final long         MAX_IDLE_TIME   = 15000L;
    /*     */private ResultCollector       resultCollector;
    /*     */private HttpClient            client;
    /*     */private static final String   CONTENT_CHARSET = "UTF-8";

    /*     */
    /*     */public HttpUnitRunner()
    /*     */{
        /* 67 */this.exec = Executors.newFixedThreadPool(TaskConfig.getThreadCount());
        /* 68 */this.resultCollector = new ResultCollector();
        /* 69 */this.taskStatusPool = new TaskStatusPool();
        /*     */
        /* 71 */HttpParams params = new BasicHttpParams();
        /* 72 */ConnManagerParams.setMaxTotalConnections(params, TaskConfig.getThreadCount());
        /* 73 */HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        /*     */
        /* 75 */SchemeRegistry schemeRegistry = new SchemeRegistry();
        /* 76 */schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        /* 77 */schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        /* 78 */ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(params,
                                                                                                  schemeRegistry);
        /*     */
        /* 80 */this.client = new DefaultHttpClient(clientConnectionManager, params);
        /* 81 */this.client.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        /* 82 */this.client.getParams().setParameter("http.connection.timeout", Integer.valueOf(10000));
        /* 83 */this.client.getParams().setParameter("http.protocol.handle-redirects", Boolean.valueOf(false));
        /* 84 */this.client.getParams().setParameter("http.useragent",
                                                     "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        /*     */
        /* 88 */HttpClientParams.setCookiePolicy(this.client.getParams(), "compatibility");
        /*     */}

    /*     */
    /*     */public synchronized void addTasks(List<UrlTask> urlTasks)
    /*     */{
        /* 93 */for (UrlTask urlTask : urlTasks)
            /* 94 */addTask(urlTask);
        /*     */}

    /*     */
    /*     */private void addTask(UrlTask urlTask)
    /*     */{
        /* 99 */updateLastTaskTime();
        /* 100 */if (this.taskStatusPool.addUrlTask(urlTask))
        /*     */{
            /* 102 */UrlCheckTask urlCheckTask = new UrlCheckTask(this.client);
            /* 103 */urlCheckTask.setResultCollector(this.resultCollector);
            /* 104 */urlCheckTask.setUrlCheckRunner(this);
            /* 105 */urlCheckTask.setUrlTask(urlTask);
            /* 106 */addTask(urlCheckTask);
            /*     */}
        /*     */}

    /*     */
    /*     */private synchronized void updateLastTaskTime()
    /*     */{
        /* 112 */this.lastTaskTime = System.currentTimeMillis();
        /*     */}

    /*     */
    /*     */private synchronized long getLastTaskTime() {
        /* 116 */return this.lastTaskTime;
        /*     */}

    /*     */
    /*     */private void addTask(UrlCheckTask urlCheckTask)
    /*     */{
        /* 125 */this.exec.execute(urlCheckTask);
        /*     */}

    /*     */
    /*     */private void shutdown()
    /*     */{
        /* 132 */this.exec.shutdown();
        /*     */}

    /*     */
    /*     */public void run()
    /*     */{
        /* 139 */List urls = TaskConfig.getCheckList();
        /* 140 */this.resultCollector.setReportPath(TaskConfig.getReportPath());
        /* 141 */this.resultCollector.setRunStartTime(System.currentTimeMillis());
        /* 142 */for (String url : urls) {
            /* 143 */UrlTask task = new UrlTask();
            /* 144 */task.setLevel(1);
            /* 145 */task.setCheckChildren(true);
            /* 146 */task.setParentUrl(null);
            /* 147 */task.setTopParentUrl(null);
            /* 148 */task.setSrc(url);
            /* 149 */addTask(task);
            /*     */}
        /*     */
        /* 153 */MonitorThread monitorThread = new MonitorThread();
        /* 154 */monitorThread.setDaemon(true);
        /* 155 */monitorThread.start();
        /*     */
        /* 158 */IdleConnectionMonitorThread idleThread = new IdleConnectionMonitorThread(
                                                                                          this.client.getConnectionManager());
        /*     */
        /* 160 */idleThread.setDaemon(true);
        /* 161 */idleThread.start();
        /*     */while (true)
        /*     */{
            /* 165 */if (this.exec.isTerminated()) {
                /* 166 */System.out.println("Url Check Task is completed!");
                /* 167 */this.resultCollector.setRunStopTime(System.currentTimeMillis());
                /* 168 */ResultCreator.generatorReport(this.resultCollector);
                /* 169 */break;
                /*     */}
            /*     */try {
                /* 172 */TimeUnit.MILLISECONDS.sleep(1000L);
                /*     */}
            /*     */catch (InterruptedException e)
            /*     */{
                /*     */}
            /*     */}
        /*     */}

    /*     */
    /*     */public static class IdleConnectionMonitorThread extends Thread
    /*     */{

        /*     */private final ClientConnectionManager connMgr;
        /*     */private volatile boolean              shutdown;

        /*     */
        /*     */public IdleConnectionMonitorThread(ClientConnectionManager connMgr)
        /*     */{
            /* 214 */this.connMgr = connMgr;
            /*     */}

        /*     */
        /*     */public void run()
        /*     */{
            /*     */try {
                /* 220 */while (!this.shutdown)
                    /* 221 */synchronized (this) {
                        /* 222 */wait(5000L);
                        /*     */
                        /* 224 */this.connMgr.closeExpiredConnections();
                        /*     */
                        /* 227 */this.connMgr.closeIdleConnections(30L, TimeUnit.SECONDS);
                        /*     */}
                /*     */}
            /*     */catch (InterruptedException ex)
            /*     */{
                /*     */}
            /*     */}

        /*     */
        /*     */public void shutdown() {
            /* 236 */this.shutdown = true;
            /* 237 */synchronized (this) {
                /* 238 */notifyAll();
                /*     */}
            /*     */}
        /*     */
    }

    /*     */
    /*     */class MonitorThread extends Thread
    /*     */{

        /*     */MonitorThread()
        /*     */{
            /*     */}

        /*     */
        /*     */public void run()
        /*     */{
            /*     */while (true)
            /*     */{
                /* 188 */long currentTime = System.currentTimeMillis();
                /* 189 */if (currentTime - HttpUnitRunner.this.getLastTaskTime() > 15000L) {
                    /* 190 */HttpUnitRunner.this.shutdown();
                    /* 191 */break;
                    /*     */}
                /*     */try {
                    /* 194 */TimeUnit.MILLISECONDS.sleep(1000L);
                    /*     */}
                /*     */catch (InterruptedException e)
                /*     */{
                    /*     */}
                /*     */}
            /*     */}
        /*     */
    }
    /*     */
}
