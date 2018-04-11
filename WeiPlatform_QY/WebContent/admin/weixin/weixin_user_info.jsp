<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script> 
<script src="${pageContext.request.contextPath}/js/jquery.ui.datepicker-zh-CN.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<%-- <script src="${pageContext.request.contextPath}/js/jquery.form.js"></script> --%>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>
<script src="${pageContext.request.contextPath}/js/syUtil.js"></script>
<style type="text/css">
	body {
		font-family:helvetica,tahoma,verdana,sans-serif;
		font-size:12px;
		margin:0;
		padding:0;
		widht:100%;
		height:100%;
	}	
	.topDiv {
		margin-bottom: 10px;
		border: 1px solid #CCCCCC;
		padding:10px;
	}
	.conditionDiv {
		margin-bottom: 10px;
	}
	.mytext {
		width:180px;
	}
	.border_table {
		border:solid #add9c0; 
		border-width:1px 0px 0px 1px;
	}
</style>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
	<div class="conditionDiv">
		<table width="100%" border="0" cellpadding="0" cellspacing="5">
			<tbody>
				<tr>
					<td>
						<label>微信ID：</label>
					</td>
					<td  colspan="3" >
						<input id="openId" name="openId" type="text" style="width:250px;" disabled="disabled"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>昵称：</label>
					</td>
					<td>
						<input id="nickname" name="nickname" type="text" class="mytext" disabled="disabled"/>
					</td>
					<td>
						<label>K账号：</label>
					</td>
					<td>
						<input id="k_account_view" name="k_account_view" type="text" class="mytext" disabled="disabled"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>性别：</label>
					</td>
					<td>
						<input id="sex_view" name="sex_view" type="text" class="mytext" disabled="disabled"/>
					</td>
					<td>
						<label>状态：</label>
					</td>
					<td>
						<input id="status" name="status" type="text" class="mytext" disabled="disabled"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>等级：</label>
					</td>
					<td>
						<input id="grade_view" name="grade_view" type="text" class="mytext" disabled="disabled"/>
					</td>
					<td>
						<label>部门：</label>
					</td>
					<td>
						<input id="department_view" name="department_view" type="text" class="mytext" disabled="disabled"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>电话：</label>
					</td>
					<td>
						<input id="cellphone_view" name="cellphone_view" type="text" class="mytext" disabled="disabled"/>
					</td>
					<td>
						<label>邮箱：</label>
					</td>
					<td>
						<input id="email_view" name="email_view" type="text" class="mytext" disabled="disabled"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>地域：</label>
					</td>
					<td>
						<input id="city" name="" type="text" class="mytext" disabled="disabled"/>
					</td>
					<td>
						<label>关注时间：</label>
					</td>
					<td>
						<input id="subscribe_time" name="subscribe_time" type="text" class="mytext" disabled="disabled"/>
					</td>
				</tr>
			</tbody>
		</table>
    </div>	
    <div>
    	<table id="requestDatagrid"></table>
    </div>
    <div style="padding-top:7px;text-align:center;">  
   		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="openChatWindows();">查看聊天记录</a>
		<a href='javascript:void(0);' class="easyui-linkbutton" onclick="cancleWindow('weixinUserInfoDiv');">返回</a>  
	</div>
</body>
</html>