<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">
	$(function() {
		$.post("adviserMgr!getAdviserInfo",
	            { params: document.getElementById("hidden_id").value },
	            function(data){
	            	//加载信息
	            	loadAdviserInfo(data);
	            },
	            "json"
	    );
	});
	
	// 保存
	function updateAdviser(){
		if(!$('#update_password_params').form('validate')){
			return;
		}
		var newPsw = document.getElementById("update_password").value; 
	    var cfmPsw = document.getElementById("confirm_password").value; 
	    if(newPsw != cfmPsw){ 
	    	alert('<s:text name="ADVISER_VIEW_STR6"></s:text>！'); 
	        return; 
	    } 
		var params = JSON.stringify(serializeObject($("#update_password_params")));
		$.post("<%=request.getContextPath()%>/adviserMgr!adviserAction",
        	{
				req_params : params
        	},
        	function(data){
        		$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
        		$('#tt_adviser').datagrid("clearSelections");
        		cancleWindow('updatePassDiv');
        		$('#tt_adviser').datagrid("reload");
        	},
        	"json"
        ); 
	}
</script>
<script type="text/javascript">
	function loadAdviserInfo(data) {
		//登录账号
		document.getElementById("edit_account").value = data.account;
	}
</script>
<div style="padding: 5px;">
	<form id="update_password_params">
		<input type="hidden" id="action_no" name="action_no" value="4"/>
		<input type="hidden" id="edit_account" name="edit_account" />
		<table width="250px" border="0" cellpadding="0" cellspacing="5">
			<tr>
				<td><label><s:text name="ADVISER_VIEW_STR7"></s:text> </label></td>
				<td><input type="password" id="update_password" name="update_password" class="easyui-validatebox" required="true"/></td>
			</tr>
			<tr>
			    <td><label><s:text name="ADVISER_VIEW_STR8"></s:text> </label></td>
				<td><input type="password" id="confirm_password" name="confirm_password" class="easyui-validatebox" required="true"/></td>
			</tr>
		</table>
	</form>
	<div style="padding-top:7px;text-align:center;">  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-ok" onclick="updateAdviser();"><s:text name="SAVE"></s:text></a>  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-cancel" onclick="cancleWindow('updatePassDiv');"><s:text name="CANCLE"></s:text></a>  
	</div>
</div>
