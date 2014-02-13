/*    */package result;

import urlcheck.UrlTask;
/*    */public class CheckResult
/*    */{

    /*    */private UrlTask urlTask;
    /*    */private int     statusCode;
    /*    */private String  msg;
    /*    */private long    loadTime;
    /*    */private boolean success;
    /*    */private boolean isLoadFail;

    /*    */
    /*    */public UrlTask getUrlTask()
    /*    */{
        /* 15 */return this.urlTask;
        /*    */}

    /*    */
    /*    */public void setUrlTask(UrlTask urlTask) {
        /* 19 */this.urlTask = urlTask;
        /*    */}

    /*    */
    /*    */public int getStatusCode() {
        /* 23 */return this.statusCode;
        /*    */}

    /*    */
    /*    */public void setStatusCode(int statusCode) {
        /* 27 */this.statusCode = statusCode;
        /*    */}

    /*    */
    /*    */public long getLoadTime() {
        /* 31 */return this.loadTime;
        /*    */}

    /*    */
    /*    */public void setLoadTime(long loadTime) {
        /* 35 */this.loadTime = loadTime;
        /*    */}

    /*    */
    /*    */public void setMsg(String msg) {
        /* 39 */this.msg = msg;
        /*    */}

    /*    */
    /*    */public String getMsg() {
        /* 43 */return this.msg;
        /*    */}

    /*    */
    /*    */public String toString()
    /*    */{
        /* 49 */return "CheckResult [statusCode=" + this.statusCode + ", isLoadFail=" + this.isLoadFail + ", loadTime="
                       + this.loadTime + ", success=" + this.success + ", msg=" + this.msg + ", urlTask="
                       + this.urlTask + "]";
        /*    */}

    /*    */
    /*    */public void setSuccess(boolean success)
    /*    */{
        /* 55 */this.success = success;
        /*    */}

    /*    */
    /*    */public boolean isSuccess() {
        /* 59 */return this.success;
        /*    */}

    /*    */
    /*    */public void setLoadFail(boolean isLoadFail) {
        /* 63 */this.isLoadFail = isLoadFail;
        /*    */}

    /*    */
    /*    */public boolean isLoadFail() {
        /* 67 */return this.isLoadFail;
        /*    */}
    /*    */
}
