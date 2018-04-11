package com.yidatec.weixin.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PoiReadExcel {
	
	public Sheet readExcel(String fileName){
		
        FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Workbook wb = null;
		Sheet sheet;
		
		//2003
		if (fileName.endsWith(".xls")) {
			try {
				wb = new HSSFWorkbook(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//2007
		if (fileName.endsWith(".xlsx")) {
			try {
				wb = new XSSFWorkbook(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		sheet = wb.getSheetAt(0);
		return sheet;
	}

}
