<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="VIEW_PLATFORM_USER"></s:text></title>	
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
		width:100px;
	}
</style>
<script>

	$(function() {
		initPlatformUserGrid();
		
	});	

	function initPlatformUserGrid() {		
		$('#tt_platformUser').datagrid({ 
			title:'<s:text name="PLATFORM_USERS_VIEW_STR10"></s:text>',
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
		        {field:'account',title:'<s:text name="PLATFORM_USERS_VIEW_STR1"></s:text>',width:120,align:'center'},  
		        {field:'name',title:'<s:text name="PLATFORM_USERS_VIEW_STR2"></s:text>',width:100,align:'center'}, 
		        {field:'mobile_phone',title:'<s:text name="PLATFORM_USERS_VIEW_STR3"></s:text>',width:100,align:'center'},  
		        {field:'mail',title:'<s:text name="PLATFORM_USERS_VIEW_STR13"></s:text>',width:150,align:'center'},  
		        {field:'enabled',title:'<s:text name="PLATFORM_USERS_VIEW_STR4"></s:text>',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='1')
		        		  statusText = '<s:text name="PLATFORM_USERS_VIEW_STR8"></s:text>';
		        	  else if(value=='0')
		        		  statusText = '<s:text name="PLATFORM_USERS_VIEW_STR7"></s:text>';
		        	  return statusText;
		        }},		        
		        {field:'create_date',title:'<s:text name="PLATFORM_USERS_VIEW_STR19"></s:text>',width:150,align:'center'}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]			
		});  
	}
	
	// 查询管理账户
	function searchPlatformUsers() {
		var conditions = JSON.stringify(sy.serializeObject($("#conditions")));	
		//alert(conditions);		
		$('#tt_platformUser').datagrid({	        
	        url:'${pageContext.request.contextPath}/adminPlatformUserMgr!queryPlatformUsers',
			queryParams : {				
				req_params : conditions
			},
			onLoadSuccess:function(data) { }
		});
	}
	
	// 添加管理账户
	function addPlatformUser() {
		$('#addPlatformDiv').window({
    		href:'add_platform_user.jsp', 
            modal:true,
            cache:false,
            width:550,
            height:220,
            left:100,
            top:100,
            onLoad:function(){
            	
            }
        });
   		$('#addPlatformDiv').window('open');
	}
	
	// 编辑管理账户
	function editPlatformUser() {
		var rows = $('#tt_platformUser').datagrid("getSelections");
		if (rows.length < 1) {
			$.messager.alert('<s:text name="PLATFORM_USERS_VIEW_STR20"></s:text>','<s:text name="PLATFORM_USERS_VIEW_STR21"></s:text>!');
			return;
		}
		if (rows.length > 1) {
			$.messager.alert('<s:text name="PLATFORM_USERS_VIEW_STR20"></s:text>','<s:text name="PLATFORM_USERS_VIEW_STR22"></s:text>!');
			return;
		}
		$('#addPlatformDiv').window({
    		href:'add_platform_user.jsp', 
            modal:true,
            cache:false,
            width:550,
            height:230,
            left:100,
            top:100,
            onLoad:function(){
            	$("#action_no").val(2);
            	$("#id").val(rows[0].id);
            	$("#account").val(rows[0].account);
            	$("#name").val(rows[0].name);
            	$("#mobile_phone").val(rows[0].mobile_phone);
            	$("#mail").val(rows[0].mail);
            }
        });
   		$('#addPlatformDiv').window('open');
	}
	
	// 删除管理账户
	function deletePlatformUser() {
		var rows = $('#tt_platformUser').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="PLATFORM_USERS_VIEW_STR20"></s:text>','<s:text name="PLATFORM_USERS_VIEW_STR23"></s:text>!');
			return;
		}
		$.messager.confirm('<s:text name="PLATFORM_USERS_VIEW_STR20"></s:text>','<s:text name="PLATFORM_USERS_VIEW_STR24"></s:text>?',
			function(r){
				if(r){
					var tmp_code = "";
					for(var i =0;i<rows.length;i++) {
						tmp_code = tmp_code + rows[i].id;
						if (i < rows.length -1){
							tmp_code = tmp_code + ',';
						}
					}
					var params = '{"id":"' + tmp_code + '", "action_no":"3"}';
					$.post("<%=request.getContextPath()%>/adminPlatformUserMgr!platformUserAction",
			        	{        		
			        		// 项产品编码
			        		//"id" : tmp_code,
			        		// 操作号[1:新增,2:修改,3:注销]
			        		//"action_no" : 3
			        		req_params : params
			        	},
			        	function(data){
			        		$.messager.alert('<s:text name="PLATFORM_USERS_VIEW_STR25"></s:text>', data.resDesc);
			        		$('#tt_platformUser').datagrid("reload");
			        	},
			        	"json"
			        );
				}
			}
		);
	}
	
	//启用管理账号
	function updatePlatformUserForStart(){
		var rows = $('#tt_platformUser').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="PLATFORM_USERS_VIEW_STR20"></s:text>','<s:text name="PLATFORM_USERS_VIEW_STR26"></s:text>!');
			return;
		}
		$.messager.confirm('<s:text name="PLATFORM_USERS_VIEW_STR20"></s:text>','<s:text name="PLATFORM_USERS_VIEW_STR27"></s:text> ?',
			function(r){
				if(r){
					var tmp_code = "";
					for(var i =0;i<rows.length;i++) {
						tmp_code = tmp_code + rows[i].id;
						if (i < rows.length -1){
							tmp_code = tmp_code + ',';
						}
					}
					var params = '{"id":"' + tmp_code + '", "action_no":"4"}';
					$.post("<%=request.getContextPath()%>/adminPlatformUserMgr!platformUserAction",
			        	{        		
			        		req_params : params
			        	},
			        	function(data){
			        		$.messager.alert('<s:text name="PLATFORM_USERS_VIEW_STR25"></s:text>', data.resDesc);
			        		$('#tt_platformUser').datagrid("reload");
			        	},
			        	"json"
			        );
				}
			}
		);
	}
	
	//停止管理账号
	function updatePlatformUserForStop(){
		var rows = $('#tt_platformUser').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="PLATFORM_USERS_VIEW_STR20"></s:text>','<s:text name="PLATFORM_USERS_VIEW_STR28"></s:text>!');
			return;
		}
		$.messager.confirm('<s:text name="PLATFORM_USERS_VIEW_STR20"></s:text>','<s:text name="PLATFORM_USERS_VIEW_STR29"></s:text> ?',
			function(r){
				if(r){
					var tmp_code = "";
					for(var i =0;i<rows.length;i++) {
						tmp_code = tmp_code + rows[i].id;
						if (i < rows.length -1){
							tmp_code = tmp_code + ',';
						}
					}
					var params = '{"id":"' + tmp_code + '", "action_no":"5"}';
					$.post("<%=request.getContextPath()%>/adminPlatformUserMgr!platformUserAction",
			        	{        		
			        		req_params : params
			        	},
			        	function(data){
			        		$.messager.alert('<s:text name="PLATFORM_USERS_VIEW_STR25"></s:text>', data.resDesc);
			        		$('#tt_platformUser').datagrid("reload");
			        	},
			        	"json"
			        );
				}
			}
		);
	}	
	
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<div class="topDiv">
		<form id="conditions">
			<div class="conditionDiv">
				<label><s:text name="PLATFORM_USERS_VIEW_STR1"></s:text>：</label> 
				<input id="qry_account" name="qry_account" type="text" class="mytext" />
		     	&nbsp;&nbsp;
		     	<label><s:text name="PLATFORM_USERS_VIEW_STR2"></s:text>：</label> 
				<input id="qry_name" name="qry_name" type="text" class="mytext" />
		     	&nbsp;&nbsp;
		     	<label><s:text name="PLATFORM_USERS_VIEW_STR3"></s:text>：</label> 
				<input id="qry_mobile_phone" name="qry_mobile_phone" type="text" class="mytext" />
		     	&nbsp;&nbsp;
				<label><s:text name="PLATFORM_USERS_VIEW_STR4"></s:text>：</label> 
				<select id="qry_enabled" name="qry_enabled" class="mytext">
				    <option value="">-<s:text name="PLATFORM_USERS_VIEW_STR30"></s:text>-</option>
				    <option value="1"><s:text name="PLATFORM_USERS_VIEW_STR8"></s:text></option>
				    <option value="2"><s:text name="PLATFORM_USERS_VIEW_STR7"></s:text></option>
				</select>
				&nbsp;&nbsp;
	     	</div>		
		</form>
		
		<div style="padding:5px;text-align:right;">  
			<a id="btnQuery" href="javascript:void(0);" class="easyui-linkbutton" onclick="searchPlatformUsers();"><s:text name="PLATFORM_USERS_VIEW_STR31"></s:text></a>
		</div>
	</div>	
	<div class="conditionDiv">
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="addPlatformUser();"><s:text name="PLATFORM_USERS_VIEW_STR5"></s:text></a>
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="editPlatformUser();"><s:text name="PLATFORM_USERS_VIEW_STR6"></s:text></a>
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="updatePlatformUserForStop();"><s:text name="PLATFORM_USERS_VIEW_STR7"></s:text></a>
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="updatePlatformUserForStart();"><s:text name="PLATFORM_USERS_VIEW_STR8"></s:text></a>
		<!-- <a href="javascript:void(0);" class="easyui-linkbutton" onclick="deletePlatformUser();"><s:text name="PLATFORM_USERS_VIEW_STR9"></s:text></a>	 -->	
	</div>	
	<table id="tt_platformUser"></table>
	<div id="addPlatformDiv" class="easyui-window" closed="true" title="<s:text name="PLATFORM_USERS_VIEW_STR10"></s:text>" style="padding:20px" collapsible="false" minimizable="false" maximizable="false"></div>
</div>	
</body>
</html>