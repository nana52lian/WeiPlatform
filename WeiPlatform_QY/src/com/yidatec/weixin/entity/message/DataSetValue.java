package com.yidatec.weixin.entity.message;

/**
 * JFreeChart 数据集合的某一个值的实体
 * @author Lance
 *
 */
public class DataSetValue {

	private double value = 0;
	
	private String rowkey = null;
	
	private String columnKey = null;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getRowkey() {
		return rowkey;
	}

	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}
	
	
}
