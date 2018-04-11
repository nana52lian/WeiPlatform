<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>资源管理</title>	
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">

<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
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
<script type="text/javascript">
	function delResources() {
		var rows = $('#tt').datagrid("getSelections");	//获取你选择的所有行
		var ids = "";
		//var ids = document.getElementById("ids");
		for(var i=0; i<rows.length; i++){
			ids = ids + rows[i].id + ',';
			//var index = $('#tt').datagrid('getRowIndex', rows[i]);//获取某行的行号			
			//$('#tt').datagrid('deleteRow',index);	//通过行号移除该行
		}
		document.getElementById("ids").value = ids;	
		var objForm = document.getElementById("frm_resList");
		objForm.submit();
	}
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<form action="adminResourceMgr!addResource" method="post">
		<table>
			<tbody>
				<tr>	
					<td width="100px;" align="left">资源名称:</td>
					<td align="left"><input type="input" name="resource_name" /> </td>
				</tr>
				<tr>	
					<td width="100px;" align="left">上一层:</td>
					<td align="left">
						<input id="pid" name="pid" class="easyui-combobox"
						url="${pageContext.request.contextPath}/adminResourceMgr!getParent" 						
						valueField="id" textField="resource_name" panelHeight="auto" >
					</td>
				</tr>
				<tr>	
					<td>类型:</td>
					<td align="left">
						<select name="resource_type">
							<option value="button">按钮</option>
							<option value="url">超链接</option>
							<option value="module">模块</option>							
							<option value="method">方法</option>
						</select>
					</td>
				</tr>
				<tr>	
					<td>排序:</td>
					<td align="left"><input type="input" name="resource_priority" /></td>
				</tr>
				<tr>	
					<td>内容:</td>
					<td align="left"><input type="input" name="resource_string" /></td>
				</tr>
				<tr>	
					<td>描述:</td>
					<td align="left">
						<textarea rows="5" cols="20" name="resource_desc"></textarea>
					</td>
				</tr>
				<tr>	
					<td></td>
					<td align="left">
						<input type="submit" value="Save" title="保存" />
					</td>
				</tr>
			</tbody>
		</table>
	</form>

	<form id="frm_resList" name="frm_resList" action="adminResourceMgr!delResource" method="post">
		<input type="hidden" name="ids" id="ids" />
		<table id="tt" class="easyui-datagrid" style="width:800px;height:415px"  
	        url="${pageContext.request.contextPath}/adminResourceMgr!getResources" 
	        iconCls="icon-save" pagination="true" toolbar="#tb" idField="id" rownumbers="true">  
		    <thead>  
		        <tr>  
		        	<th field="ck" checkbox="true"></th>
		        	<th field="id" width="245">id</th>
		            <th field="resource_name" width="80">资源名称</th>  
		            <th field="resource_type" width="80">类型</th>
		            <th field="resource_priority" width="80">排序</th>		            
		            <th field="resource_string" width="200">内容</th>  
		            <th field="resource_desc" width="160">描述</th>  
		            <th field="pid" width="245" align="center">上一层</th>
		        </tr>
		    </thead>  
		</table>  
		<div id="tb">  
		    <a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="JavaScript:delResources();">删除</a>  
		</div>  
	</form>
</div>
</body>
</html>