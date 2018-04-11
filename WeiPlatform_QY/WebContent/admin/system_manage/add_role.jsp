<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">


	//save role info
	function saveRole() {	
		if(!$('#roleForm').form('validate')){
			return;
		}
		
		$('#roleForm').ajaxSubmit({
			success:function (data) {				
				$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
				closeWindow('addRoleDiv');
				$('#tt_role').datagrid("reload");
			},
			dataType:'json'
		});
	}

</script>

<form style="padding:10px 20px 10px 40px;" name="roleForm" id="roleForm" action="${pageContext.request.contextPath}/adminRoleAuthsMgr!roleAction" method="post" >  
    <input type="hidden" id="action_no" name="authorityEntity.action_no" value="1" />
    <input type="hidden" id="id" name="authorityEntity.id" />
    <p><s:text name="USER_ROLE_VIEW_STR6"></s:text>:<input type="text" id="auth_name" name="authorityEntity.auth_name" class="easyui-validatebox" required="true"/></p>  
    <p><s:text name="USER_ROLE_VIEW_STR7"></s:text>:<textarea cols="19" rows="5" id="auth_desc" name="authorityEntity.auth_desc" class="easyui-validatebox" required="true"></textarea></p>
    <div style="padding:5px;text-align:center;">
        <a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="saveRole();"><s:text name="SAVE"></s:text></a>  
        <a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="closeWindow('addRoleDiv');"><s:text name="CANCLE"></s:text></a>  
    </div>  
</form>  