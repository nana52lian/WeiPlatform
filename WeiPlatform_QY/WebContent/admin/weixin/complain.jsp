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

<style type="text/css">
	.ui-page{
        background:#eee;
	}
</style>
</head>
<body>

<script type="text/javascript">
	
function sendMail(){
	
	
	//document.mailForm.submit();
	
	var prid = $("#prid").val();
	var call7575 = $("#call7575").val();
	var mail_topic = $("#mail_topic").val();
	var mail_detail = $("#mail_detail").val();
	var mail_call = $("#mail_call").val();
	
	if(prid=="" || call7575=="" || mail_topic=="" || mail_detail=="" || mail_call==""){
		openPopup('提交失败','Warning',undefined,true,undefined,'error','cn');
	} else {
		var params = JSON.stringify(serializeObject($("#mailForm")));
		$.post("<%=request.getContextPath()%>/WeixinViewAction!sendMail", {
			req_params : params
		}, function(data) {
			close();
		}, "json");
	}
}

function close(){
	window.location.href = "${pageContext.request.contextPath}/admin/weixin/success.jsp";
}
</script>


<div data-role="page" id="pageDetail">
	<div id="one" class="ui-body-d ui-content" style="padding:0;display: block;width: 100%;">
		<form name="mailForm" id="mailForm" action="<%=request.getContextPath()%>/WeixinViewAction!sendMail" method="post">
		<div style="width: 100%;padding:10px 0 10px 0;">
			<!-- <div style="padding-right:15px;padding-left:15px">
				<label for="feedback" class="font-label">Feedback Type</label>
				<input type="text" name="feedback" id="feedback" value="" placeholder="" class="">
			</div> -->
			<div style="padding-right:15px;padding-left:15px">
				<label for="feedback" class="font-label">Feedback Type</label>
				<select name="feedback" id="feedback" class="required font-blue" data-native-menu="false">
					<option value="Comment">Comment</option>
					<option value="Compliment">Compliment</option>
					<option value="Complaint">Complaint</option>
				</select>
			</div>
			<div style="padding-right:15px;padding-left:15px">
				<label for="prid" class="font-label">PRID</label>
				<input type="text" name="prid" id="prid" value="" placeholder="" class="">
			</div>
			<div style="padding-right:15px;padding-left:15px">
				<label for="call7575" class="font-label">是否有致电7575</label>
				<select name="call7575" id="call7575" class="required font-blue" data-native-menu="false">
					<option value="是">是</option>
					<option value="否">否</option>
				</select>
			</div>
			<div style="padding-right:15px;padding-left:15px">
				<label for="mail_topic" class="font-label">主题</label>
				<input type="text" name="mail_topic" id="mail_topic" value="" placeholder="" class="">
			</div>
			<div style="padding-right:15px;padding-left:15px">
				<label for="mail_detail" class="font-label">详细描述</label>
				<input type="text" name="mail_detail" id="mail_detail" value="" placeholder="" class="">
			</div>
			<div style="padding-right:15px;padding-left:15px">
				<label for="mail_call" class="font-label">联系方式</label>
				<input type="text" name="mail_call" id="mail_call" value="" placeholder="" class="">
			</div>
			<div style="padding:10px 15px 20px 15px;">
				<input id="subBtn" type="button" value="提交" onclick="sendMail()" style="padding:10px;background: #E57330;text-shadow: none;opacity:100;color:white;font-size:20px;text-indent:0px;font-family:微软雅黑;" >
			</div>
		</div>
		</form>
	</div>
</div>
</body>
</html>