<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no"/>
<title>Appraisal Level</title>
</head>
<body>
<%
	String task_queue_id = request.getParameter("task_queue_id");
	if(task_queue_id == null)
	{
		task_queue_id = "";
	}
	String open_id = request.getParameter("open_id");
	if(open_id == null)
	{
		open_id = "";
	}
%> 
<div style="width:100%;border-bottom: 2px solid #317ecb;color:#000;font-size:1.375em;margin-bottom:5px;">
	You can tell us about your consulting experience, we will listen carefully.
</div>
<form name="form" action="${pageContext.request.contextPath}/weixin!appraise" method="post" >
<input type="hidden" id="task_queue_id" name="task_queue_id" value="<%=task_queue_id%>"/>
<input type="hidden" id="open_id" name="open_id" value="<%=open_id%>"/>
<div style="color:#393939;font-size:1em;margin:5px 0 5px 0;">
	<div style="padding:5px 0 5px;"><input type="radio" name="service_score" id="s1" value="5" checked="checked" /><label for="s1">Very satisfied</label></div>
	<div style="padding:5px 0 5px;"><input type="radio" name="service_score" id="s2" value="4" /><label for="s2">Satisfied</label></div>
	<div style="padding:5px 0 5px;"><input type="radio" name="service_score" id="s3" value="3" /><label for="s3">General</label></div>
	<div style="padding:5px 0 5px;"><input type="radio" name="service_score" id="s4" value="2" /><label for="s4">Dissatisfied</label></div>
	<div style="padding:5px 0 5px;"><input type="radio" name="service_score" id="s5" value="1" /><label for="s5">Very dissatisfied</label></div>
	<div style="padding:5px 0 5px;"><label for="s6">Appraisal description(You can only enter 150 characters)</label></div>
	<div style="padding:5px 0 5px;"><textarea id="asses" name="asses" rows="8" cols="37" maxlength="150" onkeyup="return DjCheckMaxlength(this);" title="You can only enter 150 characters"></textarea></div>
	<input type="submit" value="Submit">
</div>
</form>
</body>
</html>