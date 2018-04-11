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
		initCategoriesGrid();
	});
	
	function initCategoriesGrid() {
		$('#tt_category').datagrid({ 
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
	        iconCls:'icon-save',
		    url:'${pageContext.request.contextPath}/articleMgr!loadCategories',  
		    columns:[[
				{field:'id',hidden:true},
				{field:'section_id',hidden:true},
				{field:'name',title:'<s:text name="categories_mgr_str5"></s:text>',width:350,align:'center'},
				{field:'section_name',title:'<s:text name="categories_mgr_str6"></s:text>',width:150,align:'center'},
		        {field:'create_date',title:'<s:text name="categories_mgr_str7"></s:text> ',width:150,align:'center'},  
		        
		    ]] ,
		    pagination:true,
		    pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]
		});
	}
	
	// 创建子类
	function addCategory() {
		$('#categoryDiv').window({
    		href:'category_info.jsp', 
            modal:true,
            cache:false,
            width:310,
            height:190,
            left:100,
            top:100,
            onLoad:function(){
            	
            }
        });
   		$('#categoryDiv').window('open');
	}
	
	// 编辑分类
	function editCategory() {
		var rows = $('#tt_category').datagrid("getSelections");
		if (rows.length < 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="REQUISITION_MANAGE_STR3"></s:text>!');
			return;
		}		
		$('#categoryDiv').window({
    		href:'category_info.jsp', 
            modal:true,
            cache:false,
            width:310,
            height:190,
            left:100,
            top:100,
            onLoad:function(){
            	$("#action_no").val(2);
            	$("#id").val(rows[0].id);
            	$('#section').combobox("setValue", rows[0].section_id);
            	$("#name").val(rows[0].name);            	
            }
        });
   		$('#categoryDiv').window('open');
	}
	
	// 删除管理账户
	function delCategory() {
		var rows = $('#tt_category').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="REQUISITION_MANAGE_STR3"></s:text>!');
			return;
		}
		$.messager.confirm('<s:text name="WARNING"></s:text>','<s:text name="PLATFORM_USERS_VIEW_STR16"></s:text> ?',
			function(r){
				if(r){
					var tmp_code = "";
					for(var i =0;i<rows.length;i++) {
						tmp_code = tmp_code + rows[i].id;
						if (i < rows.length -1){
							tmp_code = tmp_code + ',';
						}
					}
					var params = '{"ids":"' + tmp_code + '", "action_no":"3"}';
					$.post("<%=request.getContextPath()%>/articleMgr!categoryAction",
			        	{
			        		req_params : params
			        	},
			        	function(data){
			        		$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
			        		$('#tt_category').datagrid("reload");
			        		$('#tt_category').datagrid("clearSelections");
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
	<div class="conditionDiv">
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="addCategory();"><s:text name="categories_mgr_str1"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="editCategory();"><s:text name="categories_mgr_str2"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="delCategory();"><s:text name="categories_mgr_str3"></s:text></a>
	</div>	
	<table id="tt_category"></table>
	<div id="categoryDiv" class="easyui-window" closed="true" title='<s:text name="categories_mgr_str4"></s:text>' style="padding:20px" collapsible="false" minimizable="false" maximizable="false"></div>
	
</div>	
</body>
</html>