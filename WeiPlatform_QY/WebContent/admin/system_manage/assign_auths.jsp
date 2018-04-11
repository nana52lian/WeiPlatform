<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="assign_auths"></s:text></title>	
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
		$('#tt_roles').datagrid({ 
			//title:'角色',
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			singleSelect:true,
	        iconCls:'icon-save',
		    url:'${pageContext.request.contextPath}/adminRoleAuthsMgr!loadRoles',  
		    columns:[[
				{field:'id',hidden:true},		        
		        {field:'auth_name',title:'<s:text name="USER_ROLE_VIEW_STR6"></s:text>',width:150,align:'left'},		        
		        {field:'auth_desc',title:'<s:text name="USER_ROLE_VIEW_STR10"></s:text>',width:150,align:'left'}
		    ]],
		    onClickRow: function(rowIndex, rowData) {
		    	refreshTree(rowData.id);
		    }
		});
		
		$('#tt_auth').tree({
			url:'${pageContext.request.contextPath}/adminRoleAuthsMgr!loadAuthTree'			
		}); 
	});
	
	// refresh tree by role id
	function refreshTree(role_id) {
		// 加载数据
		$.post("${pageContext.request.contextPath}/adminRoleAuthsMgr!loadRoleAuthTree",
                { "role_id" : role_id },
                function(data){
                	$('#tt_auth').tree("loadData", data);                	
                },
                "json"
        );
	}
	
	// save role's auth
	function saveRoleAuth() {
		//$.blockUI({ message: '<h1><img src="${pageContext.request.contextPath}/images/loading.gif" /><s:text name="LOADING"></s:text>...</h1>' });
		var nodes = $('#tt_auth').tree('getChecked');		
		var ids = "";
		var parentIds = "";
		var parentNode = null;		
		for (var i=0; i<nodes.length; i++) {
			// 先加子节点ID
			if (ids.indexOf(nodes[i].id) == -1 && nodes[i].id != 0) {
				ids = ids + nodes[i].id + ",";
			}
			// 再加父节点ID
			parentNode = $('#tt_auth').tree('getParent', nodes[i].target);
			while(parentNode) {
				if (ids.indexOf(parentNode.id) == -1 && parentNode.id != 0) {
					ids = ids + parentNode.id + ",";
				}
				parentNode = $('#tt_auth').tree('getParent', parentNode.target);				
				if (parentNode) {
					if (parentNode.id == 0) {						
						break;
					}
				} else {					
					break;
				}	
			}
		}		
		// 保存数据
		var role_id = $('#tt_roles').datagrid('getSelected').id;		
		$.post("${pageContext.request.contextPath}/adminRoleAuthsMgr!saveRoleAuth",
                { "role_id" : role_id, "ids" : ids + parentIds},
                function(data){
                	//$('#tt_auth').tree("loadData", data);
                	$.unblockUI();
                	$.messager.alert('<s:text name="INFO"></s:text>', data.message);
                },
                "json"
        );		
	}
	
	function getCheckID(node) {
		var parentNode = $('#tt_auth').tree('getParent', node.target);
		if (parentNode && parentNode.id != 0) {
			return getCheckID(parentNode);
		} else {
			alert("check = " + node.id);
			return node.id;
		}
	}
	
	
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<div class="conditionDiv">
		<a id="btnClear" href="#" class="easyui-linkbutton" style="padding-left:10px;" onclick="saveRoleAuth();"><s:text name="SAVE"></s:text></a>		
	</div>
	<div style="float:left; width:800px; height:600px; padding: 0px; border:solid 0px red;">
		<div style="float:left;padding: 10px; width:350px;height:600px; border:solid 0px red;">			
			<div class="easyui-panel" title="<s:text name="USER_ROLE_VIEW_STR9"></s:text>" iconCls="" collapsible="false" style="padding:5px;width:350px;height:600px;">  
				<table id="tt_roles"></table>
			</div>
		</div>
		<div style="float:left;padding: 10px; width:350px;height:600px; border:solid 0px red;">
			<div class="easyui-panel" title="<s:text name="USER_ROLE_VIEW_STR11"></s:text>" iconCls="" collapsible="false" style="padding:5px;width:350px;height:600px;">  
				<ul id="tt_auth" class="easyui-tree" checkbox="true"></ul>
			</div>
		</div>
	</div>
	<div style="clear:both"></div>
	
</div>	
</body>
</html>