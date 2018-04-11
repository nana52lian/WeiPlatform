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
		initSectionGrid();
	});
	
	function initSectionGrid() {
		$('#tt_section').datagrid({ 
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
	        iconCls:'icon-save',
		    url:'${pageContext.request.contextPath}/articleMgr!loadSections',  
		    columns:[[
				{field:'id',hidden:true},		        
		        {field:'code',title:'<s:text name="section_mgr_str5"></s:text>',width:150,align:'left'},  
		        {field:'name',title:'<s:text name="section_mgr_str6"></s:text>',width:150,align:'center'}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]
		});
	}
	
	// 创建分类
	function addSection() {
		$('#sectionDiv').window({
    		href:'section_info.jsp', 
            modal:true,
            cache:false,
            width:310,
            height:190,
            left:100,
            top:100,
            onLoad:function(){
            	
            }
        });
   		$('#sectionDiv').window('open');
	}
	
	// 编辑分类
	function editSection() {
		var rows = $('#tt_section').datagrid("getSelections");
		if (rows.length < 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="REQUISITION_MANAGE_STR3"></s:text>!');
			return;
		}		
		$('#sectionDiv').window({
    		href:'section_info.jsp', 
            modal:true,
            cache:false,
            width:310,
            height:190,
            left:100,
            top:100,
            onLoad:function(){
            	$("#action_no").val(2);
            	$("#id").val(rows[0].id);            	
            	$("#code").val(rows[0].code);
            	$("#name").val(rows[0].name);            	
            }
        });
   		$('#sectionDiv').window('open');
	}
	
	// 删除管理账户
	function delSection() {
		var rows = $('#tt_section').datagrid("getSelections");
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
					$.post("<%=request.getContextPath()%>/articleMgr!sectionAction",
			        	{
			        		req_params : params
			        	},
			        	function(data){
			        		$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
			        		$('#tt_section').datagrid("reload");
			        		$('#tt_section').datagrid("clearSelections");
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
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="addSection();"><s:text name="section_mgr_str1"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="editSection();"><s:text name="section_mgr_str2"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="delSection();"><s:text name="section_mgr_str3"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="javascript:parent.addTabPane('B11','<s:text name="section_mgr_str4"></s:text>','${pageContext.request.contextPath}/admin/article_manage/categories_mgr.jsp');"><s:text name="section_mgr_str4"></s:text></a>
	</div>	
	<table id="tt_section"></table>
	<div id="sectionDiv" class="easyui-window" closed="true" title='<s:text name="section_mgr_str7"></s:text>' style="padding:20px" collapsible="false" minimizable="false" maximizable="false"></div>
		
</div>	
</body>
</html>