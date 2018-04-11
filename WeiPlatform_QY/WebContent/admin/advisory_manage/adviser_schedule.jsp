<%@ page language="java" import="com.yidatec.weixin.common.GlobalDef" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<%@ taglib uri="/struts-tags" prefix="s"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的咨询时间管理</title>	
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
		width:160px;
	}
	
	.border_table {
		border:solid #add9c0; 
		border-width:1px 0px 0px 1px;
	}
	
	.border_table tbody tr td {
		border:solid #add9c0; 
		border-width:0px 1px 1px 0px;		
	}
	
</style>
<script>

	$(function() {
		$.post("adviserMgr!getSchedule",
	            function(data){
	            	//加载信息
	            	loadData(data);
	            },
	            "json"
	    );
	});
	
	function saveSchedule() {
		var params = JSON.stringify(serializeObject($("#form_schedule")));		
		$.post("${pageContext.request.contextPath}/adviserMgr!saveSchedule",
        	{
				req_params : params
        	},
        	function(data){
        		$.messager.alert('信息', data.resDesc);	        		
        	},
        	"json"
        ); 
	}
	
	function saveRemark() {
		var remark = $('#add_remark').val();	
		
		$.post("${pageContext.request.contextPath}/adviserMgr!saveRemark",
        	{
				req_params : remark
        	},
        	function(data){
        		$.messager.alert('信息', data.resDesc);	        		
        	},
        	"json"
        ); 
	}
	
	function loadData(data){
		for(var i=5;i<data.length+5;i++){
			if(data[i-5].status == 0){
				document.getElementById("check0_"+i).checked = true;
			} 
			if(data[i-5].status == 1){
				document.getElementById("check1_"+i).checked = true;
			}
		}
	}
	 
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<div class="topDiv">
		<table width="100%">
			<tr>
				<td width="5%">
					<label>备注：</label>
				</td>
				<td width="40%">
					<textarea id="add_remark" name="add_remark" cols="50" rows="2" ></textarea>	
				</td>
				<td width="50%">
					<a id="btnSaveRemark" href="#" class="easyui-linkbutton" onclick="saveRemark();">保存备注</a>
				</td>
			</tr>
		</table>	
	</div>
	<div class="topDiv">
		<form id="form_schedule">
		<div >  
			<table width="100%">
				<tr>
					<td width="50%">
						<h3>保存的设定将成为今天及以后的工作时间表，如有变化请及时修改和保存。</h3>
					</td>
					<td width="40%">
						<a id="btnSave" href="#" class="easyui-linkbutton" onclick="saveSchedule();">保存</a>		
					</td>
				</tr>
			</table>
		</div>
		<table width="100%" class="border_table" border="0" cellpadding="5" cellspacing="0" >
			<tbody>
				<tr>
					<td width="10%">时间</td>
					<td>状态</td>
				</tr>
				<s:iterator value="{'5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21'}" id="schedule" status="index">
					<s:if test="#index.odd">
               			<tr style="background-color: #fafafa;">
							<td><s:property value="#schedule"/>点</td>
							<td>
								<input id="check0_<s:property value="#schedule"/>" name="schedule_<s:property value="#schedule"/>" type="radio" value="0" />空闲
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input id="check1_<s:property value="#schedule"/>" name="schedule_<s:property value="#schedule"/>" type="radio" value="1" checked="checked"/>繁忙
							</td>
						</tr>
        			</s:if>
					<s:else>
						<tr> 
							<td><s:property value="#schedule"/>点</td>
							<td>
								<input id="check0_<s:property value="#schedule"/>" name="schedule_<s:property value="#schedule"/>" type="radio" value="0" />空闲
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input id="check1_<s:property value="#schedule"/>" name="schedule_<s:property value="#schedule"/>" type="radio" value="1" checked="checked"/>繁忙
							</td>
						</tr>
					</s:else>
					
					
				</s:iterator>				
			</tbody>
		</table>
		</form>
	</div>
</div>	
</body>
</html>