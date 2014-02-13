/*    */package runner;

/*    */
/*    */import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import urlcheck.UrlTask;
import utils.MD5Utils;

/*    */
/*    */
/*    */
/*    */
/*    */public class TaskStatusPool
/*    */{

    /* 18 */ConcurrentHashMap<String, Integer> linkRecords = new ConcurrentHashMap();

    /*    */
    /*    */public boolean addUrlTask(UrlTask urlTask)
    /*    */{
        /* 27 */String url = urlTask.getSrc();
        /* 28 */String md5 = MD5Utils.md5(url);
        /* 29 */if (StringUtils.isEmpty(md5)) {
            /* 30 */md5 = url;
            /*    */}
        /* 32 */if ((StringUtils.startsWithIgnoreCase(url, "http://"))
                    || (StringUtils.startsWithIgnoreCase(url, "https://"))) {
            /* 33 */if (this.linkRecords.containsKey(md5)) {
                /* 34 */this.linkRecords.put(md5, Integer.valueOf(((Integer) this.linkRecords.get(md5)).intValue() + 1));
                /* 35 */return false;
                /*    */}
            /* 37 */this.linkRecords.put(md5, Integer.valueOf(1));
            /* 38 */return true;
            /*    */}
        /*    */
        /* 41 */return false;
        /*    */}
    /*    */
}
