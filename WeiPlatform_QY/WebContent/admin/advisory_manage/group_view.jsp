<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE HTML>
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
		initGroupGrid();
	});
	
	function initGroupGrid() {
		$('#tt_group').datagrid({ 
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
	        iconCls:'icon-save',
		    url:'${pageContext.request.contextPath}/adviserMgr!getGroup',  
		    columns:[[
				{field:'id',hidden:true},		        
		        {field:'name',title:'<s:text name="GROUP_VIEW_STR6"></s:text> ',width:150,align:'left'},  
		        {field:'leaderID',hidden:true}, 
		        {field:'leader',title:'LEADER',width:80,align:'center'}, 
		        {field:'create_date',title:'<s:text name="USER_ROLE_VIEW_STR20"></s:text>',width:200,align:'center'},  
		        {field:'groupdesc',title:'<s:text name="GROUP_VIEW_STR8"></s:text>',width:300,align:'left'}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]
		});
	}
	
	// 添加组
	function addGroup() {
		$('#addGroupDiv').window({
    		href:'add_group.jsp', 
            modal:true,
            cache:false,
            width:350,
            height:350,
            left:100,
            top:100,
            onLoad:function(){
            	
            }
        });
   		$('#addGroupDiv').window('open');
	}
	
	// 编辑组
	function editGroup() {
		var rows = $('#tt_group').datagrid("getSelections");
		if (rows.length < 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR21"></s:text>!');
			return;
		}
		if (rows.length > 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR22"></s:text>!');
			return;
		}
		$('#addGroupDiv').window({
    		href:'add_group.jsp', 
            modal:true,
            cache:false,
            width:350,
            height:350,
            left:100,
            top:100,
            onLoad:function(){            	
            	$("#action_no").val("2");
            	$("#id").val(rows[0].id);
            	$("#name").val(rows[0].name);
            	//$("#leader").val(rows[0].leader);
            	$('#leader').combobox("setValue",rows[0].leaderID );
            	$("#groupdesc").val(rows[0].groupdesc); 
            }
        });
   		$('#addGroupDiv').window('open');
	}
	
	// 删除组
	function delGroup() {
		var rows = $('#tt_group').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="ADVISER_VIEW_STR3"></s:text>!');
			return;
		}
		$.messager.confirm('<s:text name="WARNING"></s:text>','<s:text name="PLATFORM_USERS_VIEW_STR16"></s:text> ?',
			function(r){
				if(r){
					var ids = "";		
					for(var i=0; i<rows.length; i++){
						ids = ids + rows[i].id + ',';
					}
					$.post("<%=request.getContextPath()%>/adviserMgr!groupAction",
			        	{        		
			        		// 项产品编码
			        		"groupEntity.id" : ids,
			        		// 操作号[1:新增,2:修改,3:注销]
			        		"groupEntity.action_no" : 3
			        	},
			        	function(data){			        		
			        		$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
			        		$('#tt_group').datagrid("clearSelections");
			        		$('#tt_group').datagrid("reload");
			        	},
			        	"json"
			        );
				}
			}
		);
	}
	
	// 将用户关联到组
	function assignUserToGroup() {
		var rows = $('#tt_group').datagrid("getSelections");
		if (rows.length < 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="GROUP_VIEW_STR12"></s:text>!');
			return;
		}
		if (rows.length > 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="USER_ROLE_VIEW_STR22"></s:text>!');
			return;
		}
		$('#assignGroupDiv').window({
    		href:'assign_group.jsp', 
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
   		$('#assignGroupDiv').window('open');
		
	}
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">		
	<div class="conditionDiv">
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="addGroup();"><s:text name="GROUP_VIEW_STR1"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="editGroup();"><s:text name="GROUP_VIEW_STR2"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="delGroup();"><s:text name="GROUP_VIEW_STR3"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="assignUserToGroup();"><s:text name="GROUP_VIEW_STR4"></s:text></a>
	</div>	
	<table id="tt_group"></table>
	<div id="addGroupDiv" class="easyui-window" closed="true" title="<s:text name="GROUP_VIEW_STR5"></s:text>" style="padding:20px" collapsible="false" minimizable="false" maximizable="false"></div>
	<div id="assignGroupDiv" class="easyui-window" closed="true" title="<s:text name="GROUP_VIEW_STR9"></s:text>" style="padding:20px" collapsible="false" minimizable="false" maximizable="false"></div>
</div>	
</body>
</html>