<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>完成咨询</title>
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
		//alert('<%=session.getAttribute("lang")%>');
		getCompleteAdvisory();
	});

	function getCompleteAdvisory(){
		$('#tt_complete_advisory').datagrid({ 
			title:'<s:text name="complete_advisory_str1"></s:text>',
			width:'auto',
			height:'auto',
			idField:'id',
			url:'${pageContext.request.contextPath}/weixin!getCompleteAdvisory',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			singleSelect:true,
		    columns:[[  
		        {field:'id',title:'<s:text name="complete_advisory_str2"></s:text>',width:150,align:'center'},
		        {field:'open_id',title:'<s:text name="complete_advisory_str3"></s:text>',width:90,align:'center'}, 
		        {field:'create_date',title:'<s:text name="complete_advisory_str4"></s:text>',width:150,align:'center'},
		        {field:'task_status',title:'<s:text name="complete_advisory_str5"></s:text>',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='3')
		        		  statusText = '<s:text name="complete_advisory_str6"></s:text>';
		        	  else if(value=='4')
		        		  statusText = '<s:text name="complete_advisory_str7"></s:text>';
		        	  return statusText;
		        }},
		        {field:'msg_type',hidden:true}, 
		        {field:'content',title:'<s:text name="complete_advisory_str8"></s:text>',width:150,align:'center',formatter:function(value,rowData,rowIndex){
		        	if(rowData.msg_type == "image"){
		        		 var temp1 = value.substring(value.indexOf('"')+1);
		        		 var temp2 = temp1.substring(0,temp1.indexOf('"'));
		        	     var imgUrl = "<img src=\"" + temp2 + "\" width=\"50px\" height=\"50px\"/>";
		        	} else {
		        		imgUrl = value;
		        	}
	        	  return imgUrl;
		        }},
		        {field:'service_score',title:'<s:text name="complete_advisory_str9"></s:text>',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	 var statusText='';
		        	  if(value=='5')
		        		  statusText = '<s:text name="complete_advisory_str10"></s:text>';
		        	  else if(value=='4')
		        		  statusText = '<s:text name="complete_advisory_str11"></s:text>';
		        	  else if(value=='3')
		        		  statusText = '<s:text name="complete_advisory_str12"></s:text>';
		        	  else if(value=='2')
		        		  statusText = '<s:text name="complete_advisory_str13"></s:text>';
		        	  else if(value=='1')
		        		  statusText = '<s:text name="complete_advisory_str14"></s:text>';
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
			$.messager.alert('<s:text name="complete_advisory_str15"></s:text>','<s:text name="complete_advisory_str16"></s:text>');
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

	//打开底层对话框
	function openChatWindow(itemid) {
		$('#openChatDiv').window({    		
            cache:false,
             width:740,
            height:520, 
            /*   width:$(window).width()/2 + 200,
            height:$(window).height()/1.2,*/
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
	        url:'${pageContext.request.contextPath}/weixin!searchTaskByIDAndKcode',
			queryParams : {				
				req_params : conditions
			},
			onLoadSuccess:function(data) { }
		});
	}
	
	//清空查询数据
	function clearConditions(){
		document.getElementById("code").value = "";
		document.getElementById("code").text = "";
		document.getElementById("kcode").value = "";
		document.getElementById("kcode").text = "";
	}
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div class="topDiv">
		<form id="conditions">
			<div class="conditionDiv">
				<label><s:text name="complete_advisory_str17"></s:text></label> 
				<input id="code" name="code" type="text" class="mytext" />
				<label><s:text name="complete_advisory_str18"></s:text></label> 
				<input id="kcode" name="kcode" type="text" class="mytext" />
	     	</div>		
		</form>
		<div style="padding-right:10px;text-align:right;">  
			<a id="btnClear" href="#" class="easyui-linkbutton" onclick="clearConditions();"><s:text name="complete_advisory_str19"></s:text></a>
			<a id="btnQuery" href="javascript:void(0);" class="easyui-linkbutton" onclick="searchAdviser();"><s:text name="complete_advisory_str20"></s:text></a>
		</div>
	</div>	
<div style="padding:15px;text-align:left;">  
			<a href="javascript:void(0);" class="easyui-linkbutton" onclick="openChatWindows();"><s:text name="complete_advisory_str21"></s:text></a> 
	</div>
<table id="tt_complete_advisory"></table>
<div id="openChatDiv" class="easyui-window" closed="true" title='<s:text name="complete_advisory_str21"></s:text>' collapsible="false" minimizable="false" maximizable="true">
      <div id="chat-container" class="easyui-tabs" data-options="fit:true,border:false" ></div>
</div>
</body>
</html>