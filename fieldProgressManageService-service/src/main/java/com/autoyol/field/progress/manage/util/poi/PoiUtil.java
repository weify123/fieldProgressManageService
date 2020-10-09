package com.autoyol.field.progress.manage.util.poi;

import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;


/**
 * excel 导入工具类
 *
 * @author jie.meng
 */
public class PoiUtil {


    /**
     * 根据版本得到excel工作区
     *
     * @return
     * @throws IOException
     */
    public static Workbook getWorkBook(String name, InputStream inputStream) throws IOException {
        Workbook workbook = null;
        //根据版本创建
        if (isExcel2003(name)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (isExcel2007(name)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    /***
     * 获取Excel表中的值
     * @param cell
     * @return 字符串
     */

    public static String getValue(Cell cell) {
        String cellValue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:// 空值
                    cellValue = "";
                    break;
                case Cell.CELL_TYPE_NUMERIC:// 数字
                    if (DateUtil.isCellDateFormatted(cell)) {
                        cellValue = cell.getDateCellValue().toString();
                    } else {
                        DecimalFormat df = new DecimalFormat("0");
                        String format = df.format(cell.getNumericCellValue());
                        cellValue = format;
                    }
                    break;

                case Cell.CELL_TYPE_BOOLEAN:// Boolean
                    cellValue = cell.getBooleanCellValue() + "";
                    break;

                case Cell.CELL_TYPE_FORMULA: // 公式
                    cellValue = cell.getCellFormula() + "";
                    break;
                case Cell.CELL_TYPE_STRING://字符串
                    cellValue = cell.getStringCellValue();
                    break;
                default:
                    break;
            }
        }
        return cellValue;
    }


    /**
     * 判断excel 空行
     */
    public static boolean isBlankRow(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i <= row.getLastCellNum() - 1; i++) {
            Cell hcell = row.getCell(i);
            if (!isBlankCell(hcell)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断excel 空单元格
     *
     * @param hcell
     * @return
     */
    public static boolean isBlankCell(Cell hcell) {
        if (hcell == null) {
            return true;
        }
        hcell.setCellType(Cell.CELL_TYPE_STRING);
        String content = hcell.getStringCellValue().trim();
        if (content == null || "".equals(content)) // 找到非空行
        {
            return true;
        }
        return false;
    }

    public static String getTimesValue(Cell cell) {
        String cellValue = "";
        if (null != cell) {
            if (0 == cell.getCellType()) {
                Date date = cell.getDateCellValue();
                cellValue = LocalDateUtil.dateToStringFormat(date, CacheConstraint.DATE_YYYY_MM_DD_LINE);
            } else {
                cellValue = cell.getStringCellValue();
                if (StringUtils.isNotBlank(cellValue)) {
                    try {
                        cellValue = LocalDateUtil.dateToStringFormat(LocalDateUtil.convertToDate(cellValue),
                                CacheConstraint.DATE_YYYY_MM_DD_LINE);
                    } catch (Exception e) {
                        cellValue = "00000000";
                    }
                }
            }
        }
        return cellValue;
    }

    /**
     * @描述：是否是2003的excel，返回true是2003
     * @参数：@param filePath　文件完整路径
     * @参数：@return
     * @返回值：boolean
     */
    private static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }


    /**
     * @描述：是否是2007的excel，返回true是2007
     * @参数：@param filePath　文件完整路径
     * @参数：@return
     * @返回值：boolean
     */
    private static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * @param row
     * @return String[]  返回类型
     * @Description: <p>获取表头 </p>
     */
    public static String[] getTabelHeader(Row row) {
        if (!isBlankRow(row)) {
            int colNum = row.getPhysicalNumberOfCells();
            String[] head = new String[colNum];
            for (int i = 0; i < colNum; i++) {
                head[i] = getValue(row.getCell(i));
            }
            return head;
        }
        return null;
    }
}
