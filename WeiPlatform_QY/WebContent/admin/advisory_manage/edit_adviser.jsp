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
		if(!$('#edit_adviser_params').form('validate')){
			return;
		}
		var params = JSON.stringify(sy.serializeObject($("#edit_adviser_params")));
		$.post("<%=request.getContextPath()%>/adviserMgr!adviserAction",
        	{
				req_params : params
        	},
        	function(data){
        		$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
        		$('#tt_adviser').datagrid("clearSelections");
        		cancleWindow('editAdviserDiv');
        		$('#tt_adviser').datagrid("reload");
        	},
        	"json"
        ); 
	}
</script>
<script type="text/javascript">
	function loadAdviserInfo(data) {
		//GUID
		document.getElementById("edit_id").value = data.id;
		//姓名
		document.getElementById("edit_name").value = data.name;
		//登录账号
		document.getElementById("edit_account").value = data.account;
		//性别
		$("#edit_sex").combobox('setValue', data.sex);
		//联系电话
		document.getElementById("edit_phone").value = data.phone;
		//邮箱
		document.getElementById("edit_mail").value = data.mail;
		//全职/兼职
		if(data.type==2){
			$('input:radio[name=edit_type]:nth(0)').attr('checked',true);
		} else if (data.type==3){
			$('input:radio[name=edit_type]:nth(1)').attr('checked',true);
		}
		
		//CN/EN
		if(data.weichat_type=='az_cn'){
			$('input:radio[name=edit_weichat_type]:nth(0)').attr('checked',true);
		} else if (data.weichat_type=='az_en'){
			$('input:radio[name=edit_weichat_type]:nth(1)').attr('checked',true);
		}
		
		document.getElementById("edit_name").focus();
	}
</script>
<div style="padding: 5px;">
	<form id="edit_adviser_params">
		<input type="hidden" id="action_no" name="action_no" value="2"/>
		<input type="hidden" id="edit_id" name="edit_id" />
		<input type="hidden" id="account" name="account" />
		<table width="570px" border="0" cellpadding="0" cellspacing="5">
			<tr>
				<td><label><s:text name="SUBSCRIBER_VIEW_STR9"></s:text> </label></td>
				<td><input type="text" id="edit_name" name="edit_name" class="easyui-validatebox" required="true"/></td>
				<td><label><s:text name="ADVISER_VIEW_STR2"></s:text> </label></td>
				<td>
					<input type="text" id="edit_account" name="edit_account" class="easyui-validatebox" readonly="readonly"/>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="SUBSCRIBER_VIEW_STR2"></s:text> </label></td>
				<td>
					<select id="edit_sex" name="edit_sex" class="easyui-combobox" panelHeight="auto" style="width:60px;">  
					    <option value="1"><s:text name="MAN"></s:text></option>  
					    <option value="0"><s:text name="WOMAN"></s:text></option>  
					</select>
				</td>
				<td><label><s:text name="SUBSCRIBER_VIEW_STR7"></s:text></label></td>
				<td><input type="text" id="edit_phone" name="edit_phone" /></td>
			</tr>
			<tr>
				<td><label><s:text name="SUBSCRIBER_VIEW_STR8"></s:text> </label></td>
				<td><input type="text" id="edit_mail" name="edit_mail" class="easyui-validatebox" validType="email"/></td>
				<td><label><s:text name="ADVISER_VIEW_STR10"></s:text>/<s:text name="ADVISER_VIEW_STR11"></s:text></label></td>
				<td><input type="radio" name="edit_type" value="2" title="<s:text name='ADVISER_VIEW_STR10'></s:text>" /><s:text name="ADVISER_VIEW_STR10"></s:text><input type="radio" name="edit_type" value="3" title="<s:text name='ADVISER_VIEW_STR11'></s:text>"/><s:text name="ADVISER_VIEW_STR11"></s:text></td>
			</tr>
			<tr>
			    <td><label><s:text name="SUBSCRIBER_VIEW_STR28"></s:text></label></td>
			    <td><input type="radio" name="edit_weichat_type" value="az_cn" title="CN" />CN<input type="radio" name="edit_weichat_type" value="az_en" title="EN"/>EN</td>
			</tr>
		</table>
	</form>
	<div style="padding-top:7px;text-align:center;">  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-ok" onclick="updateAdviser();"><s:text name='SAVE'></s:text></a>  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-cancel" onclick="cancleWindow('editAdviserDiv');"><s:text name='CANCLE'></s:text></a>  
	</div>
</div>
