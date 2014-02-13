/*     */package result;

/*     */
/*     */import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */public class ResultCollector
/*     */{

    /* 16 */private LinkedBlockingQueue<CheckResult> allCheckResultList          = new LinkedBlockingQueue();
    /* 17 */private LinkedBlockingQueue<CheckResult> failCheckResultList         = new LinkedBlockingQueue();
    /* 18 */private LinkedBlockingQueue<CheckResult> failLoadTimeCheckResultList = new LinkedBlockingQueue();
    /* 19 */private AtomicLong                       recordNum                   = new AtomicLong(0L);
    /* 20 */private long                             runStartTime                = 0L;
    /* 21 */private long                             runStopTime                 = 0L;
    /*     */private String                              reportPath;
    /* 23 */private SimpleDateFormat                 format                      = new SimpleDateFormat(
                                                                                                        "yyyy-MM-dd HH:mm:ss");

    /*     */
    /*     */public void addResult(CheckResult checkResult)
    /*     */{
        /* 27 */this.recordNum.getAndIncrement();
        /* 28 */if (!checkResult.isSuccess()) {
            /* 29 */this.failCheckResultList.add(checkResult);
            /*     */}
        /*     */
        /* 42 */if (checkResult.isLoadFail())
        /* 43 */this.failLoadTimeCheckResultList.add(checkResult);
        /*     */else
        /* 45 */this.allCheckResultList.add(checkResult);
        /*     */}

    /*     */
    /*     */public long getRecordNum()
    /*     */{
        /* 56 */return this.recordNum.get();
        /*     */}

    /*     */
    /*     */public List<CheckResult> getFailResultList() {
        /* 60 */List resultList = new ArrayList();
        /* 61 */CheckResult result = null;
        /* 62 */while ((result = (CheckResult) this.allCheckResultList.poll()) != null) {
            /* 63 */resultList.add(result);
            /*     */}
        /* 65 */return resultList;
        /*     */}

    /*     */
    /*     */public List<CheckResult> getFailLoadResultList() {
        /* 69 */List resultList = new ArrayList();
        /* 70 */CheckResult result = null;
        /* 71 */while ((result = (CheckResult) this.failLoadTimeCheckResultList.poll()) != null) {
            /* 72 */resultList.add(result);
            /*     */}
        /* 74 */return resultList;
        /*     */}

    /*     */
    /*     */public void setReportPath(String reportPath) {
        /* 78 */this.reportPath = reportPath;
        /*     */}

    /*     */
    /*     */public String getReportPath() {
        /* 82 */return this.reportPath;
        /*     */}

    /*     */
    /*     */public void setRunStartTime(long runStartTime) {
        /* 86 */this.runStartTime = runStartTime;
        /*     */}

    /*     */
    /*     */public long getRunStartTime() {
        /* 90 */return this.runStartTime;
        /*     */}

    /*     */
    /*     */public void setRunStopTime(long runStopTime) {
        /* 94 */this.runStopTime = runStopTime;
        /*     */}

    /*     */
    /*     */public long getRunStopTime() {
        /* 98 */return this.runStopTime;
        /*     */}

    /*     */
    /*     */public int getFailRecordNum() {
        /* 102 */return this.failCheckResultList.size();
        /*     */}

    /*     */
    /*     */public String getPassPercent() {
        /* 106 */long total = getRecordNum();
        /* 107 */int fail = getFailRecordNum();
        /* 108 */double percent = (total - fail) / total * 100.0D;
        /* 109 */return String.format("%.2f", new Object[] { Double.valueOf(percent) }) + "%";
        /*     */}

    /*     */
    /*     */public String getStartTimeStr() {
        /* 113 */if (0L != this.runStartTime) {
            /* 114 */return this.format.format(new Date(this.runStartTime));
            /*     */}
        /* 116 */return "";
        /*     */}

    /*     */
    /*     */public String getStopTimeStr() {
        /* 120 */if (0L != this.runStopTime) {
            /* 121 */return this.format.format(new Date(this.runStopTime));
            /*     */}
        /* 123 */return "";
        /*     */}

    /*     */
    /*     */public String getDuration() {
        /* 127 */long duration = 0L;
        /* 128 */if ((0L == this.runStartTime) || (0L == this.runStopTime)) {
            /* 129 */return "";
            /*     */}
        /* 131 */duration = (this.runStopTime - this.runStartTime) / 1000L;
        /*     */
        /* 133 */return String.valueOf(duration) + "秒";
        /*     */}
    /*     */
}
