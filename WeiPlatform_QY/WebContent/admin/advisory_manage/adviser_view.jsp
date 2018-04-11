<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE HTML>
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
		width:160px;
	}
</style>
<script>

	$(function() {
		initPlatformUserGrid();
		
	});	

	function initPlatformUserGrid() {		
		$('#tt_adviser').datagrid({ 
			title:'<s:text name="ADVISER_VIEW_STR1"></s:text>',
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
		    columns:[[  
		        {field:'id',title:'ID',width:280,align:'center'},
		        {field:'account',title:'<s:text name="ADVISER_VIEW_STR2"></s:text>',width:90,align:'center'},  
		        {field:'name',title:'<s:text name="SUBSCRIBER_VIEW_STR9"></s:text>',width:90,align:'center'},  
		        {field:'sex',title:'<s:text name="SUBSCRIBER_VIEW_STR2"></s:text>',width:60,align:'center',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='1')
		        		  statusText = '<s:text name="MAN"></s:text>';
		        	  else if(value=='0')
		        		  statusText = '<s:text name="WOMAN"></s:text>';
		        	  return statusText;
		        }},	
		        {field:'phone',title:'<s:text name="SUBSCRIBER_VIEW_STR7"></s:text>',width:100,align:'center'} 
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]			
		});  
	}
	
	// 查询咨询师
	function searchAdviser() {
		var conditions = JSON.stringify(sy.serializeObject($("#conditions")));	
		//alert(conditions);		
		$('#tt_adviser').datagrid({	        
	        url:'${pageContext.request.contextPath}/adviserMgr!queryAdviser',
			queryParams : {				
				req_params : conditions
			},
			onLoadSuccess:function(data) { }
		});
	}
	
	// 添加咨询师
	function addAdviser() {
		$('#addAdviserDiv').window({
    		href:'add_adviser.jsp', 
            modal:true,
            cache:false,
            width:640,
            height:300,
            left:20,
            top:20,
            onLoad:function(){
            	
            }
        });
   		$('#addAdviserDiv').window('open');
	}
	
	// 编辑咨询师
	function editAdviser() {
		var rows = $('#tt_adviser').datagrid("getSelections");
		if (rows.length < 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR21"></s:text>!');
			return;
		}
		if (rows.length > 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR22"></s:text>!');
			return;
		}
		document.getElementById("hidden_id").value = rows[0].id;
		$('#editAdviserDiv').window({
    		href:'edit_adviser.jsp', 
            modal:true,
            cache:false,
            width:640,
            height:300,
            left:20,
            top:20
        });
   		$('#editAdviserDiv').window('open');
	}
	
	// 更改密码
	function updatePass() {
		var rows = $('#tt_adviser').datagrid("getSelections");
		if (rows.length < 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR21"></s:text>!');
			return;
		}
		if (rows.length > 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR22"></s:text>!');
			return;
		}
		document.getElementById("hidden_id").value = rows[0].id;
		$('#updatePassDiv').window({
    		href:'update_password_adviser.jsp', 
            modal:true,
            cache:false,
            width:300,
            height:200,
            left:350,
            top:300
        });
   		$('#updatePassDiv').window('open');
	}
	
	// 删除咨询师
	function deleteAdviser() {
		var rows = $('#tt_adviser').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="ADVISER_VIEW_STR3"></s:text>!');
			return;
		}
		$.messager.confirm('<s:text name="WARNING"></s:text>','<s:text name="PLATFORM_USERS_VIEW_STR16"></s:text> ?',
			function(r){
				if(r){
					var tmp_code = "";
					var tmp_account = "";
					for(var i =0;i<rows.length;i++) {
						tmp_code = tmp_code + rows[i].id;
						tmp_account = tmp_account + rows[i].account;
						if (i < rows.length -1){
							tmp_code = tmp_code + ',';
							tmp_account = tmp_account + ',';
						}
					}
					var params = '{"id":"' + tmp_code + '", "account":"' + tmp_account + '","action_no":"3"}';
					$.post("<%=request.getContextPath()%>/adviserMgr!adviserAction",
			        	{        		
			        		req_params : params
			        	},
			        	function(data){
			        		$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
			        		$('#tt_adviser').datagrid("clearSelections");
			        		$('#tt_adviser').datagrid("reload");
			        	},
			        	"json"
			        );
				}
			}
		);
	}
	
	//清空
	function clearConditions(){
		document.getElementById("name").value = "";
		//$('#sex').find("option[text='所有']").attr("selected",true);
		$('#sex').val("0");
	}
	
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<input type="hidden" id="hidden_id" name="hidden_id" />
	<div class="topDiv">
		<form id="conditions">
			<div class="conditionDiv">
				<label><s:text name="PLATFORM_USERS_VIEW_STR2"></s:text>：</label> 
				<input id="name" name="name" type="text" class="mytext" />
		     	&nbsp;&nbsp;
		     	<label><s:text name="SUBSCRIBER_VIEW_STR2"></s:text>：</label>
		     	<select id="sex" name="sex" class="sex" panelHeight="auto" style="width:60px;">  
				    <option value="2"><s:text name="ALL"></s:text></option>
				    <option value="1"><s:text name="MAN"></s:text></option>  
				    <option value="0"><s:text name="WOMAN"></s:text></option>  
				</select>
				&nbsp;&nbsp;
				<label><s:text name="ADVISER_VIEW_STR2"></s:text>：</label> 
				<input id="account" name="account" type="text" class="mytext" />
	     	</div>		
		</form>
		<div style="padding-right:10px;text-align:right;">  
			<a id="btnClear" href="#" class="easyui-linkbutton" onclick="clearConditions();"><s:text name="CLEAR"></s:text></a>
			<a id="btnQuery" href="javascript:void(0);" class="easyui-linkbutton" onclick="searchAdviser();"><s:text name="SEARCH"></s:text></a>
		</div>
	</div>	
	<div class="conditionDiv">
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="addAdviser();"><s:text name="CREATE"></s:text></a>
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="editAdviser();"><s:text name="EDIT"></s:text></a>
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="updatePass();"><s:text name="ADVISER_VIEW_STR4"></s:text></a>
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="deleteAdviser();"><s:text name="DELETE"></s:text></a>		
	</div>	
	<table id="tt_adviser"></table>
	<div id="addAdviserDiv" class="easyui-window" closed="true" title="<s:text name="ADVISER_VIEW_STR5"></s:text>" style="padding:20px" collapsible="false" minimizable="false" maximizable="false"></div>
	<div id="editAdviserDiv" class="easyui-window" closed="true" title="<s:text name="ADVISER_VIEW_STR5"></s:text>" style="padding:20px" collapsible="false" minimizable="false" maximizable="false"></div>
	<div id="updatePassDiv" class="easyui-window" closed="true" title="<s:text name="ADVISER_VIEW_STR4"></s:text>" style="padding:20px" collapsible="false" minimizable="false" maximizable="false"></div>
</div>	
</body>
</html>