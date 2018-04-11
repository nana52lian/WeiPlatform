<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String account = request.getParameter("account") == null ? "" : request.getParameter("account").toString();
	String id = request.getParameter("id") == null ? "" : request.getParameter("id").toString();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>反馈咨询</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>

<script type="text/javascript">
	function saveFeedback() {
		if ($("#feedback").val() == "") {
			$.messager.alert('信息', "请填写反馈信息!");	
			return ;
		}		
		var params = JSON.stringify(serializeObject($("#form_feedback")));		
		$.post("${pageContext.request.contextPath}/advisory!saveFeedback",
        	{
				req_params : params
        	},
        	function(data){
        		$.messager.alert('信息', data.resDesc);	        		
        	},
        	"json"
        ); 
	}
	
	function checkbox(obj) {
		if(obj.checked) {
	    	obj.value = 1;
	  	} else {
	    	obj.value = 0; 
	  	}
	}
	
</script>
<style type="text/css">
	body {
		font-family:helvetica,tahoma,verdana,sans-serif;
		font-size:12px;
		margin:0;
		padding:0;
		widht:100%;
		height:100%;
		background-color: #d6f0fa;
		color: #3d3d3d;
	}
	
	input, label, textarea {
		vertical-align:middle;
	}
</style>
</head> 
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<form id="form_feedback">
		<input type="hidden" id="account" name="account" value="<%=account %>">
		<input type="hidden" id="user_schedule_id" name="user_schedule_id" value="<%=id %>">
		<table width="100%" border="1" cellpadding="5" cellspacing="0" style="border-collapse:collapse;border:1px solid #64b3a4;">
			<tbody>
				<tr> 
					<td colspan="2" style="background-color: #e5f1f1;"> 							
						<label>经过此次咨询，您对咨询师的感受</label>
					</td>
				</tr>
				<tr>					
					<td> 
						<input type="checkbox" id="assess1" name="assess1" onclick="checkbox(this);"/>
						<label for="assess1">有收货的</label>						
					</td>
					<td>
						<input type="checkbox" id="assess2" name="assess2" onclick="checkbox(this);"/>
						<label for="assess2">被理解和尊重</label>						
					</td>
				</tr>
				<tr> 
					<td> 
						<input type="checkbox" id="assess3" name="assess3" onclick="checkbox(this);"/>
						<label for="assess3">感觉舒适的</label>						
					</td>
					<td>
						<input type="checkbox" id="assess4" name="assess4" onclick="checkbox(this);"/>
						<label for="assess4">可信赖的</label>						
					</td>
				</tr>
				<tr> 
					<td> 
						<input type="checkbox" id="assess5" name="assess5" onclick="checkbox(this);"/>
						<label for="assess5">倾向于指导</label>						
					</td>
					<td>
						<input type="checkbox" id="assess6" name="assess6" onclick="checkbox(this);"/>
						<label for="assess6">倾向于启发</label>						
					</td>
				</tr>
				<tr> 
					<td> 
						<input type="checkbox" id="assess7" name="assess7" onclick="checkbox(this);"/>
						<label for="assess7">倾向于倾听</label>						
					</td>
					<td>
						
					</td>
				</tr>
			</tbody>
		</table>
		<div style="height:10px;"></div> 
		<table width="100%" border="1" cellpadding="5" cellspacing="0" style="border-collapse:collapse;border:1px solid #64b3a4;">
			<tbody>
				<tr>
					<td style="background-color: #e5f1f1;">
						<label>反馈内容 </label>
					</td>
				</tr>
				<tr>
					<td>
						<textarea id="feedback" name="feedback" rows="5" style="width:98%;" ></textarea>
					</td>
				</tr>
			</tbody>
		</table>
		<div style="height:5px;"></div>
		<div style="width:100%;text-align:center;">
			<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-ok" onclick="saveFeedback();">保存</a>
		</div>	
	</form>
</div>
</body>
</html>