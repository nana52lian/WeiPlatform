<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">
	$(function(){  
		
	});
	
	// 保存
	function savePlatformUser(){
		if($("#password").val() != $("#c_password").val()){
			$.messager.alert('<s:text name="WARNING" ></s:text>','<s:text name="change_password_str2"></s:text>');
			return;
		}
		if(!$('#platform_user_params').form('validate')){
		   /* mobile : {// 验证手机号码
		        validator : function(value) {
		            return /^(13|15|18)\d{9}$/i.test(value);
		        },
		        message : '手机号码格式不正确'
		    }  */
			return;
		}

		var params = JSON.stringify(sy.serializeObject($("#platform_user_params")));
		$.post("<%=request.getContextPath()%>/adminPlatformUserMgr!platformUserAction",
        	{
				req_params : params
        	},
        	function(data){
        		$.messager.alert('<s:text name="INFO" ></s:text>', data.resDesc);
        		cancleWindow('addPlatformDiv');
        		$('#tt_platformUser').datagrid("reload");
        	},
        	"json"
        ); 
	}
</script>
<div style="padding: 10px;">
	<form id="platform_user_params">
		<input type="hidden" id="action_no" name="action_no" value="1"/>
		<input type="hidden" id="id" name="id" />
		<table width="500px" border="0" cellpadding="0" cellspacing="2">
			<tr>
				<td><label><s:text name="add_platform_user_str1" ></s:text> </label></td>
				<td><input type="text" id="account" name="account" class="easyui-validatebox" required="true"/></td>
				<td><label><s:text name="add_platform_user_str2" ></s:text></label></td>
				<td><input type="password" id="password" name="password" class="easyui-validatebox" required="true"/></td>
			</tr>
			<tr>
				<td><label><s:text name="add_platform_user_str3" ></s:text> </label></td>
				<td><input type="text" id="name" name="name" class="easyui-validatebox" required="true"/></td>
				<td><label><s:text name="add_platform_user_str4" ></s:text> </label></td>
				<td><input type="password" id="c_password" name="c_password" class="easyui-validatebox" required="true"/></td>
			</tr>
			<tr>
				<td><label><s:text name="add_platform_user_str5" ></s:text> </label></td>
				<td><input type="text" id="mobile_phone" name="mobile_phone" class="easyui-validatebox" required="true" /></td>
				<td><label><s:text name="add_platform_user_str6" ></s:text> </label></td>
				<td><input type="text" id="mail" name="mail" class="easyui-validatebox" required="true" validType="email"/></td>
			</tr>
		</table>
	</form>
	<div style="padding:5px;text-align:center;">  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-ok" onclick="savePlatformUser();"><s:text name="add_platform_user_str7" ></s:text></a>  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-cancel" onclick="cancleWindow('addPlatformDiv');"><s:text name="add_platform_user_str8" ></s:text></a>  
	</div>
</div>
