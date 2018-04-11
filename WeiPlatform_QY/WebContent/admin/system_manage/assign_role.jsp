<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">

	$(function(){		
		// 初始化平台账户列表
		initPlatformUserGrid();
	});
		
	// 初始化已经添加的平台账户列表
	function initAssignedUserGrid(auth_id) {
		$('#tt_assigned').datagrid({ 
			title:'<s:text name="USER_ROLE_VIEW_STR14"></s:text>',
			idField:'id',
			height:'385',
			width:'320',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			singleSelect:false,
	        iconCls:'icon-save',
	        url:'${pageContext.request.contextPath}/adminRoleAuthsMgr!loadRoleUsers',
	        queryParams : { 'authorityEntity.id' : auth_id },
		    columns:[[
				{field:'id',hidden:true},
				{field:'account',title:'<s:text name="USER_ROLE_VIEW_STR17"></s:text>',width:120,align:'center'},
				{field:'name',title:'<s:text name="USER_ROLE_VIEW_STR27"></s:text>',width:100,align:'center'}	
		        
		    ]],
		    toolbar:'#tb2',
		    /*
		    toolbar:[ 
		 	  			{ 
							text:'从角色关联中删除', 
							iconCls:'icon-remove', 
							handler:delSelected
			  			} 
	  				],
	  		*/
	    	frozenColumns:[[
   	      		{field:'ck',checkbox:true} 
   			]]
		});		
	}
	
	// 移除关联
	function delSelected() {
		var selectRows = $('#tt_assigned').datagrid("getSelections");
		var rowsLength  = selectRows.length;
		if(selectRows.length < 1){			
			return;
		}
		
		for(var i = rowsLength-1  ; i >= 0 ; i--){
			var index = $('#tt_assigned').datagrid('getRowIndex',selectRows[i]);//获取某行的行号
			delRow('tt_assigned',index);			
		}
	}
	
	function delRow(datagrid,index){
		 $('#' + datagrid).datagrid('deleteRow',index);
	}
	
	// 初始化平台账户列表
	function initPlatformUserGrid() {
		$('#tt_platform_user').datagrid({ 
			title:'<s:text name="USER_ROLE_VIEW_STR16"></s:text>',
			idField:'id',
			height:'385',
			width:'520',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			singleSelect:false,
	        iconCls:'icon-save',
	        url:'${pageContext.request.contextPath}/adminPlatformUserMgr!loadPlatformUsers',
	        queryParams : { req_params : '{}'},
		    columns:[[
					{field:'id',hidden:true},		        
					{field:'account',title:'<s:text name="USER_ROLE_VIEW_STR17"></s:text>',width:220,align:'center'},
					{field:'name',title:'<s:text name="USER_ROLE_VIEW_STR27"></s:text>',width:200,align:'center'}	
		    ]],
		    toolbar:'#tb',
		    /*
		    toolbar:[ 
		 	  			{ 
							text:'关联到角色', 
							iconCls:'icon-back', 
							handler:addToRole,
							plan:false
			  			} 
	  				],
	  		*/
	    	frozenColumns:[[
   	      		{field:'ck',checkbox:true} 
   			]],
   			pagination:true,
   			pageList:[10,20,30]
		});
	}
	
	// 添加关联
	function addToRole() {
		var rows = $('#tt_platform_user').datagrid("getSelections");
		var selectedRows = $('#tt_assigned').datagrid("getRows");		
		var selectedRowLength = selectedRows.length;
		for(var i = 0 ;i < rows.length;i++){
			var id = rows[i].id;
			var isHas = false; 
			for(var j = 0 ; j < selectedRowLength ; j++){
				if(id == selectedRows[j].id ){
					isHas = true;
					break;
				}
			}			
			if(!isHas){
				 $('#tt_assigned').datagrid('appendRow',{ id:rows[i].id, account:rows[i].account, name:rows[i].name, sort:0  });
			}
		}
	}
	
	function saveRoleUser() {
		var rows = $('#tt_assigned').datagrid("getRows");				
		var ids = "";
		for(var i=0; i<rows.length; i++){
			ids = ids + rows[i].id + ',';
		}
		$('#platfrom_user_ids').val(ids);		
		$('#addUserForm').ajaxSubmit({
				success:function (data) {
					$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
					closeWindow('assignRoleDiv');					
				},
				dataType:'json'
			}
		);
	}
		
</script>
<div id="tb">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-back',plain:false" onclick="addToRole()"><s:text name="USER_ROLE_VIEW_STR15"></s:text></a>
</div>
<div id="tb2">
	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:false" onclick="delSelected()"><s:text name="USER_ROLE_VIEW_STR3"></s:text></a>
</div>
<form action="${pageContext.request.contextPath}/adminRoleAuthsMgr!assignUser" id="addUserForm" name="addUserForm" method="post">
	<input type="hidden" id="auth_id" name="authorityEntity.id" />
	<input type="hidden" id="platfrom_user_ids" name="authorityEntity.platfrom_user_ids" />
</form>
<div style="padding:0px;">
	<table>
		<tr>
			<td width="45%"><table id="tt_assigned"></table></td>		
			<td width="45%"><table id="tt_platform_user"></table></td>	
		</tr>
	</table>
	<div style="text-align:center;">
		<a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="saveRoleUser();"><s:text name="SAVE"></s:text></a>  
		<a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="cancleWindow('assignRoleDiv');"><s:text name="CANCLE"></s:text></a>  
    </div> 
</div>
