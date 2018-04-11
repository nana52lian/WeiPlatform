<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关注人列表</title>	
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>
<script src="${pageContext.request.contextPath}/js/syUtil.js"></script>

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
		width:120px;
	}
</style>
<script>

	$(function() {
		initWeixinUserGrid();
		
	});	

	function initWeixinUserGrid() {		
		$('#tt_weixin_user').datagrid({ 
			title:'<s:text name="SUBSCRIBER_VIEW_STR1"></s:text>',
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			singleSelect:true,
		    columns:[[  
		        {field:'id',hidden:true},
		        {field:'openid',title:'openid',width:205,align:'center'},  
		        {field:'key_account',title:'<s:text name="SUBSCRIBER_VIEW_STR11"></s:text>',width:100,align:'center'},
		        {field:'name',title:'<s:text name="SUBSCRIBER_VIEW_STR9"></s:text>',width:100,align:'center'}, 
		        {field:'sex',title:'<s:text name="SUBSCRIBER_VIEW_STR2"></s:text>',width:60,align:'center',formatter:function(value,rowData,rowIndex){
		        	  var sexText='';
		        	  if(value=='1')
		        		  sexText = '<s:text name="MAN"></s:text>';
		        	  else if(value=='2')
		        		  sexText = '<s:text name="WOMAN"></s:text>';
		        	  else if(value=='0')
		        		  sexText = '<s:text name="UNKNOWN"></s:text>';
		        	  return sexText;
		        }},	
		        /* {field:'grade',title:'<s:text name="SUBSCRIBER_VIEW_STR4"></s:text>',width:100,align:'center'},
		        {field:'department',title:'<s:text name="SUBSCRIBER_VIEW_STR5"></s:text>',width:100,align:'center'},
		        {field:'area',title:'<s:text name="SUBSCRIBER_VIEW_STR6"></s:text>',width:100,align:'center'}, */
		        {field:'cellphone',title:'<s:text name="SUBSCRIBER_VIEW_STR7"></s:text>',width:100,align:'center'},
		        {field:'email',title:'<s:text name="SUBSCRIBER_VIEW_STR8"></s:text>',width:100,align:'center'},
		        {field:'formatSubscribeTime',title:'<s:text name="SUBSCRIBER_VIEW_STR12"></s:text>',width:150,align:'center'},
		        {field:'active_time',title:'<s:text name="SUBSCRIBER_VIEW_STR13"></s:text>',width:150,align:'center'},
		        {field:'request_count',title:'<s:text name="SUBSCRIBER_VIEW_STR14"></s:text>',width:50,align:'center'},
		        {field:'user_level',title:'<s:text name="SUBSCRIBER_VIEW_STR15"></s:text>',width:120,align:'center'},
		        {field:'subscribe',title:'<s:text name="SUBSCRIBER_VIEW_STR16"></s:text>',width:70,align:'center',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='1')
		        		  statusText = '<s:text name="SUBSCRIBER_VIEW_STR17"></s:text>';
		        	  else if(value=='0')
		        		  statusText = '<s:text name="SUBSCRIBER_VIEW_STR18"></s:text>';
		        	  return statusText;
		        }}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30]
		});  
	}
	
	// 查询微信用户
	function searchWeixinUser() {
		var conditions = JSON.stringify(sy.serializeObject($("#conditions")));	
		// 查询前先翻到第一页
		//$('#tt_weixin_user').datagrid('getPager').pagination('select', 1);
		$('#tt_weixin_user').datagrid({	        
	        url:'${pageContext.request.contextPath}/weixinUserMgr!queryWeixinUsers',
			queryParams : {				
				req_params : conditions
			},
			onLoadSuccess:function(data) { }
		});
	}
	
	// 获取单个微信用户信息
	function getWinxinUserInfo(){
		var rows = $('#tt_weixin_user').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="WARNING" ></s:text>','<s:text name="REQUISITION_MANAGE_STR3" ></s:text>!');
			return;
		}
		$('#weixinUserInfoDiv').window({
    		href:'weixin_user_info.jsp', 
            cache:false,
            width:580,
            height:420,
            left:100,
            top:100,
            onLoad:function(){
            	$('#openId').val(rows[0].openid);
            	$('#k_account_view').val(rows[0].key_account);
            	$('#nickname').val(rows[0].nickname);
            	var value = rows[0].sex;
				var sexText='';
					if(value=='1')
						sexText = '<s:text name="MAN"></s:text>';
					else if(value=='2')
						sexText = '<s:text name="WOMAN"></s:text>';
					else if(value=='0')
						sexText = '<s:text name="UNKNOWN"></s:text>';
            	$('#sex_view').val(sexText);
            	
            	var value_1 = rows[0].subscribe;
            	var subscribeText='';
					if(value_1=='1')
						subscribeText = '<s:text name="SUBSCRIBER_VIEW_STR17"></s:text>';
					else if(value_1=='0')
						subscribeText = '<s:text name="SUBSCRIBER_VIEW_STR18"></s:text>';
        		$('#status').val(subscribeText);
        		$('#grade_view').val(rows[0].grade);
        		
        		$('#grade_view').val(rows[0].grade);
        		$('#department_view').val(rows[0].department);
        		$('#cellphone_view').val(rows[0].cellphone);
        		$('#email_view').val(rows[0].email);
        		$('#area_view').val(rows[0].area);
        		
        		/* $('#country').val(rows[0].country);
        		$('#province').val(rows[0].province);
        		$('#city').val(rows[0].city); */
            	$('#subscribe_time').val(rows[0].formatSubscribeTime);
            	initRequestDatagrid(rows[0].openid);
            }
        });
   		$('#weixinUserInfoDiv').window('open');
	}
	
	//关注人详细信息
	function initRequestDatagrid(openid){
		var open_id = openid;
		$('#requestDatagrid').datagrid({ 
			width:'auto',
			height:'auto',
			idField:'id',
			url:'${pageContext.request.contextPath}/weixin!getRequestByOpenId',
			queryParams : {				
				req_params : open_id
			},
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
		    columns:[[  
		        {field:'id',hidden:true},
		        {field:'open_id',hidden:true},
		        {field:'nickname',title:'<s:text name="SUBSCRIBER_VIEW_STR19"></s:text>',width:90,align:'center'},
		        {field:'create_date',title:'<s:text name="SUBSCRIBER_VIEW_STR20"></s:text>',width:150,align:'center'},
		        {field:'task_status',title:'<s:text name="SUBSCRIBER_VIEW_STR21"></s:text>',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='3')
		        		  statusText = '<s:text name="SUBSCRIBER_VIEW_STR22"></s:text>';
		        	  else if(value=='4')
		        		  statusText = '<s:text name="SUBSCRIBER_VIEW_STR23"></s:text>';
		        	  return statusText;
		        }},
		        {field:'msg_type',hidden:true}, 
		        {field:'content',title:'<s:text name="SUBSCRIBER_VIEW_STR24"></s:text>',width:150,align:'center',formatter:function(value,rowData,rowIndex){
		        	if(rowData.msg_type == "image"){
		        		 var temp1 = value.substring(value.indexOf('"')+1);
		        		 var temp2 = temp1.substring(0,temp1.indexOf('"'));
		        	     var imgUrl = "<img src=\"" + temp2 + "\" width=\"50px\" height=\"50px\"/>";
		        	} else {
		        		imgUrl = value;
		        	}
	        	  return imgUrl;
		        }},
		        {field:'service_score',title:'<s:text name="SUBSCRIBER_VIEW_STR25"></s:text>',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	 var statusText='';
		        	  if(value=='5')
		        		  statusText = '<s:text name="REQUISITION_MANAGE_STR23"></s:text>';
		        	  else if(value=='4')
		        		  statusText = '<s:text name="REQUISITION_MANAGE_STR24"></s:text>';
		        	  else if(value=='3')
		        		  statusText = '<s:text name="REQUISITION_MANAGE_STR25"></s:text>';
		        	  else if(value=='2')
		        		  statusText = '<s:text name="REQUISITION_MANAGE_STR26"></s:text>';
		        	  else if(value=='1')
		        		  statusText = '<s:text name="REQUISITION_MANAGE_STR27"></s:text>';
		        	  return statusText;
		        }}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30]
			/* ,
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]] */
		});  
	}
	
	//客户服务按钮
	function openChatWindows(){
		var rows = $('#requestDatagrid').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="REQUISITION_MANAGE_STR3"></s:text>!');
			return;
		}
		for(var i=0;i<rows.length;i++){
			//加载对话Tab
			addTabPane(rows[i].open_id,
					   rows[i].nickname,
					   '${pageContext.request.contextPath}/weixin!openHistoryWindow?taskID=' + rows[i].id + '&flag=2'
					);
		}
	}
	
	//打开底层对话框
	function openChatWindow(itemid) {
		$('#openChatDiv').window({    		
            cache:false,
            width:640,
            height:520,
            left:150,
            top:150,
            onLoad:function(){
            	
            }
        });   		
		$('#openChatDiv').window('open');
	}
	
	//加载对话框Tab
	function addTabPane(itemid, title, url, icon){  
	    var tt = $('#chat-container');	    
	    if (tt.tabs('exists', title)){//如果tab已经存在,则选中并刷新该tab     
	        tt.tabs('select', title);
	        refreshTab({ tabTitle:title, url:url });  
	    } else {
	    	// itemid 就是发消息人的ID
			var content = "<iframe id='"+ itemid + "' name='"+ itemid + "' scrolling='auto' frameborder='0' src=\'"+url+"\' style='width:100%;height:99%;'></iframe>";
			tt.tabs('add',{
	        	id:itemid,
	            title:title,  
	            closable:true,  
	            content:content,  
	            iconCls:icon
	        });  
	    };
	    openChatWindow(itemid);
	} 
	
	/**     
	 * 刷新tab 
	 * @cfg  
	 *example: {tabTitle:'tabTitle',url:'refreshUrl'} 
	 *如果tabTitle为空，则默认刷新当前选中的tab 
	 *如果url为空，则默认以原来的url进行reload 
	 */  
	function refreshTab(cfg){  
	    var refresh_tab = cfg.tabTitle ? $('#chat-container').tabs('getTab',cfg.tabTitle) : $('#chat-container').tabs('getSelected');  
	    if(refresh_tab && refresh_tab.find('iframe').length > 0){  
		    var _refresh_ifram = refresh_tab.find('iframe')[0];  
		    var refresh_url = cfg.url ? cfg.url : _refresh_ifram.src;  
		    //_refresh_ifram.src = refresh_url;  
		    _refresh_ifram.contentWindow.location.href = refresh_url;  
	    };
	}
	
	function cancleWindow(id){
		$('#'+id ).window('close');
	}
	
	function clearConditions(){
		document.getElementById("openid").value = "";
		document.getElementById("k_account").value = "";
		document.getElementById("name").value = "";
		/* document.getElementById("grade").value = "";
		document.getElementById("department").value = "";
		document.getElementById("area").value = ""; */
		document.getElementById("cellphone").value = "";
		document.getElementById("email").value = "";
		document.getElementById("taskId").value = "";
		$('#sex').val("0");
	}
	
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<input type="hidden" id="hidden_id" name="hidden_id" />
	<div class="topDiv">
		<form id="conditions">
			<div class="conditionDiv">
				<label>openID：</label>
				<input id="openid" name="openid" class="mytext" />
				&nbsp;&nbsp;
				<label><s:text name="SUBSCRIBER_VIEW_STR11"></s:text>：</label>
				<input id="k_account" name="k_account" class="mytext" />
				&nbsp;&nbsp;
				<label><s:text name="SUBSCRIBER_VIEW_STR9"></s:text>：</label> 
				<input id="name" name="name" class="mytext" />
				&nbsp;&nbsp;
				<label><s:text name="SUBSCRIBER_VIEW_STR2"></s:text>：</label>
				<select id="sex" name="sex" class="sex" panelHeight="auto" style="width:60px;">  
				    <option value="3"><s:text name="ALL"></s:text></option>
				    <option value="1"><s:text name="MAN"></s:text></option>  
				    <option value="2"><s:text name="WOMAN"></s:text></option>  
				</select>
				<%-- &nbsp;&nbsp;
				<label><s:text name="SUBSCRIBER_VIEW_STR4"></s:text>：</label> 
				<input id="grade" name="grade" class="mytext" /> --%>
	     	</div>
	     	<div class="conditionDiv">
				<%-- <label><s:text name="SUBSCRIBER_VIEW_STR5"></s:text>：</label>
				<input id="department" name="department" class="mytext" />
				&nbsp;&nbsp;
				<label><s:text name="SUBSCRIBER_VIEW_STR6"></s:text>：</label>
				<input id="area" name="area" class="mytext" />
				&nbsp;&nbsp; --%>
				<label><s:text name="SUBSCRIBER_VIEW_STR7"></s:text>：</label> 
				<input id="cellphone" name="cellphone" class="mytext" />
				&nbsp;&nbsp;
				<label><s:text name="SUBSCRIBER_VIEW_STR8"></s:text>：</label>
				<input id="email" name="email" class="mytext" />
				&nbsp;&nbsp;
				<label><s:text name="SUBSCRIBER_VIEW_STR10"></s:text>：</label> 
				<input id="taskId" name="taskId" class="mytext" />
	     	</div>		
		</form>
		<div style="padding-right:10px;text-align:right;">  
			<a id="btnClear" href="#" class="easyui-linkbutton" onclick="clearConditions();"><s:text name="CLEAR"></s:text></a>
			<a id="btnQuery" href="javascript:void(0);" class="easyui-linkbutton" onclick="searchWeixinUser();"><s:text name="SEARCH"></s:text></a>
		</div>
	</div>	
	<!-- <div class="conditionDiv">
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="getWinxinUserInfo();">查看</a>
	</div> -->	
	<table id="tt_weixin_user"></table>
	<div id="weixinUserInfoDiv" class="easyui-window" closed="true" title="<s:text name="SUBSCRIBER_VIEW_STR26"></s:text>" style="padding:20px" collapsible="false" minimizable="false" maximizable="false">
	</div>
	<div id="openChatDiv" class="easyui-window" closed="true" title="<s:text name="SUBSCRIBER_VIEW_STR27"></s:text>" collapsible="false" minimizable="false" maximizable="true">
		<div id="chat-container" class="easyui-tabs" data-options="fit:true,border:false" ></div>
	</div>
</div>	
</body>
</html>