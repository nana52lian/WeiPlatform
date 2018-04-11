<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>请求类型设置</title>
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
		width:100px;
	}
</style>
<script>
	$(function() {
		$('#tt').datagrid({ 
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			autoSizeColumn:true, 
			singleSelect:true,
	        iconCls:'icon-save',
		    url:'${pageContext.request.contextPath}/adminResourceMgr!loadRequestTypes',
		    columns:[[  
		        {field:'id',hidden:true},
		        {field:'parentId',hidden:true},
		        {field:'description',title:'<s:text name="REQUEST_TYPE_STR5"></s:text>',width:400,align:'left'},
		        {field:'parentDescription',title:'<s:text name="REQUEST_TYPE_STR6"></s:text>',width:400,align:'left'},
		    ]],
		    pagination:true,
	        pageList:[10,20,30],
	        frozenColumns:[[
	           {field:'ck',checkbox:true} 
	        ]]
		});  
		
	});	
	function addTypeRequest(){
		$('#addTypeRequestDiv').window({  
	           modal:true,
	           width:400,
	           height:150,
	           left:100,
	           top:100
	    });
		
		$('#addTypeRequestDiv').window('open');
	}
	
	function editTypeRequest(){
		var selectRow = $('#tt').datagrid("getSelected");
		if(selectRow!=null){
			$("#editTypeRequestDiv input[id=description]").val(selectRow.description);
			$("#requestTypeId").val(selectRow.id);
			
			$('#editTypeRequestDiv').window({  
		           modal:true,
		           width:400,
		           height:150,
		           left:100,
		           top:100
		    });
			
			$('#editTypeRequestDiv').window('open');
		}
		
	}
	
	function saveRequestType(hiddenIdObj,formId){
		if(!$('#'+formId).form('validate')){
			return;
		}
		var requestTypeId = "";
		var requestTypeDescription = $("#addTypeRequestDiv input[id=description]").val();
		if(hiddenIdObj!=""){
			requestTypeId = $("#"+ hiddenIdObj).val();
			requestTypeDescription = $("#editTypeRequestDiv input[id=description]").val();
		}
		$.post("${pageContext.request.contextPath}/adminResourceMgr!addOrUpdateRequestType",{requestTypeId:requestTypeId,requestTypeDescription:requestTypeDescription},function(data){
			parent.$.messager.alert('<s:text name="REQUEST_TYPE_STR10"></s:text>',data);
			$("#tt").datagrid("unselectAll");
			$("#tt").datagrid("uncheckAll");
			$("#tt").datagrid("reload");
		});
	}
	
	function delTypeRequest(){
		var selectRow = $('#tt').datagrid("getSelected");
		if(selectRow!=null){
			parent.$.messager.confirm('<s:text name="REQUEST_TYPE_STR11"></s:text>','<s:text name="REQUEST_TYPE_STR12"></s:text>？',function(r){
				if (r){
					$.post("${pageContext.request.contextPath}/adminResourceMgr!delRequestType",{requestTypeId:selectRow.id},function(data){
						parent.$.messager.alert('<s:text name="REQUEST_TYPE_STR10"></s:text>',data);
						$("#tt").datagrid("unselectAll");
						$("#tt").datagrid("uncheckAll");
						$("#tt").datagrid("reload");
					});
				}
			});
		}
	}
	
	function addSubTypeRequest(){
		var selectRow = $('#tt').datagrid("getSelected");
		if(selectRow!=null){
			$("#parentRequestTypeId").val(selectRow.id);
			$('#addSubTypeRequestDiv').window({  
		           modal:true,
		           width:400,
		           height:150,
		           left:100,
		           top:100
		    });
			
			$('#addSubTypeRequestDiv').window('open');
		}
		else{
			parent.$.messager.alert('<s:text name="REQUEST_TYPE_STR10"></s:text>',"<s:text name='REQUEST_TYPE_STR13'></s:text>");
		}
	}
	
	function saveSubRequestType(){
		if(!$('#addSubTypeRequestForm').validate()){
			return;
		}
		
		$.post("${pageContext.request.contextPath}/adminResourceMgr!saveSubRequestType",{requestTypeId:$("#parentRequestTypeId").val(),requestTypeDescription:$("#addSubTypeRequestDiv input[id=description]").val()},function(data){
			parent.$.messager.alert('<s:text name="REQUEST_TYPE_STR10"></s:text>',data);
			$("#tt").datagrid("unselectAll");
			$("#tt").datagrid("uncheckAll");
			$("#tt").datagrid("reload");
		});
	}
	
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">		
	<div class="conditionDiv">
		<a id="btnClear" href="javascript:void(0)" class="easyui-linkbutton" onclick="addTypeRequest();"><s:text name='REQUEST_TYPE_STR1'></s:text></a>
		<a id="btnClear" href="javascript:void(0)" class="easyui-linkbutton" onclick="editTypeRequest();"><s:text name='REQUEST_TYPE_STR2'></s:text></a>
		<a id="btnClear" href="javascript:void(0)" class="easyui-linkbutton" onclick="delTypeRequest();"><s:text name='REQUEST_TYPE_STR3'></s:text></a>
		<a id="btnClear" href="javascript:void(0)" class="easyui-linkbutton" onclick="addSubTypeRequest();"><s:text name='REQUEST_TYPE_STR4'></s:text></a>
	</div>
	<div class="conditionDiv">
		<table id="tt"></table>
	</div>
	<div id="addTypeRequestDiv" class="easyui-window" closed="true" title="<s:text name='REQUEST_TYPE_STR1'></s:text>" style="padding:20px">
	   		<form id="addTypeRequestForm">
		   		<table width="100%" border="0" cellpadding="0" cellspacing="5">
		   			<tr>
		   				<td width="30%"><s:text name='REQUEST_TYPE_STR7'></s:text>：</td>
		   				<td><input id="description" type="text" class="mydatetext easyui-validatebox" required="true"></td>
		   			</tr> 
		   		</table>
	   		</form>
	   		<div style="padding:5px;text-align:center;">  
	           	<a href="javascript:void(0);" id="updateButton" class="easyui-linkbutton" icon="icon-ok" onclick="saveRequestType('','addTypeRequestForm');"><s:text name='REQUEST_TYPE_STR8'></s:text></a>  
	           	<a href="javascript:void(0);" id="cancleButton" class="easyui-linkbutton" icon="icon-cancel" onclick="cancleWindow('addTypeRequestDiv');"><s:text name='REQUEST_TYPE_STR9'></s:text></a>  
	       	</div>
	</div>
	<div id="editTypeRequestDiv" class="easyui-window" closed="true" title="<s:text name='REQUEST_TYPE_STR14'></s:text>" style="padding:20px">
	   		<form id="editTypeRequestForm">
		   		<input type="hidden" id="requestTypeId" value="">
		   		<table width="100%" border="0" cellpadding="0" cellspacing="5">
		   			<tr>
		   				<td width="30%"><s:text name='REQUEST_TYPE_STR7'></s:text>：</td>
		   				<td><input id="description" type="text" class="mydatetext easyui-validatebox" required="true"></td>
		   			</tr>
		   		</table>
	       	</form>
	   		<div style="padding:5px;text-align:center;">  
	           	<a href="javascript:void(0);" id="updateButton" class="easyui-linkbutton" icon="icon-ok" onclick="saveRequestType('requestTypeId','editTypeRequestForm');"><s:text name='REQUEST_TYPE_STR8'></s:text></a>  
	           	<a href="javascript:void(0);" id="cancleButton" class="easyui-linkbutton" icon="icon-cancel" onclick="cancleWindow('editTypeRequestDiv');"><s:text name='REQUEST_TYPE_STR9'></s:text></a>  
	       	</div>
	</div>
	<div id="addSubTypeRequestDiv" class="easyui-window" closed="true" title="<s:text name='REQUEST_TYPE_STR4'></s:text>" style="padding:20px">
	   		<form id="addSubTypeRequestForm">
		   		<input type="hidden" id="parentRequestTypeId" value="">
		   		<table width="100%" border="0" cellpadding="0" cellspacing="5">
		   			<tr>
		   				<td width="30%"><s:text name='REQUEST_TYPE_STR7'></s:text>：</td>
		   				<td><input id="description" type="text" class="mydatetext easyui-validatebox" required="true"></td>
		   			</tr>
		   		</table>
	   		</form>
	   		<div style="padding:5px;text-align:center;">  
	           	<a href="javascript:void(0);" id="updateButton" class="easyui-linkbutton" icon="icon-ok" onclick="saveSubRequestType();"><s:text name='REQUEST_TYPE_STR8'></s:text></a>  
	           	<a href="javascript:void(0);" id="cancleButton" class="easyui-linkbutton" icon="icon-cancel" onclick="cancleWindow('addSubTypeRequestDiv');"><s:text name='REQUEST_TYPE_STR9'></s:text></a>  
	       	</div>
	</div>
</div>
</body>
</html>