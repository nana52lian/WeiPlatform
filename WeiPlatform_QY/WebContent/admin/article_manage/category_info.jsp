<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<script type="text/javascript">
	$(function(){  
		loadSections();
	});
	
	// 加载分类
	function loadSections() {
		$('#section').combobox({  
			url:'<%=request.getContextPath()%>/articleMgr!loadSections',  
	   		 valueField:'id',  
	   		 textField:'name'
		}); 
	}
	
	// 保存
	function saveCategory(){
		if(!$('#category_params').form('validate')) return;		
		var params = JSON.stringify(serializeObject($("#category_params")));		
		$.post("<%=request.getContextPath()%>/articleMgr!categoryAction",
        	{
				req_params : params
        	},
        	function(data){
        		$.messager.alert('信息', data.resDesc);
        		cancleWindow('categoryDiv');
        		$('#tt_category').datagrid("reload");
        		$('#tt_category').datagrid("clearSelections");
        	},
        	"json"
        ); 
	}
</script>
<div style="padding: 10px;">
	<form id="category_params">
		<input type="hidden" id="action_no" name="action_no" value="1"/>
		<input type="hidden" id="id" name="id" />
		<table width="100%" border="0" cellpadding="0" cellspacing="2">
			<tr>
				<td><label>所属分类 </label></td>
				<td><input id="section" name="section" class="easyui-combobox" panelHeight="auto" required="true"/></td>				
			</tr>
			<tr>
				<td><label>子类名称 </label></td>
				<td><input type="text" id="name" name="name" class="easyui-validatebox" required="true"/></td>				
			</tr>			
		</table>
	</form>
	<div style="padding:5px;text-align:center;">  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-ok" onclick="saveCategory();">保存</a>  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-cancel" onclick="cancleWindow('categoryDiv');">返回</a>  
	</div>
</div>
