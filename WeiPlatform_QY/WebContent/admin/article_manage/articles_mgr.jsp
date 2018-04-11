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
		initArticleGrid();
		loadSections();
	});
	
	function initArticleGrid() {
		$('#tt_article').datagrid({ 
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
	        iconCls:'icon-save',
		    url:'${pageContext.request.contextPath}/articleMgr!loadArticles',  
		    columns:[[
				{field:'id',hidden:true},		        
		        {field:'title',title:'<s:text name="articles_mgr_str8"></s:text> ',width:260,align:'left'},  
		        {field:'writer',title:'<s:text name="articles_mgr_str9"></s:text>',width:100,align:'center'}, 
		        {field:'section',title:'<s:text name="articles_mgr_str10"></s:text> ',width:180,align:'center'},  
		        {field:'category',title:'<s:text name="articles_mgr_str11"></s:text>',width:250,align:'left'},
		        {field:'status',title:'<s:text name="articles_mgr_str12"></s:text>',width:80,align:'left',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='1')
		        		  statusText = '<s:text name="articles_mgr_str13"></s:text>';
		        	  else if(value=='0')
		        		  statusText = '<s:text name="articles_mgr_str14"></s:text>';
		        	  return statusText;
		        }},	
		        {field:'create_date',title:'<s:text name="articles_mgr_str15"></s:text>',width:150,align:'left'}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]
		});
	}
	
	function loadSections() {
		$('#section').combobox({  
			url:'<%=request.getContextPath()%>/articleMgr!loadSections',  
			valueField:'id',  
	   		textField:'name',
	   		onSelect:function(record) {
	   			loadCategories(record.id);
	   		}
		}); 
	}
	
	function loadCategories(id) {
		$('#category').combobox({  
			url:'<%=request.getContextPath()%>/articleMgr!loadCategoriesBySection?section=' + id,  
			valueField:'id',  
	   		textField:'name'
		}); 
	}
	
	function searchArticles() {		
		$('#tt_article').datagrid({	        
	        url:'${pageContext.request.contextPath}/articleMgr!loadArticles',
			queryParams : {				
				section :  $('#section').combobox("getValue"),
				category : $('#category').combobox("getValue")
			},
			onLoadSuccess:function(data) { }
		});
	}
	
	function addArticle() {
		parent.addTabPane('B21','<s:text name="articles_mgr_str16"></s:text>','${pageContext.request.contextPath}/admin/article_manage/article.jsp');
	}
	
	function editArticle() {
		var rows = $('#tt_article').datagrid("getSelections");
		if (rows.length < 1) {
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="REQUISITION_MANAGE_STR3"></s:text>!');
			return;
		}
		parent.addTabPane('B21','<s:text name="articles_mgr_str16"></s:text>','${pageContext.request.contextPath}/admin/article_manage/article.jsp?id=' + rows[0].id);
	}
	
	function delArticle() {
		var rows = $('#tt_article').datagrid("getSelections");
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
					$.post("<%=request.getContextPath()%>/articleMgr!articleAction",
			        	{
			        		req_params : params
			        	},
			        	function(data){
			        		$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
			        		$('#tt_article').datagrid("reload");
			        		$('#tt_article').datagrid("clearSelections");
			        	},
			        	"json"
			        );
				}
			}
		);
	}
	
	function publishArticle(status) {
		var rows = $('#tt_article').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="REQUISITION_MANAGE_STR3"></s:text>!');
			return;
		}
		var tmp_code = "";
		for(var i =0;i<rows.length;i++) {
			tmp_code = tmp_code + rows[i].id;
			if (i < rows.length -1){
				tmp_code = tmp_code + ',';
			}
		}
		var params = '{"ids":"' + tmp_code + '","action_no":"4","status":' + status + '}';
		$.post("<%=request.getContextPath()%>/articleMgr!articleAction",
        	{
        		req_params : params
        	},
        	function(data){
        		$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
        		$('#tt_article').datagrid("reload");
        		$('#tt_article').datagrid("clearSelections");
        	},
        	"json"
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
				<label><s:text name="articles_mgr_str6"></s:text>：</label> 
				<input id="section" name="section" class="easyui-combobox" panelHeight="auto" />
		     	&nbsp;&nbsp;
		     	<label><s:text name="articles_mgr_str7"></s:text>：</label> 
				<input id="category" name="category" class="easyui-combobox" panelHeight="auto" />
		     	&nbsp;&nbsp;		     	
	     	</div>		
		</form>		
		<div style="padding:5px;text-align:right;">  
			<a id="btnQuery" href="javascript:void(0);" class="easyui-linkbutton" onclick="searchArticles();"><s:text name="SEARCH"></s:text></a>
		</div>
	</div>	
	<div class="conditionDiv">
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="addArticle();"><s:text name="articles_mgr_str1"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="editArticle();"><s:text name="articles_mgr_str2"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="delArticle();"><s:text name="articles_mgr_str3"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="publishArticle(1);"><s:text name="articles_mgr_str4"></s:text></a>
		<a id="btnClear" href="#" class="easyui-linkbutton" onclick="publishArticle(0);"><s:text name="articles_mgr_str5"></s:text></a>		
	</div>	
	<table id="tt_article"></table>
	
</div>	
</body>
</html>