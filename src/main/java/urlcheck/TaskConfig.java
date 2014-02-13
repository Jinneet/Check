/*     */package urlcheck;

/*     */
/*     */import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */public class TaskConfig
/*     */{

    /* 19 */private static Properties properties       = new Properties();
    /*     */private static final String  CONFIG_FILE_NAME = "config.properties";
    /*     */private static final String  MAX_LEVEL        = "maxLevel";
    /*     */private static final String  THREAD_COUNT     = "threadCount";
    /*     */private static final String  CHECK_URL_FILE   = "checkUrlFile";
    /*     */private static final String  REPORT_PATH      = "reportPath";
    /*     */private static final String  REPORT_FILE_NAME = "reportFileName";
    /*     */private static final String  IS_CHECK_CSS     = "ischeck.css";
    /*     */private static final String  IS_CHECK_JS      = "ischeck.js";
    /*     */private static final String  IS_CHECK_IMG     = "ischeck.img";
    /*     */private static final String  MAX_LOAD_TIME    = "maxLoadTime";
    /*     */private static final String  CHECK_DOMAIN     = "checkDoamin";

    /*     */
    /*     */public static List<String> getCheckList()
    /*     */{
        /* 53 */File checkListFile = new File(properties.getProperty("checkUrlFile", "checkUrlList"));
        /* 54 */if (!checkListFile.exists())
        /* 55 */throw new RuntimeException("The check url list file is not existed!");
        /*     */try
        /*     */{
            /* 58 */return FileUtils.readLines(checkListFile);
            /*     */}
        /*     */catch (IOException e) {
            /* 61 */throw new RuntimeException("Read the check url list file error!", e);
            /*     */}
        /*     */}

    /*     */
    /*     */public static File getCheckListFile() {
        /* 66 */File checkListFile = new File(properties.getProperty("checkUrlFile", "checkUrlList"));
        /* 67 */if (!checkListFile.exists()) {
            /* 68 */throw new RuntimeException("The check url list file is not existed!");
            /*     */}
        /* 70 */return checkListFile;
        /*     */}

    /*     */
    /*     */public static int getMaxLevel()
    /*     */{
        /* 80 */String level = StringUtils.trim(properties.getProperty("maxLevel", "2"));
        /* 81 */return Integer.valueOf(level).intValue();
        /*     */}

    /*     */
    /*     */public static int getThreadCount()
    /*     */{
        /* 90 */String count = StringUtils.trim(properties.getProperty("threadCount", "5"));
        /* 91 */return Integer.valueOf(count).intValue();
        /*     */}

    /*     */
    /*     */public static String getReportPath()
    /*     */{
        /* 100 */return StringUtils.trim(properties.getProperty("reportPath"));
        /*     */}

    /*     */
    /*     */public static boolean isCheckJs() {
        /* 104 */return Boolean.valueOf(StringUtils.trim(properties.getProperty("ischeck.js", "true"))).booleanValue();
        /*     */}

    /*     */
    /*     */public static boolean isCheckCss() {
        /* 108 */return Boolean.valueOf(StringUtils.trim(properties.getProperty("ischeck.css", "true"))).booleanValue();
        /*     */}

    /*     */
    /*     */public static boolean isCheckImg() {
        /* 112 */return Boolean.valueOf(StringUtils.trim(properties.getProperty("ischeck.img", "false"))).booleanValue();
        /*     */}

    /*     */
    /*     */public static long getMaxLoadTime() {
        /* 116 */return Long.valueOf(StringUtils.trim(properties.getProperty("maxLoadTime"))).longValue();
        /*     */}

    /*     */
    /*     */public static String[] getCheckDomains() {
        /* 120 */String domains = StringUtils.trim(properties.getProperty("checkDoamin"));
        /* 121 */return StringUtils.split(domains, "|");
        /*     */}

    /*     */
    /*     */public static String getReportFileName() {
        /* 125 */return StringUtils.trim(properties.getProperty("reportFileName"));
        /*     */}

    /*     */
    /*     */static
    /*     */{
        /* 36 */InputStream in = null;
        /*     */try
        /*     */{
            /* 39 */in = FileUtils.openInputStream(new File("config.properties"));
            /* 40 */if (null == in) {
                /* 41 */throw new RuntimeException("The config file is null!");
                /*     */}
            /* 43 */properties.load(in);
            /*     */} catch (IOException e) {
            /* 45 */throw new RuntimeException("The config file is not existed!");
            /*     */} finally {
            /* 47 */IOUtils.closeQuietly(in);
            /*     */}
        /*     */}
    /*     */
}
