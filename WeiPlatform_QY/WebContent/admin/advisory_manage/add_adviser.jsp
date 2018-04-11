<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">
	$(function(){  
		document.getElementsByName('add_type')[0].checked=true;//默认选中专职
		
		document.getElementsByName('weichat_type')[0].checked=true;//默认选中中文
	}); 

	// 保存
	function saveAdviser(){
		if(!$('#add_adviser_params').form('validate')){
			return;
		}
		var params = JSON.stringify(sy.serializeObject($("#add_adviser_params")));
		var params_validateAccount = $('#add_account').val();
		$.post("<%=request.getContextPath()%>/adviserMgr!getAdviserInfo",
	            { "params" : params_validateAccount },
	            function(result){
	            	if (result != "null") {
	            		$.messager.alert('Warning','<s:text name="ADVISER_VIEW_STR9"></s:text>!');
	            		return false;
                    } else {
                    	$.post("<%=request.getContextPath()%>/adviserMgr!adviserAction",
                           	{
                   				req_params : params
                           	},
                           	function(data){
                           		$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
                           		cancleWindow('addAdviserDiv');
                           		$('#tt_adviser').datagrid("reload");
                           	},
                           	"json"
                           ); 
            		}
	            }
	    );
	}
	
</script>
<div style="padding: 5px;">
	<form id="add_adviser_params">
		<input type="hidden" id="action_no" name="action_no" value="1"/>
		<input type="hidden" id="id" name="id" />
		<table width="570px" border="0" cellpadding="0" cellspacing="5">
			<tr>
				<td><label><s:text name="SUBSCRIBER_VIEW_STR9"></s:text> </label></td>
				<td><input type="text" id="add_name" name="add_name" class="easyui-validatebox" required="true"/></td>
				<td><label><s:text name="ADVISER_VIEW_STR2"></s:text> </label></td>
				<td>
					<input type="text" id="add_account" name="add_account" class="easyui-validatebox" required="true"/>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="SUBSCRIBER_VIEW_STR2"></s:text></label></td>
				<td>
					<select id="add_sex" name="add_sex" class="easyui-combobox" panelHeight="auto" style="width:60px;">  
					    <option value="1"><s:text name="MAN"></s:text></option>  
					    <option value="0"><s:text name="WOMAN"></s:text></option>  
					</select>
				</td>
				<td><label><s:text name="SUBSCRIBER_VIEW_STR7"></s:text></label></td>
				<td><input type="text" id="add_phone" name="add_phone" /></td>
			</tr>
			<tr>
				<td><label><s:text name="SUBSCRIBER_VIEW_STR8"></s:text> </label></td>
				<td><input type="text" id="add_mail" name="add_mail" class="easyui-validatebox"  validType="email"/></td>
				<td><label><s:text name="ADVISER_VIEW_STR10"></s:text>/<s:text name="ADVISER_VIEW_STR11"></s:text></label></td>
				<td><input type="radio" name="add_type" value="2" title="<s:text name='ADVISER_VIEW_STR10'></s:text>" /><s:text name="ADVISER_VIEW_STR10"></s:text><input type="radio" name="add_type" value="3" title="<s:text name='ADVISER_VIEW_STR11'></s:text>"/><s:text name="ADVISER_VIEW_STR11"></s:text></td>
			</tr>
			<tr>
				<td><label><s:text name="SUBSCRIBER_VIEW_STR28"></s:text></label></td>
				<td><input type="radio" name="weichat_type" value="az_cn" title="CN" />CN<input type="radio" name="weichat_type" value="az_en" title="EN"/>EN</td>
			</tr>
		</table>
	</form>
	<div style="padding-top:5px;text-align:center;">  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-ok" onclick="saveAdviser();"><s:text name='SAVE'></s:text></a>  
		<a href="javascript:void(0);" class="easyui-linkbutton" icon="icon-cancel" onclick="cancleWindow('addAdviserDiv');"><s:text name='CANCLE'></s:text></a>  
	</div>
</div>
