package com.autoyol.field.progress.manage.util.poi;

import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

public class ExcelMergeUtil {

    public static void reportMergeXls(List<List<String>> rowList, List<String> excelHeader, List<String> excelMerge,
                                      List<String> secondTitle, String sheetName, int colCount, HttpServletResponse response) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);//创建一个sheet-test1

        //设置单元格风格，居中对齐.
        HSSFCellStyle cs = wb.createCellStyle();
        cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        cs.setWrapText(true); // 设置是否能够换行，能够换行为true

        //创建第一行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell;

        for (int i = 0; i < excelHeader.size(); i++) {//设置表头-标题
            cell = row.createCell(i);
            cell.setCellValue(excelHeader.get(i));
            cell.setCellStyle(cs);
            //sheet.autoSizeColumn(i);//自动设宽
            sheet.setColumnWidth(i, 2500);
            //纵向：合并第一列的第1行和第2行第
            sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
        }

        for (int i = excelHeader.size(); i < excelHeader.size() + excelMerge.size() * colCount; i++) {//设置表头-标题
            cell = row.createCell(i);
            cell.setCellValue(excelMerge.get((i - excelHeader.size()) / colCount));
            cell.setCellStyle(cs);
            //sheet.autoSizeColumn(i);//自动设宽
            sheet.setColumnWidth(i, 2000);
            for (int j = 1; j < colCount; j++) {
                cell = row.createCell(++i);
                cell.setCellStyle(cs);
                sheet.autoSizeColumn(i);//自动设宽
            }
            //横向：合并第一行的第i-1列到第i列
            sheet.addMergedRegion(new CellRangeAddress(0, 0, i - colCount + 1, i));
        }

        //设置对应的合并单元格标题
        row = sheet.createRow(1);
        for (int j = excelHeader.size(); j < excelHeader.size() + secondTitle.size(); j++) {
            cell = row.createCell(j);
            cell.setCellStyle(cs);
            cell.setCellValue(secondTitle.get(j - excelHeader.size()));
            //sheet.autoSizeColumn(j);//自动设宽
            sheet.setColumnWidth(j, 1500);
        }
        //设置列值-内容
        for (int i = 0; i < rowList.size(); i++) {
            row = sheet.createRow(i + colCount);
            List<String> data = rowList.get(i);

            int less = secondTitle.size() + excelHeader.size() * 2 - data.size();
            for (int j = 0; j < data.size() + less - excelHeader.size(); j++) {
                String val;
                if (less > 0 && j >= excelHeader.size() && j < less) {
                    val = CacheConstraint.STRING_EMPTY;
                } else {
                    int index = less > 0 && j >= less ? j - less + excelHeader.size() : j;
                    val = data.get(index >= data.size() ? index - excelHeader.size() : index);
                }
                row.createCell(j).setCellValue(val);
            }
        }

        flushToRequest(wb, response, sheetName);

    }

    public static void flushToRequest(HSSFWorkbook wb, HttpServletResponse response, String fileName)
            throws Exception {
        if (fileName == null) {
            fileName = "default";
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    String.format("attachment;filename=%s.xls", new String(fileName.getBytes("gb2312"), "iso8859-1")));
            OutputStream ouputStream = response.getOutputStream();
            exportStream(wb, ouputStream);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void exportStream(HSSFWorkbook wb, OutputStream o) throws Exception {
        try {
            wb.write(o);
            o.flush();
            o.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        return cell.getStringCellValue();
    }

    public static List<String> getMergedRegionValue(Sheet sheet, int row, String regex) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                Row fRow = sheet.getRow(firstRow);
                Cell fCell = fRow.getCell(firstColumn);
                String str = getCellValue(fCell);
                if (str.matches(regex)) {
                    list.add(str);
                }

            }
        }
        return list;
    }
}