<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!--[if IE]>
<bgsound id="snd" src='' loop="0">
<![endif]-->
<![if !IE]>
<audio id="snd" src='' autoplay="autoplay">
</audio>
<![endif]>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="main_str5"></s:text></title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>
<script src="${pageContext.request.contextPath}/js/chat_helper.js"></script>  
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
		
		document.getElementById('busy').innerText = '<s:text name="uncomplete_advisory_str1"></s:text>';
		// 设置全局的ajax访问，处理ajax清求时sesion超时
		//ajaxSetupEx('${pageContext.request.contextPath}');	
		//取得登录人类型,并判断是否隐藏按钮
		$.post("${pageContext.request.contextPath}/weixin!getLoginType",
		    	{},
		    	function(data){	
		    		//并判断是否隐藏按钮
                    if(data.type == "3"){
                    	document.getElementById('setTask').style.display = "";
                    	document.getElementById('setBusy').style.display = "none";
                    } else  {
                    	document.getElementById('setTask').style.display = "none";
                    	document.getElementById('setBusy').style.display = "";
                    	if(data.busyStatus=="1"){
       		    			document.getElementById('busy').innerText = '<s:text name="uncomplete_advisory_str3"></s:text>';
       		    		} else {
       		    			document.getElementById('busy').innerText = '<s:text name="uncomplete_advisory_str1"></s:text>';
       		    		}
                    }
	    			//定时刷新
	    			setInterval("dataReload()",10000);
		    	},
		    	"json"
	    ); 
		//分配与处理中展示列表
		getUnCompleteAdvisory();
		//挂起
		gettWaitAdvisory();
		$(".tabs-title").live("click",function(){
			//alert($(this).html());
			var txts = $(this).html();
			//alert(txts);
			$(this).html(txts.replace('<sapn style="color:red">','').replace('</sapn>',''));
		})
		
	});
	// 刷新列表
	function dataReload(){
		$('#tt_uncomplete_advisory').datagrid("reload");
		$('#tt_wait_advisory').datagrid("reload");
	}

	// 分配与处理中展示列表
	var map = {};
	function getUnCompleteAdvisory(){
		$('#tt_uncomplete_advisory').datagrid({ 
			title:'<s:text name="uncomplete_advisory_str4"></s:text>',
			width:'auto',
			height:'auto',
			idField:'id',
			url:'${pageContext.request.contextPath}/weixin!getUnCompleteAdvisory',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			/* singleSelect:true, */
		    columns:[[  
		        {field:'id',hidden:true},
		        /* {field:'open_id',hidden:true},*/
		        {field:'open_id',title:'<s:text name="uncomplete_advisory_str5"></s:text>',width:90,align:'center'}, 
		        {field:'create_date',title:'<s:text name="uncomplete_advisory_str6"></s:text>',width:150,align:'center'},
		        {field:'task_status',title:'<s:text name="uncomplete_advisory_str7"></s:text>',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='1')
		        		  statusText = '<s:text name="uncomplete_advisory_str9"></s:text>';
		        	  else if(value=='2')
		        		  statusText = '<s:text name="uncomplete_advisory_str10"></s:text>';
		        	  return statusText;
		        }},
		        {field:'msg_type',hidden:true}, 
		        {field:'content',title:'<s:text name="uncomplete_advisory_str8"></s:text>',width:200,align:'left',formatter:function(value,rowData,rowIndex){
		        	var content = "";
		        	if(rowData.msg_type == "image"){
		        		 var temp1 = value.substring(value.indexOf('"')+1);
		        		 var temp2 = temp1.substring(0,temp1.indexOf('"'));
						 if(temp2 = "_blank"){
                                var temp1 = value.substring(value.indexOf('c=')+3);
                                var temp2 = temp1.substring(0,temp1.indexOf('"'));
                         }
		        		 content = "<img src=\"" + temp2 + "\" width=\"50px\" height=\"50px\"/>";
		        	} else if (rowData.msg_type == "voice") {
		        		content = '<s:text name="uncomplete_advisory_str11"></s:text>';
		        	} else {
		        		content = value;
		        	}
		        	map[rowData.id] = rowData.create_user;
		        	alert(rowData.id1+'ss'+map[rowData.id1]);
		        	alert(rowData.open_id+'ss'+map[rowData.id])
		        	if(map[rowData.id1] != rowData.id1 && map[rowData.id] == rowData.open_id){
		        		playSound('${pageContext.request.contextPath}/images/msg.wav');
		        	}

		        	map[rowData.id1] = rowData.id1;
		        	
	        	  	return content;
		        }}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]],
			onLoadSuccess:function(data){
				var div01 = $("td[field='content'] div");  
	            div01.css({  
		            "width":200,  
		            "white-space":"nowrap",  
		            "text-overflow":"ellipsis",  
		            "-o-text-overflow":"ellipsis",  
		            "overflow":"hidden"  
            	});
	            for(var i = 0;i<data.rows.length;i++){
	            	if(data.rows[i].task_status == '1'){
	            		playSound('${pageContext.request.contextPath}/images/msg.wav');
	            	}
	            }
			}	
		});
	}

	//响提示音
	function playSound(src){ 
		var _s = document.getElementById('snd'); 
		if(src!='' && typeof src!=undefined){ 
			_s.src = src; 
		}
	}
	
	// 挂起
	function gettWaitAdvisory(){
		$('#tt_wait_advisory').datagrid({ 
			title:'<s:text name="uncomplete_advisory_str12"></s:text>',
			width:'auto',
			height:'auto',
			idField:'id',
			url:'${pageContext.request.contextPath}/weixin!getWaitAdvisory',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			/* singleSelect:true, */
		    columns:[[  
		        {field:'id',hidden:true},
		        /* {field:'open_id',hidden:true}, */
		        {field:'open_id',title:'<s:text name="uncomplete_advisory_str5"></s:text>',width:90,align:'center'}, 
		        {field:'create_date',title:'<s:text name="uncomplete_advisory_str6"></s:text>',width:150,align:'center'},
		        {field:'task_status',title:'<s:text name="uncomplete_advisory_str7"></s:text>',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='5')
		        		  statusText = '<s:text name="uncomplete_advisory_str13"></s:text>';
		        	  else if(value=='6')
		        		  statusText = '<s:text name="uncomplete_advisory_str14"></s:text>';
		        	  return statusText;
		        }},
		        {field:'msg_type',hidden:true}, 
		        
		        {field:'maxAlterFlag',hidden:true,formatter:function(value,rowData,rowIndex){
		        	if(value=='1')
		        		alert('<s:text name="uncomplete_advisory_str15"></s:text>');
		        }}, 
		        {field:'content',title:'<s:text name="uncomplete_advisory_str8"></s:text>',width:200,align:'left',formatter:function(value,rowData,rowIndex){
		        	var content = "";
		        	if(rowData.msg_type == "image"){
		        		 var temp1 = value.substring(value.indexOf('"')+1);
		        		 var temp2 = temp1.substring(0,temp1.indexOf('"'));
		        		 if(temp2 = "_blank"){
                             var temp1 = value.substring(value.indexOf('c=')+3);
                             var temp2 = temp1.substring(0,temp1.indexOf('"'));
                      }
		        		 content = "<img src=\"" + temp2 + "\" width=\"50px\" height=\"50px\"/>";
		        	} else if (rowData.msg_type == "voice") {
		        		content = '<s:text name="uncomplete_advisory_str11"></s:text>';
		        	} else {
		        		content = value;
		        	}
	        	  	return content;
	        }}
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
				{field:'ck',checkbox:true} 
			]],
			onLoadSuccess:function(data){
				var div01 = $("td[field='content'] div");  
	            div01.css({  
		            "width":200,  
		            "white-space":"nowrap",  
		            "text-overflow":"ellipsis",  
		            "-o-text-overflow":"ellipsis",  
		            "overflow":"hidden"  
            	});
			}
		});
	}
	
	//兼职人员手动获取任务
	function setTask(){
		$.post("${pageContext.request.contextPath}/weixin!getTaskForAmateur",
		    	{},
		    	function(data){	
		    		if(data.maxcount=="0"){
			    		//alert('<s:text name="uncomplete_advisory_str16"></s:text>');
			    		$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="uncomplete_advisory_str16"></s:text>');
		    		} else if (data.maxcount=="1"){
			    		//alert('<s:text name="uncomplete_advisory_str17"></s:text>');
			    		$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="uncomplete_advisory_str17"></s:text>');
		    		} else if (data.maxcount=="2"){
		    			//alert('<s:text name="uncomplete_advisory_str18"></s:text>');
		    			$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="uncomplete_advisory_str18"></s:text>');
		    		}
		    	},
		    	"json"
	    ); 
	}
	
	//客户服务按钮
	function openChatWindows(){
		var rows = $('#tt_uncomplete_advisory').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('<s:text name="uncomplete_advisory_str19"></s:text>','<s:text name="uncomplete_advisory_str20"></s:text>');
			return;
		}
		for(var i=0;i<rows.length;i++){
			//加载对话Tab
			addTabPane(rows[i].open_id,
					   rows[i].open_id,
					   '${pageContext.request.contextPath}/weixin!openChatWindow?taskID=' + rows[i].id
					);
		}
		$('#tt_uncomplete_advisory').datagrid("uncheckAll");
	}

	//打开底层对话框
	function openChatWindow(itemid) {
		$('#openChatDiv').window({    		
            cache:false,
            width:760,
            height:575,
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
	    
	        /* tt.tabs({
				onSelect:function(title,index){
		        	alert(title);
	    			currentTab = tt.tabs('getTab',title);
	    			tt.tabs('update', {
	    				tab: currentTab,
	    				options: {
	    					title: "bbbbb"
	    				}
	    			});
		        }
			}); */
	    
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
	 
	//查看按钮事件，由于显示内容不同
		function preview(){
			var rows = $('#tt_wait_advisory').datagrid("getSelections");
			if (rows.length < 1) {			
				$.messager.alert('<s:text name="uncomplete_advisory_str19"></s:text>','<s:text name="uncomplete_advisory_str20"></s:text>');
				return;
			}
			for(var i=0;i<rows.length;i++){
				//加载对话Tab
				addTabPane(rows[i].open_id,
						   rows[i].open_id,
						   '${pageContext.request.contextPath}/weixin!openHistoryWindow?taskID=' + rows[i].id
						);
			}
		}
	
	//坐席员设置繁忙状态
	function setBusy(){
		var statusFlag = "1";
		if(document.getElementById('busy').innerText == '<s:text name="uncomplete_advisory_str1"></s:text>'){
			statusFlag = "1";
		} else {
			statusFlag = "0";
		}
		var params = {
				status	: statusFlag
		};
		$.post("${pageContext.request.contextPath}/weixin!setBusyStatus",
		    	{
			        req_params : JSON.stringify(params)
		    	},
		    	function(data){	
		    		//alert('<s:text name="uncomplete_advisory_str2"></s:text>');
		    		$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="uncomplete_advisory_str2"></s:text>');
		    		if(statusFlag == "1"){
		    			document.getElementById('busy').innerText = '<s:text name="uncomplete_advisory_str3"></s:text>';
		    		} else {
		    			document.getElementById('busy').innerText = '<s:text name="uncomplete_advisory_str1"></s:text>';
		    		}
		    		
		    	},
		    	"json"
	    ); 
	}
	
	function openServiceAndLeaderWindow(tableId){
		
		var selectRow = $('#'+tableId).datagrid("getSelected");
		if(selectRow!=null){
			$('#tt_service').datagrid({ 
				title:'<s:text name="uncomplete_advisory_str21"></s:text>',
				width:'auto',
				height:'auto',
				idField:'id',
				nowrap: false,
				striped: true,
				collapsible:false,
				rownumbers:true,
				autoSizeColumn:true, 
				singleSelect:true,
		        iconCls:'icon-save',
			    url:'${pageContext.request.contextPath}/weixin!getAllService',
			    columns:[[  
			        {field:'id',hidden:true},
			        {field:'account',title:'<s:text name="uncomplete_advisory_str22"></s:text>',width:100,align:'left'},
			        {field:'name',title:'<s:text name="uncomplete_advisory_str23"></s:text>',width:100,align:'left'},
			        {field:'sex',title:'<s:text name="uncomplete_advisory_str24"></s:text>',width:100,align:'left',formatter:function(value){
			        	var statusText='';
		        	  	if(value=='0')
		        		  	statusText = '<s:text name="uncomplete_advisory_str25"></s:text>';
		        	  	else if(value=='1')
		        		  	statusText = '<s:text name="uncomplete_advisory_str26"></s:text>';
		        	  	return statusText;
			        }},
			        {field:'mobile_phone',title:'<s:text name="uncomplete_advisory_str27"></s:text>',width:100,align:'left'},
			    ]],
			    pagination:true,
		        pageList:[10,20,30],
		        frozenColumns:[[
		           {field:'ck',checkbox:true} 
		        ]],
		        onSelectRow:function(rowIndex,rowData){
		        	//var conditions = JSON.stringify(serializeObject($("#conditions")));
		        	$('#tt_leader').datagrid('options').url = "${pageContext.request.contextPath}/weixin!getCurrentLeader";
		        	$('#tt_leader').datagrid('options').queryParams = {'id':rowData.id};
		    		$('#tt_leader').datagrid('reload');
		        },
		        onCheck:function(rowIndex,rowData){
		        	//var conditions = JSON.stringify(serializeObject($("#conditions")));
		        	$('#tt_leader').datagrid('options').url = "${pageContext.request.contextPath}/weixin!getCurrentLeader";
		        	$('#tt_leader').datagrid('options').queryParams = {'id':rowData.id};
		    		$('#tt_leader').datagrid('reload');
		        }
			});
			
			$('#tt_leader').datagrid({ 
				title:'<s:text name="uncomplete_advisory_str28"></s:text>',
				width:'auto',
				height:'auto',
				idField:'id',
				nowrap: false,
				striped: true,
				collapsible:false,
				rownumbers:true,
				autoSizeColumn:true, 
				singleSelect:false,
		        iconCls:'icon-save',
			    //url:'${pageContext.request.contextPath}/weixin!getCurrentLeader',
			    columns:[[  
			        {field:'id',hidden:true},
			        {field:'account',title:'<s:text name="uncomplete_advisory_str22"></s:text>',width:100,align:'left'},
			        {field:'name',title:'<s:text name="uncomplete_advisory_str23"></s:text>',width:100,align:'left'},
			        {field:'sex',title:'<s:text name="uncomplete_advisory_str24"></s:text>',width:100,align:'left',formatter:function(value){
			        	var statusText='';
		        	  	if(value=='0')
		        		  	statusText = '<s:text name="uncomplete_advisory_str25"></s:text>';
		        	  	else if(value=='1')
		        		  	statusText = '<s:text name="uncomplete_advisory_str26"></s:text>';
		        	  	return statusText;
			        }},
			        {field:'mobile_phone',title:'<s:text name="uncomplete_advisory_str27"></s:text>',width:100,align:'left'},
			    ]],
		        frozenColumns:[[
		           {field:'ck',checkbox:true} 
		        ]]
			});  
			
			$("#taskId").val(selectRow.id);
			
			$('#serviceDiv').window({  
		           modal:true,
		           width:700,
		           height:600,
		           left:50,
		           top:50
		    });
			$('#serviceDiv').window('open');
		} else {
			$.messager.alert('<s:text name="uncomplete_advisory_str19"></s:text>','<s:text name="uncomplete_advisory_str20"></s:text>');
			return;
		}
	}
	
	function transferTask(){
		var selectRows = $('#tt_leader').datagrid("getSelections");
		if(selectRows.length>0){
			var taskId = $("#taskId").val();
			var serviceSelectRow = $('#tt_service').datagrid("getSelected");
			var transferToId = serviceSelectRow.id;
			var leaderIds=[];
			for(var key in selectRows){
				var leaderId = selectRows[key].id;
				leaderIds.push(leaderId);
			}
			$.post("${pageContext.request.contextPath}/weixin!transferTask",{taskID:taskId,leaderIds:JSON.stringify(leaderIds),transferToId:transferToId},function(data){
				//alert(data.resDesc);
				$.messager.alert('<s:text name="INFO"></s:text>',data.resDesc);
				close('serviceDiv');
			},"json");
		}
		else{
			//alert('<s:text name="uncomplete_advisory_str29"></s:text>');
			$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="uncomplete_advisory_str29"></s:text>');
		}
	}
	function closeWin(id){
		$('#'+id).window('close');
		 $('#tt_leader').datagrid("clearSelections");
		 $('#tt_service').datagrid("clearSelections");
	}
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
	<div style="padding:15px;text-align:left;">  
			<a href="javascript:void(0);" class="easyui-linkbutton" onclick="openChatWindows();"><s:text name="customer_service"></s:text></a> 
			<a href="javascript:void(0);" class="easyui-linkbutton" onclick="setTask();" id="setTask" ><s:text name="uncomplete_advisory_str30"></s:text></a> 
			<a href="javascript:void(0);" class="easyui-linkbutton" onclick="setBusy();" id="setBusy" style="cursor: pointer;"><label id="busy" style="cursor: pointer;"></label></a> 
			<a href="javascript:void(0);" class="easyui-linkbutton" onclick="openServiceAndLeaderWindow('tt_uncomplete_advisory');"><s:text name="uncomplete_advisory_str31"></s:text></a> 
	</div>
   <table id="tt_uncomplete_advisory"></table>
   <div id="openChatDiv" class="easyui-window" closed="true" title='<s:text name="uncomplete_advisory_str32"></s:text>' collapsible="false" minimizable="false" maximizable="true">
      <div id="chat-container" class="easyui-tabs" data-options="fit:true,border:false" ></div>
   </div> 
   <div style="height: 100px;"></div>
   <div style="padding:15px;text-align:left;">  
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="preview();"><s:text name="uncomplete_advisory_str33"></s:text></a> 
	</div>
   <table id="tt_wait_advisory"></table>
   <div id="serviceDiv" class="easyui-window" closed="true" title='<s:text name="uncomplete_advisory_str34"></s:text>' style="padding:10px">
		<input id="taskId" style="display: none;">
		<table id="tt_service"></table>
		<br/>
		<table id="tt_leader"></table>
		<br/>
		<input type="button" value="Transfer" onclick="transferTask()">
		<input type="button" value="Close" onclick="closeWin('serviceDiv')">
	</div>
</body>
</html>