/*     */package utils;

/*     */
/*     */import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
/*     */public class ExcelUtil
/*     */{

    /*     */private String importFile;
    /*     */private String exportFile;
    /*     */private int    sheetNum;

    /*     */
    /*     */public List<String> importExcelTitle()
    /*     */{
        /* 29 */List list = new ArrayList();
        /*     */try {
            /* 31 */XSSFWorkbook XWB = new XSSFWorkbook(this.importFile);
            /* 32 */XSSFSheet xSheet = XWB.getSheetAt(this.sheetNum);
            /* 33 */if (xSheet.getLastRowNum() < 1) {
                /* 34 */return null;
                /*     */}
            /* 36 */XSSFRow xRowTitle = xSheet.getRow(0);
            /*     */
            /* 38 */for (int i = 0; i < xRowTitle.getLastCellNum(); i++)
            /*     */{
                /* 42 */list.add(xRowTitle.getCell(i).toString());
                /*     */}
            /*     */}
        /*     */catch (IOException e)
        /*     */{
            /* 47 */e.printStackTrace();
            /*     */}
        /* 49 */return list;
        /*     */}

    /*     */
    /*     */public List<String[]> importExcelData(int titleCells) {
        /* 53 */List list = new ArrayList();
        /*     */try {
            /* 55 */XSSFWorkbook XWB = new XSSFWorkbook(this.importFile);
            /* 56 */XSSFSheet xSheet = XWB.getSheetAt(this.sheetNum);
            /* 57 */if (xSheet.getLastRowNum() < 1) {
                /* 58 */return null;
                /*     */}
            /*     */
            /* 61 */for (int rowNum = 1; rowNum <= xSheet.getLastRowNum(); rowNum++) {
                /* 62 */XSSFRow xRow = xSheet.getRow(rowNum);
                /* 63 */String[] string = new String[titleCells];
                /*     */
                /* 65 */for (int cellNum = 0; cellNum < xRow.getLastCellNum(); cellNum++)
                /*     */{
                    /* 68 */string[cellNum] = (xRow.getCell(cellNum) == null ? null : xRow.getCell(cellNum).toString());
                    /*     */}
                /*     */
                /* 71 */for (int m = xRow.getLastCellNum() + 1; m <= titleCells; m++)
                /*     */{
                    /* 73 */string[(m - 1)] = null;
                    /*     */}
                /* 75 */list.add(string);
                /*     */}
            /*     */}
        /*     */catch (IOException e)
        /*     */{
            /* 80 */e.printStackTrace();
            /*     */}
        /* 82 */return list;
        /*     */}

    /*     */
    /*     */public void createExportTitle(Workbook wb, Sheet exportSheet, List<String> titleList, int othercoclor)
    /*     */{
        /* 91 */Row rowTitle = exportSheet.createRow(0);
        /*     */
        /* 93 */int i = 0;
        /* 94 */for (String title : titleList) {
            /* 95 */Cell titlecell = rowTitle.createCell(i);
            /* 96 */titlecell.setCellType(1);
            /* 97 */titlecell.setCellValue(title);
            /*     */
            /* 99 */CellStyle my_style = wb.createCellStyle();
            /* 100 */if (i < othercoclor) {
                /* 101 */my_style.setFillPattern((short) 2);
                /* 102 */my_style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                /* 103 */my_style.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
                /*     */} else {
                /* 105 */my_style.setFillPattern((short) 2);
                /* 106 */my_style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
                /* 107 */my_style.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
                /*     */}
            /* 109 */titlecell.setCellStyle(my_style);
            /*     */
            /* 111 */i++;
            /*     */}
        /*     */}

    /*     */
    /*     */public void createExportRow(Sheet exportSheet, List<Object> list, int rowNum)
    /*     */{
        /* 120 */Row row = exportSheet.createRow((short) rowNum);
        /*     */
        /* 122 */for (int i = 0; i < list.size(); i++) {
            /* 123 */Object object = list.get(i);
            /*     */try {
                /* 125 */if (null == object)
                /* 126 */row.createCell(i).setCellValue("");
                /* 127 */else if ((object.getClass().newInstance() instanceof String))
                /* 128 */row.createCell(i).setCellValue((String) object);
                /* 129 */else if ((object.getClass().newInstance() instanceof Boolean)) {
                    /* 130 */row.createCell(i).setCellValue(((Boolean) object).booleanValue());
                    /*     */}
                /*     */}
            /*     */catch (InstantiationException e)
            /*     */{
                /* 135 */e.printStackTrace();
                /*     */}
            /*     */catch (IllegalAccessException e) {
                /* 138 */e.printStackTrace();
                /*     */}
            /*     */}
        /*     */}

    /*     */
    /*     */public void exportFile(Workbook xwb)
    /*     */{
        /*     */try
        /*     */{
            /* 149 */FileOutputStream fileOut = new FileOutputStream(getExportFile());
            /* 150 */xwb.write(fileOut);
            /* 151 */fileOut.close();
            /*     */}
        /*     */catch (Exception e) {
            /* 154 */e.printStackTrace();
            /*     */}
        /*     */}

    /*     */
    /*     */public String getImportFile()
    /*     */{
        /* 160 */return this.importFile;
        /*     */}

    /*     */
    /*     */public void setImportFile(String importFile) {
        /* 164 */this.importFile = importFile;
        /*     */}

    /*     */
    /*     */public String getExportFile() {
        /* 168 */return this.exportFile;
        /*     */}

    /*     */
    /*     */public void setExportFile(String exportFile) {
        /* 172 */this.exportFile = exportFile;
        /*     */}

    /*     */
    /*     */public int getSheetNum() {
        /* 176 */return this.sheetNum;
        /*     */}

    /*     */
    /*     */public void setSheetNum(int sheetNum) {
        /* 180 */this.sheetNum = sheetNum;
        /*     */}

    /*     */
    /*     */public void addlastrow(String filepath, String SheetName, List<String> reportlist, List<Object> list)
    /*     */{
        /*     */Workbook wb;
        /*     */try
        /*     */{
            /* 188 */File f = new File(filepath);
            /*     */
            /* 190 */if (!f.exists()) {
                /* 191 */setExportFile(filepath);
                /* 192 */Workbook wb = new XSSFWorkbook();
                /* 193 */Sheet sheet = wb.createSheet(SheetName);
                /* 194 */List titleList = new ArrayList();
                /* 195 */titleList.add("URL");
                /* 196 */titleList.add("status");
                /* 197 */titleList.add("parent_url");
                /* 198 */titleList.add("message");
                /* 199 */int othercolor = titleList.size();
                /* 200 */for (int i = 0; i < reportlist.size(); i++) {
                    /* 201 */titleList.add(reportlist.get(i));
                    /*     */}
                /*     */
                /* 205 */double widthper = 264.82999999999998D;
                /* 206 */sheet.setColumnWidth(0, (short) (int) widthper * 50);
                /* 207 */sheet.setColumnWidth(2, (short) (int) widthper * 30);
                /* 208 */sheet.setColumnWidth(4, (short) (int) widthper * 15);
                /* 209 */sheet.setColumnWidth(5, (short) (int) widthper * 15);
                /* 210 */sheet.setColumnWidth(6, (short) (int) widthper * 15);
                /* 211 */createExportTitle(wb, sheet, titleList, othercolor);
                /* 212 */exportFile(wb);
                /*     */}
            /* 214 */FileInputStream fileInputStream = new FileInputStream(filepath);
            /* 215 */wb = new XSSFWorkbook(fileInputStream);
            /*     */}
        /*     */catch (FileNotFoundException e)
        /*     */{
            /* 219 */e.printStackTrace();
            /* 220 */wb = null;
            /*     */}
        /*     */catch (IOException e) {
            /* 223 */e.printStackTrace();
            /* 224 */wb = null;
            /*     */}
        /*     */
        /* 228 */if (wb != null) {
            /* 229 */Sheet sheetadd = wb.getSheet(SheetName);
            /* 230 */int addrownum = sheetadd.getLastRowNum() + 1;
            /*     */
            /* 232 */for (int i = 0; i < list.size(); i += 4) {
                /* 233 */List listtemp = new ArrayList();
                /* 234 */listtemp.add(list.get(i));
                /* 235 */listtemp.add(list.get(i + 1));
                /* 236 */listtemp.add(list.get(i + 2));
                /* 237 */listtemp.add(list.get(i + 3));
                /* 238 */createExportRow(sheetadd, listtemp, addrownum);
                /* 239 */addrownum++;
                /* 240 */listtemp = null;
                /*     */}
            /*     */
            /* 244 */exportFile(wb);
            /*     */}
        /*     */}

    /*     */
    /*     */public static void main(String[] args)
    /*     */{
        /* 292 */ExcelUtil excelUtil = new ExcelUtil();
        /* 293 */excelUtil.setExportFile("D:\\getPauseServiceData1.xlsx");
        /*     */
        /* 295 */List rowvalue = new ArrayList();
        /* 296 */rowvalue.add("111");
        /* 297 */rowvalue.add("222");
        /* 298 */rowvalue.add("111");
        /* 299 */rowvalue.add("222");
        /* 300 */rowvalue.add("111");
        /* 301 */rowvalue.add("222");
        /* 302 */rowvalue.add("111");
        /* 303 */rowvalue.add("222");
        /*     */
        /* 305 */List titlevalue = new ArrayList();
        /* 306 */titlevalue.add("111");
        /* 307 */titlevalue.add("222");
        /* 308 */excelUtil.addlastrow(excelUtil.getExportFile(), "sheet1", titlevalue, rowvalue);
        /*     */}
    /*     */
}
