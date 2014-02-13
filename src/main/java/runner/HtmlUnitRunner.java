/*    */package runner;

/*    */
/*    */import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import result.ResultCollector;
import result.ResultCreator;
import urlcheck.CheckUrlFileParser;
import urlcheck.TaskConfig;
import urlcheck.UrlCheckGroup;
import urlcheck.UrlCheckList;
import urlcheck.UrlTask;
import urlcheck.WebClientWrapper;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */public class HtmlUnitRunner
/*    */{

    /*    */private final ExecutorService exec;
    /*    */private TaskStatusPool        taskStatusPool;
    /*    */private ResultCollector       resultCollector;
    /* 31 */private AtomicLong         waiTask       = new AtomicLong();
    /* 32 */private final long         MAX_IDLE_TIME = 15000L;

    /*    */
    /*    */public void delWaitTask() {
        /* 35 */this.waiTask.decrementAndGet();
        /*    */}

    /*    */
    /*    */public void addwaitTask(long delta) {
        /* 39 */this.waiTask.addAndGet(delta);
        /*    */}

    /*    */
    /*    */public AtomicLong getWaitTask() {
        /* 43 */return this.waiTask;
        /*    */}

    /*    */
    /*    */public HtmlUnitRunner()
    /*    */{
        /* 50 */this.exec = Executors.newFixedThreadPool(TaskConfig.getThreadCount());
        /* 51 */this.resultCollector = new ResultCollector();
        /* 52 */this.taskStatusPool = new TaskStatusPool();
        /*    */}

    /*    */
    /*    */public void addTasks(List<UrlTask> urlTasks)
    /*    */{
        /* 57 */addwaitTask(urlTasks.size());
        /*    */
        /* 59 */for (UrlTask urlTask : urlTasks)
            /* 60 */addTask(urlTask, false);
        /*    */}

    /*    */
    /*    */private void addTask(UrlTask urlTask, boolean addTick)
    /*    */{
        /* 67 */if (addTick) {
            /* 68 */addwaitTask(1L);
            /*    */}
        /* 70 */if (this.taskStatusPool.addUrlTask(urlTask))
        /*    */{
            /* 72 */UrlHtmlUnitCheckTask urlCheckTask = new UrlHtmlUnitCheckTask();
            /* 73 */urlCheckTask.setResultCollector(this.resultCollector);
            /* 74 */urlCheckTask.setRunner(this);
            /* 75 */urlCheckTask.setUrlTask(urlTask);
            /* 76 */addTask(urlCheckTask);
            /*    */} else {
            /* 78 */delWaitTask();
            /*    */}
        /*    */}

    /*    */
    /*    */private void addTask(UrlHtmlUnitCheckTask urlCheckTask)
    /*    */{
        /* 89 */this.exec.execute(urlCheckTask);
        /*    */}

    /*    */
    /*    */private void shutdown()
    /*    */{
        /* 96 */this.exec.shutdown();
        /*    */}

    /*    */
    /*    */public void run()
    /*    */{
        /* 103 */File checkListFile = TaskConfig.getCheckListFile();
        /* 104 */this.resultCollector.setReportPath(TaskConfig.getReportPath());
        /* 105 */this.resultCollector.setRunStartTime(System.currentTimeMillis());
        /*    */
        /* 108 */CheckUrlFileParser parser = new CheckUrlFileParser();
        /* 109 */List urlCheckGroupList = null;
        /*    */try {
            /* 111 */urlCheckGroupList = parser.parser(checkListFile);
            /*    */
            /* 114 */WebClientWrapper.initPool(urlCheckGroupList);
            /*    */} catch (Exception e) {
            /* 116 */e.printStackTrace();
            /* 117 */System.exit(1);
            /*    */}
        /*    */
        /* 121 */for (UrlCheckGroup urlCheckGroup : urlCheckGroupList) {
            /* 122 */List urlList = urlCheckGroup.getUrlList();
            /* 123 */groupName = urlCheckGroup.getName();
            /* 124 */for (i$ = urlList.iterator(); i$.hasNext();) {
                url = (UrlCheckList) i$.next();
                /* 125 */maxLevel = TaskConfig.getMaxLevel();
                /* 126 */if (null != url.getLevel())
                /* 127 */maxLevel = url.getLevel().intValue();
                /* 128 */for (String urlStr : url.getUrlList()) {
                    /* 129 */UrlTask task = new UrlTask();
                    /* 130 */task.setLevel(1);
                    /* 131 */task.setMaxLevel(maxLevel);
                    /* 132 */task.setCheckChildren(true);
                    /* 133 */task.setParentUrl(null);
                    /* 134 */task.setTopParentUrl(null);
                    /* 135 */task.setSrc(urlStr);
                    /* 136 */task.setGroupName(groupName);
                    /* 137 */if (null != url.getCheckCss())
                    /* 138 */task.setCheckCss(url.getCheckCss());
                    /*    */else
                    /* 140 */task.setCheckCss(Boolean.valueOf(TaskConfig.isCheckCss()));
                    /* 141 */if (null != url.getCheckImg())
                    /* 142 */task.setCheckImg(url.getCheckImg());
                    /*    */else
                    /* 144 */task.setCheckCss(Boolean.valueOf(TaskConfig.isCheckImg()));
                    /* 145 */if (null != url.getCheckJs())
                    /* 146 */task.setCheckJs(url.getCheckJs());
                    /*    */else
                    /* 148 */task.setCheckJs(Boolean.valueOf(TaskConfig.isCheckJs()));
                    /* 149 */addTask(task, true);
                    /*    */}
                /*    */}
            /*    */}
        /*    */String groupName;
        /*    */Iterator i$;
        /*    */UrlCheckList url;
        /*    */int maxLevel;
        /* 157 */MonitorThread monitorThread = new MonitorThread();
        /* 158 */monitorThread.setDaemon(true);
        /* 159 */monitorThread.start();
        /*    */while (true)
        /*    */{
            /* 163 */if (this.exec.isTerminated()) {
                /* 164 */System.out.println("Url Check Task is completed!");
                /* 165 */this.resultCollector.setRunStopTime(System.currentTimeMillis());
                /* 166 */ResultCreator.generatorReport(this.resultCollector);
                /* 167 */break;
                /*    */}
            /*    */try {
                /* 170 */TimeUnit.MILLISECONDS.sleep(100L);
                /*    */}
            /*    */catch (InterruptedException e)
            /*    */{
                /*    */}
            /*    */}
        /*    */}

    /*    */
    /*    */class MonitorThread extends Thread
    /*    */{

        /*    */MonitorThread()
        /*    */{
            /*    */}

        /*    */
        /*    */public void run() {
            /* 185 */long preTaskNum = 0L;
            /* 186 */long preTaskTime = System.currentTimeMillis();
            /*    */while (true) {
                /* 188 */long taskNum = HtmlUnitRunner.this.getWaitTask().get();
                /* 189 */if (taskNum <= 0L) {
                    /* 190 */HtmlUnitRunner.this.shutdown();
                    /* 191 */break;
                    /*    */}
                /* 193 */System.out.println("剩余任务数:" + taskNum);
                /* 194 */if (preTaskNum == taskNum) {
                    /* 195 */long currentTaskTime = System.currentTimeMillis();
                    /* 196 */if (currentTaskTime - preTaskTime > 15000L) {
                        /* 197 */System.out.println("任务执行超时，退出！");
                        /* 198 */HtmlUnitRunner.this.shutdown();
                        /* 199 */break;
                        /*    */}
                    /*    */} else {
                    /* 202 */preTaskTime = System.currentTimeMillis();
                    /*    */}
                /*    */try
                /*    */{
                    /* 206 */TimeUnit.MILLISECONDS.sleep(1000L);
                    /*    */}
                /*    */catch (InterruptedException e) {
                    /*    */}
                /* 210 */preTaskNum = taskNum;
                /*    */}
            /*    */}
        /*    */
    }
    /*    */
}
