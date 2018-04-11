<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no"/>
<title>帮助</title>
<style type="text/css">
a { text-decoration: none; }
a:link{color:#929292;text-decoration:none;} 
a:visited{color:#929292;text-decoration:none;}
a:hover{color:#929292;text-decoration:none;}
</style>
</head>
<body>
<s:iterator value="articleCatagoryList" >
	<div style="width:100%;border-bottom: 2px solid #317ecb;color:#000;font-size:1.375em;margin-bottom:5px;">
		<s:property value="name"/>
	</div>
	<s:iterator value="articleList">
		<div style="height:22px;border-bottom: 1px solid #eceef0;color:#393939;font-size:1em;margin:5px 0 5px 0;">
			<a href='${pageContext.request.contextPath}/weixin!getContent?ArticleID=<s:property value="id"/>' ><s:property value="title"/></a>
		</div>
	</s:iterator>
</s:iterator>
</body>
</html> 