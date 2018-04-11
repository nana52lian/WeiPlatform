package com.yidatec.weixin.entity.message;

/**
 * 任务状态
 * @author cht
 *
 */
public class TaskStatus {
	
	/** 未处理  */
	public final static int UN_PROCESS = 0;
	
	/** 已经分配  */
	public final static int DISPATCH = 1;
	
	/** 正在处理  */
	public final static int PROCESSING = 2;
	
	/** 处理完成  */
	public final static int COMPLETED = 3;
	
	/** 升级完成  */
	public final static int STOP = 4;
	
	/** 被挂起  */
	public final static int WAIT = 5;

}
