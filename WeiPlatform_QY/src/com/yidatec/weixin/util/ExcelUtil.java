package com.yidatec.weixin.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtil {
	
	/**
     * 获取字符串数据
     * @param cell
     * @return
     */
	public static String getCellStringValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
            	try {
            		cellvalue = String.valueOf(cell.getRichStringCellValue());
            	} catch (Exception ex) {
            		cellvalue = cell.getCellFormula();
            	}
                break;
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_NUMERIC: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                	BigDecimal original = new BigDecimal(Double.valueOf(cell.getNumericCellValue()).doubleValue()); 
            		cellvalue = String.valueOf(original);
                    if (cellvalue.endsWith(".0")) {
                    	cellvalue = cellvalue.substring(0, cellvalue.length() - 2);
                    }
                }
                break;
            }
            case HSSFCell.CELL_TYPE_FORMULA: {
            	cellvalue = cell.getCellFormula();
            	break;
            }
           
            // 默认的Cell值
            default:
                cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
    
    /**
     * 获取整形的数据
     * @param cell
     * @return
     */
    public static int getCellIntValue(Cell cell) {
    	if (cell == null) {
    		return 0;
    	}
    	if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
    		return (int) cell.getNumericCellValue();
    	}
    	
        String cellvalue = getCellStringValue(cell);
        
        try {
        	return Integer.parseInt(cellvalue);
        } catch (Exception ex) {
        	return 0;
        }

    }
    
    /**
     * 获取浮点形的数据
     * @param cell
     * @return
     */
    public static double getCellDoubleValue(Cell cell) {
    	if (cell == null) {
    		return 0;
    	}
    	if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
    		return cell.getNumericCellValue();
    	}
    	
        String cellvalue = getCellStringValue(cell);
        
        try {
        	return Double.parseDouble(cellvalue);
        } catch (Exception ex) {
        	return 0;
        }

    }
    
    /**
     * 获取日期数据
     * @param cell
     * @return
     */
    public static Date getCellDateValue(Cell cell) {
    	
    	if (cell != null) {
	    	if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC || cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
	    		if (HSSFDateUtil.isCellDateFormatted(cell)) {
	                return cell.getDateCellValue();
	            }
	    	}
    	}
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");   
    	Date date = null;
		try {
			date = df.parse("1900-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}   
    	
    	return date;
    }
}

