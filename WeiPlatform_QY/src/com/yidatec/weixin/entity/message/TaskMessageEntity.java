package com.yidatec.weixin.entity.message;

import com.yidatec.weixin.entity.BaseEntity;

public class TaskMessageEntity extends BaseEntity{

	private static final long serialVersionUID = -7939395142726181331L;
	//ID
	private String id;
	//任务ID
    private String task_queue_id;
    //客户ID
	private String open_id;
	//客服ID
	private String service_id;
	//内容
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTask_queue_id() {
		return task_queue_id;
	}

	public void setTask_queue_id(String task_queue_id) {
		this.task_queue_id = task_queue_id;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
