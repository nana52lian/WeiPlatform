<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">

	$(function () {
		//初始化leader
		getLeader();
	});


	//save role info
	function saveGroup() {	
		if(!$('#groupForm').form('validate')){
			return;
		}
		var ps = $("#leader").combobox('getValue'); //document.getElementById('leader').value;
		if("please select"==ps || "请选择"==ps){
			$.messager.alert('<s:text name="INFO"></s:text>', '<s:text name="GROUP_VIEW_STR15"></s:text>');
			return;
		}
		
		$('#groupForm').ajaxSubmit({
			success:function (data) {				
				$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
				closeWindow('addGroupDiv');
				$('#tt_group').datagrid("reload");
			},
			dataType:'json'
		});
	}

	//获取leader
	function getLeader(){
		$('#leader').combobox({  
			 url:"${pageContext.request.contextPath}/adminPlatformUserMgr!loadLeader",  
	   		 valueField:'value',  
	   		 textField:'text' , 
	   		 onSelect:function(data){
	         },
	         onSuccess:function(data){
	         }
		}); 
	}
</script>

<form style="padding:10px 20px 10px 40px;" name="groupForm" id="groupForm" action="${pageContext.request.contextPath}/adviserMgr!groupAction" method="post" >  
    <input type="hidden" id="action_no" name="groupEntity.action_no" value="1" />
    <input type="hidden" id="id" name="groupEntity.id" />
    <p><s:text name="GROUP_VIEW_STR6"></s:text>:<input type="text" id="name" name="groupEntity.name" class="easyui-validatebox" required="true"/></p>  
    <p>LEADER:<input id="leader" name="groupEntity.leader" class="easyui-combobox easyui-validatebox" required="true" panelHeight="auto" value="<s:text name="PLATFORM_USERS_VIEW_STR30"></s:text>" style="width:122px;" data-options="editable:false"/></p>  
    <p><s:text name="GROUP_VIEW_STR7"></s:text>:<textarea cols="19" rows="5" id="groupdesc" name="groupEntity.groupdesc" class="easyui-validatebox" required="true"></textarea></p>
    <div style="padding:5px;text-align:center;">
        <a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="saveGroup();"><s:text name="SAVE"></s:text></a>  
        <a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="closeWindow('addGroupDiv');"><s:text name="CANCLE"></s:text></a>  
    </div>  
</form>  