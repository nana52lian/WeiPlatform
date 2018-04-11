<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>心情能量详情</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/weixin.css">
</head>
<body>
	<div > 
		<label><s:property value='adviserEntity.currentMonth' />月：</label>
		<label><a href='<s:property value='adviserEntity.lastMonthMoodUrl' />weixin!getMoodDetail?fromUserName=<s:property value='adviserEntity.fromUserName' />&month=<s:property value='adviserEntity.currentMonth' />'>点击查看前一个月</a></label>
	</div>
	<div style="margin-top:10px;">
		<img src='/<s:property value='adviserEntity.moodImg' />'  alt="" />
	</div>
	<div>
		<label>疾病源于习惯和情绪，您过去两周状态：<s:property value='adviserEntity.presentation' /></label>
	</div>
	<div style="color:red;font-size:12px;margin-top:10px;">微信添加朋友，查找微信公众账号：心灵四叶草  就可以关注我们</div>
</body>
</html>