<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
   
<%
	//response.sendRedirect(request.getContextPath() + "/home");
	// adminResourceMgr
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<title>Insert title here</title>
<script>

	$(function() {
		initDataGrid();		
	});	
	
	function initDataGrid() {
		$('#tt').datagrid({ 
			title:'测试数据',
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
	        iconCls:'icon-save',		   
		    columns:[[  
		        {field:'id',hidden:true},
		        {field:'col1',title:'col1',width:100,align:'center'},
		        {field:'col2',title:'col2',width:100,align:'center'},
		        {field:'col3',title:'col3',width:100,align:'center'},
		        {field:'col4',title:'col4',width:100,align:'center'},
		        {field:'col5',title:'col5',width:100,align:'center'},
		        {field:'col6',title:'col6',width:100,align:'center'},
		        {field:'col7',title:'col7',width:100,align:'center'},
		        {field:'col8',title:'col8',width:100,align:'center'},
		        {field:'col9',title:'col9',width:100,align:'center'},
		        {field:'col10',title:'col10',width:100,align:'center'},
		        {field:'col11',title:'col11',width:100,align:'center'},
		        {field:'col12',title:'col12',width:100,align:'center'},
		        {field:'col13',title:'col13',width:100,align:'center'},
		        {field:'col14',title:'col14',width:100,align:'center'},
		        {field:'col15',title:'col15',width:100,align:'center'},
		        {field:'col16',title:'col16',width:100,align:'center'},
		        {field:'col17',title:'col17',width:100,align:'center'},
		        {field:'col18',title:'col18',width:100,align:'center'},
		        {field:'col19',title:'col19',width:100,align:'center'},
		        {field:'col20',title:'col20',width:100,align:'center'},
		        {field:'col21',title:'col21',width:100,align:'center'},
		        {field:'col22',title:'col22',width:100,align:'center'},
		        {field:'col23',title:'col23',width:100,align:'center'},
		        {field:'col24',title:'col24',width:100,align:'center'},
		        {field:'col25',title:'col25',width:100,align:'center'},
		        {field:'col26',title:'col26',width:100,align:'center'},
		        {field:'col27',title:'col27',width:100,align:'center'},
		        {field:'col28',title:'col28',width:100,align:'center'},
		        {field:'col29',title:'col29',width:100,align:'center'},
		        {field:'col30',title:'col30',width:100,align:'center'}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]			
		});
	}
	
	function loadData() {
		$('#tt').datagrid({	        
	        url:'${pageContext.request.contextPath}/adminResourceMgr!test',
			queryParams : {},
			onLoadSuccess:function(data) { }
		});
	}
	
</script>
</head>
<body>
<a id="btnQuery" href="javascript:void(0);" class="easyui-linkbutton" onclick="loadData();">查询</a>
<table id="tt"></table>
</body>
</html>