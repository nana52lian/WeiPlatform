<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>请求单管理</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script> 
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
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
		width:140px;
	}
	
	.border_table {
		border:solid #add9c0; 
		border-width:1px 0px 0px 1px;
	}
	
	.border_table tbody tr td {
		border:solid #add9c0; 
		border-width:0px 1px 1px 0px;		
	}
	
</style>
<script>
	$(function() {
		getCompleteAdvisory();
		getUncompleteAdvisory();
	});

	function getCompleteAdvisory(){
		$('#tt_complete_advisory').datagrid({ 
			title:'<s:text name="REQUISITION_MANAGE_STR14"></s:text>',
			width:'auto',
			height:'auto',
			idField:'id',
			//url:'${pageContext.request.contextPath}/weixin!getAllRequest',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			singleSelect:true,
		    columns:[[  
		        {field:'id',hidden:true},
		        {field:'rid',title:'RID',width:110,align:'center'},
		        {field:'open_id',title:'<s:text name="REQUISITION_MANAGE_STR16"></s:text>',width:200,align:'center'},
		       /*  {field:'key_account',title:'<s:text name="REQUISITION_MANAGE_STR16"></s:text>',width:70,align:'center'},  */ 
		        {field:'service_id',title:'<s:text name="REQUISITION_MANAGE_STR1"></s:text>',width:200,align:'center'},
		        {field:'service_name',title:'<s:text name="REQUISITION_MANAGE_STR17"></s:text>',width:90,align:'center'},
		        {field:'type',title:'<s:text name="REQUISITION_MANAGE_STR18"></s:text>',width:90,align:'center'},
		        {field:'create_date',title:'<s:text name="REQUISITION_MANAGE_STR19"></s:text>',width:150,align:'center'},
		        /*{field:'task_status',title:'状态',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='3')
		        		  statusText = '已完成';
		        	  else if(value=='4')
		        		  statusText = '终止';
		        	  return statusText;
		        }},
		        
		        {field:'msg_type',hidden:true}, 
		        {field:'content',title:'最后聊天内容',width:150,align:'center',formatter:function(value,rowData,rowIndex){
		        	if(rowData.msg_type == "image"){
		        		 var temp1 = value.substring(value.indexOf('"')+1);
		        		 var temp2 = temp1.substring(0,temp1.indexOf('"'));
		        	     var imgUrl = "<img src=\"" + temp2 + "\" width=\"50px\" height=\"50px\"/>";
		        	} else {
		        		imgUrl = value;
		        	}
	        	  return imgUrl;
		        }},
		        */
		        {field:'last',title:'<s:text name="REQUISITION_MANAGE_STR20"></s:text>',width:90,align:'center'},
		        {field:'custom_score',title:'<s:text name="REQUISITION_MANAGE_STR21"></s:text>',width:180,align:'center'},
		        {field:'service_score',title:'<s:text name="REQUISITION_MANAGE_STR22"></s:text>',width:230,align:'center',formatter:function(value,rowData,rowIndex){
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
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]
		});
	}
	
	//未关闭请求单
	function getUncompleteAdvisory(){
		$('#tt_uncomplete').datagrid({ 
			title:'<s:text name="REQUISITION_MANAGE_STR15"></s:text>',
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
		        {field:'open_id',title:'<s:text name="REQUISITION_MANAGE_STR16"></s:text>',width:200,align:'center'},
		       /*  {field:'key_account',title:'<s:text name="REQUISITION_MANAGE_STR16"></s:text>',width:70,align:'center'},  */
		        {field:'service_id',title:'<s:text name="REQUISITION_MANAGE_STR1"></s:text>',width:200,align:'center'},
		        {field:'service_name',title:'<s:text name="REQUISITION_MANAGE_STR17"></s:text>',width:90,align:'center'},
		        {field:'type',title:'<s:text name="REQUISITION_MANAGE_STR18"></s:text>',width:90,align:'center'},
		        {field:'create_date',title:'<s:text name="REQUISITION_MANAGE_STR19"></s:text>',width:150,align:'center'},
		        {field:'last',title:'<s:text name="REQUISITION_MANAGE_STR20"></s:text>',width:70,align:'center'},
		        {field:'task_status',title:'<s:text name="REQUISITION_MANAGE_STR28"></s:text>',width:70,align:'center',formatter:function(value,rowData,rowIndex){
		        	 var statusText='';
		        	  if(value=='2')
		        		  statusText = '<s:text name="REQUISITION_MANAGE_STR29"></s:text>';
		        	  else if(value=='5')
		        		  statusText = '<s:text name="REQUISITION_MANAGE_STR30"></s:text>';
		        	  return statusText;
		        }}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]
		});
	}
	
	function openChatWindows(){
		var rows = $('#tt_complete_advisory').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="WARINING"></s:text>','<s:text name="REQUISITION_MANAGE_STR3"></s:text>!');
			return;
		}
		for(var i=0;i<rows.length;i++){
			//加载对话Tab
			addTabPane(rows[i].open_id,
					   rows[i].open_id,
					   '${pageContext.request.contextPath}/weixin!openHistoryWindowAll?taskID=' + rows[i].id + '&flag=1'
					);
		}
	}
	
	//未完成请求单详情
	function openChatWindowsUncomplete(){
		var rows = $('#tt_uncomplete').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="WARINING"></s:text>','<s:text name="REQUISITION_MANAGE_STR3"></s:text>!');
			return;
		}
		for(var i=0;i<rows.length;i++){
			//加载对话Tab
			addTabPane(rows[i].open_id,
					   rows[i].open_id,
					   '${pageContext.request.contextPath}/weixin!openHistoryWindow?taskID=' + rows[i].id + '&flag=3'
					);
		}
	}

	//打开底层对话框
	function openChatWindow(itemid) {
		$('#openChatDiv').window({    		
            modal:false,
            cache:false,
            width:740,
            height:565,
            left:100,
            top:100,
            onLoad:function(){
            	
            },
            onClose:function(){
            	var alltabs = $('#chat-container').tabs('tabs');
            	var allTabtitle = []; 
            	$.each(alltabs,function(i,n){ 
            		allTabtitle.push($(n).panel('options').title); 
            	});
            	console.log(allTabtitle);
            	$.each(allTabtitle, function (i, n) { 
            		$('#chat-container').tabs('close', n); 
            	}); 
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
	            closable:false,  
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
	 
	//根据任务单号查询任务
	function searchAdviser() {
		var conditions = JSON.stringify(sy.serializeObject($("#conditions")));	
		//alert(conditions);		
		$('#tt_complete_advisory').datagrid({	        
	        url:'${pageContext.request.contextPath}/weixin!getAllRequest',
			queryParams : {				
				req_params : conditions,
				request_type : "complete"
			},
			onLoadSuccess:function(data) { }
		});
		$('#tt_uncomplete').datagrid({	        
	        url:'${pageContext.request.contextPath}/weixin!getAllRequest',
			queryParams : {				
				req_params : conditions,
				request_type : "uncomplete"
			},
			onLoadSuccess:function(data) { }
		});
	}
	
	//清空查询数据
	function clearConditions(){
		document.getElementById("rid").value = "";
		document.getElementById("openid").value = "";
		document.getElementById("key_account").value = "";
		document.getElementById("service_id").value = "";

		//document.getElementById("date").value = "";
		$("#date").combo("setValue","").combo("setText","");

	}
</script>
</head>
<body style="padding: 10px;">
<s:include value="../langCommon.jsp"></s:include>
	<div class="topDiv">
		<form id="conditions">
			<div class="conditionDiv">
				<label>RID：</label> 
				<input id="rid" name="rid" type="text" class="mytext" />
				&nbsp;&nbsp;
				<label>openID：</label> 
				<input id="openid" name="openid" type="text" class="mytext" />
				&nbsp;&nbsp;
				<label><s:text name="REQUISITION_MANAGE_STR16"></s:text>：</label> 
				<input id="key_account" name="key_account" type="text" class="mytext" />
				&nbsp;&nbsp;
				<label><s:text name="REQUISITION_MANAGE_STR1"></s:text>：</label> 
				<input id="service_id" name="service_id" type="text" class="mytext" />
				&nbsp;&nbsp;
				<label><s:text name="REQUISITION_MANAGE_STR17"></s:text>：</label> 
				<input id="service_name" name="service_name" type="text" class="mytext" />
				<br/>
				<div style="padding-top: 10px;">
				<label><s:text name="DATA_STATISTICS_STR5"></s:text>：</label> 
				<input id="date" name="date" type="text" class="easyui-datebox"/>
				</div>
	     	</div>		
		</form>
		<div style="padding-right:10px;text-align:right;">  
			<a id="btnClear" href="#" class="easyui-linkbutton" onclick="clearConditions();"><s:text name="CLEAR"></s:text></a>
			<a id="btnQuery" href="javascript:void(0);" class="easyui-linkbutton" onclick="searchAdviser();"><s:text name="SEARCH"></s:text></a>
		</div>
	</div>	
	<div style="padding:10px;text-align:left;">  
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="openChatWindows();"><s:text name="REQUISITION_MANAGE_STR2"></s:text></a> 
	</div>
	<table id="tt_complete_advisory"></table>
	<div style="height: 80px;"></div>
	<div style="padding:10px;text-align:left;">  
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="openChatWindowsUncomplete();"><s:text name="REQUISITION_MANAGE_STR2"></s:text></a> 
	</div>
	<table id="tt_uncomplete"></table>
	<div id="openChatDiv" class="easyui-window" closed="true" title="<s:text name="REQUISITION_MANAGE_STR2"></s:text>" collapsible="false" minimizable="false" maximizable="true">
    <div id="chat-container" class="easyui-tabs" data-options="fit:true,border:false" ></div>
</div>
</body>
</html>