<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="Cache-Control" content="no-cache,must-revalidate">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<meta name="format-detection" content="telephone=no, address=no">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black-translucent" name="apple-mobile-web-app-status-bar-style">
<link rel="stylesheet" href="${pageContext.request.contextPath}/js/jquerymobile/jquery.mobile-1.4.2.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/js/jquerymobile/jquery.mobile.theme-1.4.2.css">
<script src="<%=request.getContextPath()%>/js/jquery-1.8.3.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquerymobile/jquery.mobile-1.4.2.min.js"></script>
<script src="<%=request.getContextPath()%>/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquerymobile_popup.js"></script>
</head>
<body>
<script type="text/javascript">
function close(){
	WeixinJSBridge.call('closeWindow');
}
</script>

<img src="${pageContext.request.contextPath}/images/question.png" style="width: 40px" onclick="close()">

</body>
</html>