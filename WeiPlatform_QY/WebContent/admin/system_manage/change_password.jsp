<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>	
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>

<style type="text/css">
	body {
		font-family:helvetica,tahoma,verdana,sans-serif;
		font-size:12px;
		margin:0;
		padding:0;
		widht:400px;
		height:400px;
	}	
	.Div {
		margin-bottom: 10px;
		border: 1px solid #CCCCCC;
		padding:10px;
		align: center;
	}
</style>
<script>
	//校验输入的原始密码是否正确
	function changePassword(){
		var form = document.changePasswordForm; 
	    var oldPsw = form.oldPassword.value; 
	    var newPsw = form.newPassword.value; 
	    var cfmPsw = form.confirmPassword.value; 
	    if(oldPsw =="" || newPsw == "" || cfmPsw == ""){ 
	    	$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="change_password_str1"></s:text>'); 
	        return false; 
	    } 
	    if(newPsw != cfmPsw){ 
	    	$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="change_password_str2"></s:text>'); 
	        return false; 
	    } 
	    $.post("${pageContext.request.contextPath}/adminPlatformUserMgr!validateOldPass",
	            { 
	    			req_params : oldPsw 
	    		},
	            function(flag){
	            	if (flag == 0) {
	            		$.messager.alert('Warning','<s:text name="change_password_str3"></s:text>');
	            		document.getElementById("oldPassword").value = null;
                		document.getElementById("newPassword").value = null;
                		document.getElementById("confirmPassword").value = null;
	            		return false;
                    } else {
                    	$('#changePasswordForm').ajaxSubmit({success:function(data){
                    		$.messager.alert("Info", '<s:text name="change_password_str4"></s:text>', "info", function () {
                    			window.location.href = "${pageContext.request.contextPath}/j_spring_security_logout";
                            });
                		}});
                    }
	            }
	    );
	    
	}
</script>
</head>
<body onload="changePasswordForm.oldPassword.focus()">
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
     	<form action="${pageContext.request.contextPath}/adminPlatformUserMgr!changePassword" id="changePasswordForm" name="changePasswordForm" method="post">
			<table >
				<tbody>
					<tr>
						<td><s:text name="change_password_str5"></s:text></td>
						<td><input id="oldPassword" name="oldPassword" type="password" /></td>
					</tr>
					<tr>
						<td><s:text name="change_password_str6"></s:text></td>
						<td><input id="newPassword" name="newPassword" type="password" /></td>
					</tr>
					<tr>
						<td><s:text name="change_password_str7"></s:text></td>
						<td><input id="confirmPassword" name="confirmPassword" type="password" /></td>
					</tr>
				</tbody>
			</table>
		
		<div style="padding-top:20px;padding-left:100px;">
			<a href="#" class="easyui-linkbutton" onclick="JavaScript:changePassword();"><s:text name="change_password_str8"></s:text></a> 
		</div>
		</form>
</div>	
</body>
</html>