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
		initRoleGrid();
	});
	
	function initRoleGrid() {
		$('#tt_role').datagrid({ 
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
	        iconCls:'icon-save',
		    url:'${pageContext.request.contextPath}/adminRoleAuthsMgr!loadRoles',  
		    columns:[[
				{field:'id',hidden:true},		        
		        {field:'auth_name',title:'<s:text name="USER_ROLE_VIEW_STR6"></s:text>',width:150,align:'left'},  
		        {field:'status',title:'<s:text name="USER_ROLE_VIEW_STR19"></s:text>',width:80,align:'center'}, 
		        {field:'create_date',title:'<s:text name="USER_ROLE_VIEW_STR20"></s:text>',width:200,align:'center'},  
		        {field:'auth_desc',title:'<s:text name="USER_ROLE_VIEW_STR10"></s:text>',width:300,align:'left'}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]
		});
	}
	
	// 添加角色
	function addRole() {
		$('#addRoleDiv').window({
    		href:'add_role.jsp', 
            modal:true,
            cache:false,
            width:350,
            height:300,
            left:100,
            top:100,
            onLoad:function(){
            	
            }
        });
   		$('#addRoleDiv').window('open');
	}
	
	// 编辑角色
	function editRole() {
		var rows = $('#tt_role').datagrid("getSelections");
		if (rows.length < 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR21"></s:text>!');
			return;
		}
		if (rows.length > 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR22"></s:text>!');
			return;
		}
		$('#addRoleDiv').window({
    		href:'add_role.jsp', 
            modal:true,
            cache:false,
            width:350,
            height:300,
            left:100,
            top:100,
            onLoad:function(){            	
            	$("#action_no").val("2");
            	$("#id").val(rows[0].id);
            	$("#auth_name").val(rows[0].auth_name);
            	$("#auth_desc").val(rows[0].auth_desc); 
            }
        });
   		$('#addRoleDiv').window('open');
	}
	
	// 删除角色
	function delRole() {
		var rows = $('#tt_role').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR23"></s:text>!');
			return;
		}
		for(var i=0; i<rows.length; i++){
			if (rows[i].id == '18e7c3f3-6b86-4a44-a243-d2bca539c08c') {
				$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR25"></s:text>!');
				return;
			}
		}
		$.messager.confirm('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR24"></s:text> ?',
			function(r){
				if(r){
					var ids = "";		
					for(var i=0; i<rows.length; i++){
						ids = ids + rows[i].id + ',';
					}
					$.post("<%=request.getContextPath()%>/adminRoleAuthsMgr!roleAction",
			        	{        		
			        		// 项产品编码
			        		"authorityEntity.id" : ids,
			        		// 操作号[1:新增,2:修改,3:注销]
			        		"authorityEntity.action_no" : 3
			        	},
			        	function(data){			        		
			        		$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
			        		$('#tt_role').datagrid("clearSelections");
			        		$('#tt_role').datagrid("reload");
			        	},
			        	"json"
			        );
				}
			}
		); 
	}
	
	// 将用户关联到角色
	function assignRole() {
		var rows = $('#tt_role').datagrid("getSelections");
		if (rows.length < 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR26"></s:text>!');
			return;
		}
		if (rows.length > 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR22"></s:text>!');
			return;
		}
		$('#assignRoleDiv').window({
    		href:'assign_role.jsp', 
            modal:true,
            cache:false,
            width:900,
            height:500,
            left:100,
            top:100,
            onLoad:function(){            	
            	$('#auth_id').val(rows[0].id);
            	initAssignedUserGrid(rows[0].id);
            }
        });
   		$('#assignRoleDiv').window('open');
		
	}
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">		
	<div class="conditionDiv">
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="addRole();"><s:text name="USER_ROLE_VIEW_STR1"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="editRole();"><s:text name="USER_ROLE_VIEW_STR2"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="delRole();"><s:text name="USER_ROLE_VIEW_STR3"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="javascript:parent.addTabPane('X1','<s:text name="USER_ROLE_VIEW_STR4"></s:text>','${pageContext.request.contextPath}/admin/system_manage/assign_auths.jsp');"><s:text name="USER_ROLE_VIEW_STR4"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="assignRole();"><s:text name="USER_ROLE_VIEW_STR5"></s:text></a>
	</div>	
	<table id="tt_role"></table>
	<div id="addRoleDiv" class="easyui-window" closed="true" title="<s:text name="USER_ROLE_VIEW_STR8"></s:text>" style="padding:20px" collapsible="false" minimizable="false" maximizable="false"></div>
	<div id="assignRoleDiv" class="easyui-window" closed="true" title="<s:text name="USER_ROLE_VIEW_STR13"></s:text>" style="padding:20px" collapsible="false" minimizable="false" maximizable="false"></div>
</div>	
</body>
</html>