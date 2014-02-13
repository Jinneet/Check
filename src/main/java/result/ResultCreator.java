/*    */package result;

/*    */
/*    */import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import urlcheck.TaskConfig;
import utils.ExcelUtil;

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
/*    */public class ResultCreator
/*    */{

    /*    */public static void generatorReport(ResultCollector resultCollector)
    /*    */{
        /* 44 */SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        /*    */
        /* 46 */String date = dateFormat.format(new Date());
        /* 47 */String reportFileName = TaskConfig.getReportFileName();
        /* 48 */if (StringUtils.isEmpty(reportFileName)) {
            /* 49 */reportFileName = "result-" + date;
            /*    */}
        /* 51 */File logFile = null;
        /* 52 */if (!StringUtils.isEmpty(resultCollector.getReportPath())) {
            /* 53 */File logFolder = new File(resultCollector.getReportPath());
            /* 54 */if (!logFolder.exists()) {
                /* 55 */logFolder.mkdirs();
                /*    */}
            /*    */
            /* 58 */logFile = new File(logFolder, reportFileName);
            /*    */} else {
            /* 60 */logFile = new File(reportFileName);
            /*    */}
        /*    */
        /* 63 */System.out.println("Log file is :" + logFile.getAbsolutePath());
        /*    */
        /* 67 */Properties p = new Properties();
        /*    */
        /* 69 */p.setProperty("input.encoding", "UTF-8");
        /* 70 */p.setProperty("output.encoding", "UTF-8");
        /*    */
        /* 73 */p.setProperty("file.resource.loader.class",
                              "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        /*    */
        /* 76 */String excel_file_name = resultCollector.getReportPath() + reportFileName + ".xlsx";
        /* 77 */ExcelUtil excelUtil = new ExcelUtil();
        /* 78 */excelUtil.setExportFile(excel_file_name);
        /* 79 */List resultlist = resultCollector.getFailResultList();
        /* 80 */List rowvalue = new ArrayList();
        /* 81 */for (int i = 0; i < resultlist.size(); i++) {
            /* 82 */rowvalue.add(((CheckResult) resultlist.get(i)).getUrlTask().getSrc());
            /* 83 */rowvalue.add(String.valueOf(((CheckResult) resultlist.get(i)).getStatusCode()));
            /* 84 */rowvalue.add(((CheckResult) resultlist.get(i)).getUrlTask().getParentUrl());
            /* 85 */rowvalue.add(((CheckResult) resultlist.get(i)).getMsg());
            /*    */}
        /*    */
        /* 88 */List reportList = new ArrayList();
        /* 89 */reportList.add("成功率：" + resultCollector.getPassPercent());
        /* 90 */reportList.add("扫描总数：" + resultCollector.getRecordNum());
        /* 91 */reportList.add("失败url数：" + resultCollector.getFailRecordNum());
        /*    */
        /* 93 */excelUtil.addlastrow(excelUtil.getExportFile(), "urlresult", reportList, rowvalue);
        /*    */}
    /*    */
}
