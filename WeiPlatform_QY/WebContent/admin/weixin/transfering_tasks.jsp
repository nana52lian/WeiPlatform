<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
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
<script type="text/javascript">
	$(function(){
		$('#tt').datagrid({ 
			title:'<s:text name="TRANSFERING_TASKS_STR7"></s:text>',
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			autoSizeColumn:true, 
			singleSelect:false,
	        iconCls:'icon-save',
		    url:'${pageContext.request.contextPath}/weixin!getTransferingTaskList',
		    columns:[[  
		        {field:'id',hidden:true},
		        {field:'toUserId',hidden:true},
		        {field:'nickname',title:'<s:text name="TRANSFERING_TASKS_STR2"></s:text>',width:100,align:'left'},
		        {field:'createDate',title:'<s:text name="TRANSFERING_TASKS_STR3"></s:text>',width:200,align:'left'},
		        {field:'status',title:'<s:text name="TRANSFERING_TASKS_STR4"></s:text>',width:100,align:'left',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='1')
		        		  statusText = '<s:text name="TRANSFERING_TASKS_STR8"></s:text>';
		        	  else if(value=='2')
		        		  statusText = '<s:text name="TRANSFERING_TASKS_STR9"></s:text>';
		        	  return statusText;
		        }},
		        {field:'fromUser',title:'<s:text name="TRANSFERING_TASKS_STR5"></s:text>',width:100,align:'left'},
		        {field:'toUser',title:'<s:text name="TRANSFERING_TASKS_STR6"></s:text>',width:100,align:'left'},
		    ]],
		    pagination:true,
	        pageList:[10,20,30],
	        frozenColumns:[[
	           {field:'ck',checkbox:true} 
	        ]]
		});
	});
	function acceptTransfer(){
		var selectRows = $('#tt').datagrid("getSelections");
		if(selectRows.length>0){
			var ids=[];
			var taskIds=[];
			var toUserIds=[];
			for(var key in selectRows){
				var id = selectRows[key].id;
				var taskId = selectRows[key].taskId;
				var toUserId = selectRows[key].toUserId;
				ids.push(id);
				taskIds.push(taskId);
				toUserIds.push(toUserId);
			}
			$.post("${pageContext.request.contextPath}/weixin!acceptTransferTask",{id:JSON.stringify(ids),taskId:JSON.stringify(taskIds),transferToId:JSON.stringify(toUserIds)},function(data){
				//alert(data.resDesc);
				$.messager.alert('<s:text name="INFO"></s:text>',data.resDesc);
				$('#tt').datagrid("reload");
			},"json");
		}
	}
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">	
	<div class="conditionDiv">
		<a id="btnClear" href="javascript:void(0)" class="easyui-linkbutton" onclick="acceptTransfer();"><s:text name="TRANSFERING_TASKS_STR1"></s:text></a>
	</div>
	<div class="conditionDiv">
		<table id="tt"></table>
	</div>
</div>
</body>
</html>