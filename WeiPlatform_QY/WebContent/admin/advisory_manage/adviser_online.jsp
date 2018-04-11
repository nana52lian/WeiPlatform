<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>咨询师列表</title>	
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
</style>
<script>

	$(function() {
		initPlatformUserGrid();
	});	

	//在线咨询师
	function initPlatformUserGrid() {		
		$('#tt_adviser').datagrid({ 
			title:'咨询师',
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			url:'${pageContext.request.contextPath}/adviserMgr!getAllOnlineAdviser',
		    columns:[[  
		        {field:'id',hidden:true},
		        {field:'account',title:'登录账号',width:90,align:'center'},  
		        {field:'name',title:'姓名',width:90,align:'center'},  
		        {field:'type',title:'类型',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	var statusText='';
					if(value=='2')
					 statusText = '全职';
					else if(value=='3')
					 statusText = '兼职';
					return statusText;
		        }}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]			
		});  
	}
	
	//刷新
	function refresh(){
		initPlatformUserGrid();
	}
	
	
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<div class="conditionDiv">
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="refresh();">刷新</a>
	</div>
	<table id="tt_adviser"></table>
</div>	
</body>
</html>