<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<script type="text/javascript">
	$(function(){  
		
	});
	
	// 保存
	function saveSection(){
		if(!$('#section_params').form('validate')) return;		
		var params = JSON.stringify(serializeObject($("#section_params")));		
		$.post("<%=request.getContextPath()%>/articleMgr!sectionAction",
        	{
				req_params : params
        	},
        	function(data){
        		$.messager.alert('信息', data.resDesc);
        		cancleWindow('sectionDiv');
        		$('#tt_section').datagrid("reload");
        	},
        	"json"
        ); 
	}
</script>
<div style="padding: 10px;">
	<form id="section_params">
		<input type="hidden" id="action_no" name="action_no" value="1"/>
		<input type="hidden" id="id" name="id" />
		<table width="100%" border="0" cellpadding="0" cellspacing="2">
			<tr>
				<td><label>分类编码 </label></td>
				<td><input type="text" id="code" name="code" class="easyui-validatebox" required="true"/></td>				
			</tr>
			<tr>
				<td><label>分类名称 </label></td>
				<td><input type="text" id="name" name="name" class="easyui-validatebox" required="true"/></td>				
			</tr>			
		</table>
	</form>
	<div style="padding:5px;text-align:center;">  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-ok" onclick="saveSection();">保存</a>  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-cancel" onclick="cancleWindow('sectionDiv');">返回</a>  
	</div>
</div>
