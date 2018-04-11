<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>所有咨询</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css"> 
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script> 
<script src="${pageContext.request.contextPath}/js/jquery.ui.datepicker-zh-CN.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<%-- <script src="${pageContext.request.contextPath}/js/jquery.form.js"></script> --%>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>



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
		width:160px;
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
	});

	//查询该咨询师完成的咨询记录
	function getCompleteAdvisory(){
		$('#tt_complete_advisory').datagrid({ 
			title:'咨询信息',
			width:'auto',
			height:'auto',
			idField:'id',
			//url:'${pageContext.request.contextPath}/weixin!getTaskByDate',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			//singleSelect:true,
		    columns:[[  
		        {field:'id',hidden:true},
		        {field:'service_id',hidden:true},
		        {field:'nickname',title:'微信用户昵称',width:90,align:'center'}, 
		        {field:'service_nickName',title:'客服昵称',width:90,align:'center'}, 
		        {field:'create_date',title:'任务开始时间',width:150,align:'center'},
		        {field:'task_status',title:'状态',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='0')
		        		  statusText = '未处理';
		        	  else if(value=='1')
		        		  statusText = '已经分配';
		        	  else if(value=='2')
		        		  statusText = '正在处理';
		        	  else if(value=='3')
		        		  statusText = '处理完成';
		        	  else if(value=='4')
		        		  statusText = '升级完成';
		        	  else if(value=='5')
		        		  statusText = '被挂起';
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
		        {field:'service_score',title:'评价',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	 var statusText='';
		        	  if(value=='5')
		        		  statusText = '非常满意';
		        	  else if(value=='4')
		        		  statusText = '满意';
		        	  else if(value=='3')
		        		  statusText = '一般';
		        	  else if(value=='2')
		        		  statusText = '不满意';
		        	  else if(value=='1')
		        		  statusText = '非常不满意';
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
	
	//客户服务按钮
	function openChatWindows(){
		var rows = $('#tt_complete_advisory').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('警告','您没有选择任何记录，无法进行操作!');
			return;
		}
		
		for(var i=0;i<rows.length;i++){
			if(rows[i].task_status == '3'){
				//加载对话Tab
				addTabPane(rows[i].open_id,
						   rows[i].nickname,
						   '${pageContext.request.contextPath}/weixin!openHistoryWindow?taskID=' + rows[i].id + '&flag=1'
						);
			} else {
				//加载对话Tab
				addTabPane(rows[i].open_id,
						   rows[i].nickname,
						   '${pageContext.request.contextPath}/weixin!openHistoryWindow?taskID=' + rows[i].id + '&flag=0'
						);
			}
		}
	}

	//打开底层对话框
	function openChatWindow(itemid) {
		$('#openChatDiv').window({    		
            modal:false,
            cache:false,
            width:640,
            height:520,
            left:100,
            top:100,
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
	 
	//根据任务日期查询任务
	function searchAdviser() {
		var conditions = JSON.stringify(serializeObject($("#conditions")));	
		//alert(conditions);		
		$('#tt_complete_advisory').datagrid({	        
	        url:'${pageContext.request.contextPath}/weixin!searchTaskByIDAndKcode',
			queryParams : {				
				req_params : conditions
			},
			onLoadSuccess:function(data) { }
		});
	}
	
	//清空查询数据
	function clearConditions(){
		document.getElementById('"code"').value = "";
		document.getElementById('"code"').text = "";
		document.getElementById('"kcode"').value = "";
		document.getElementById('"kcode"').text = "";
	}
	
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div class="topDiv" id="completeDiv">
		<form id="conditions">
			<div class="conditionDiv">
				<label>请求单号：</label> 
				<input id="code" name="code" type="text" class="mytext" />
				<label>K账号：</label> 
				<input id="code" name="kcode" type="text" class="mytext" />
	     	</div>		
		</form>
		<div style="padding-right:10px;text-align:right;">  
			<a id="btnClear" href="#" class="easyui-linkbutton" onclick="clearConditions();">清空</a>
			<a id="btnQuery" href="javascript:void(0);" class="easyui-linkbutton" onclick="searchAdviser();">查询</a>
		</div>
	</div>	
<div style="padding:15px;text-align:left;">  
			<a href="javascript:void(0);" class="easyui-linkbutton" onclick="openChatWindows();">聊天记录</a> 
	</div>
<table id="tt_complete_advisory"></table>
<div id="openChatDiv" class="easyui-window" closed="true" title="聊天记录" collapsible="false" minimizable="false" maximizable="true">
      <div id="chat-container" class="easyui-tabs" data-options="fit:true,border:false" ></div>
</div>
</body>
</html>