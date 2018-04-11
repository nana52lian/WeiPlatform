<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>评价详情</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>
<script src="${pageContext.request.contextPath}/js/chat_helper.js"></script>  
<script>
$(function() {
	var service_id ="<%=request.getParameter("serviceID") %>";
	$('#tt_appraisal_info').datagrid({ 
		title:'咨询师评价详情',
		width:'auto',
		height:'auto',
		idField:'id',
		url:'${pageContext.request.contextPath}/adviserMgr!getAllAppraisalOfAdviser?serviceID='+ service_id,
		nowrap: false,
		striped: true,
		collapsible:false,
		rownumbers:true,
	    columns:[[  
	        {field:'id',hidden:true},
	        {field:'nickname',title:'昵称',width:90,align:'center'}, 
	        {field:'time',title:'评价时间',width:150,align:'center'},
	        {field:'task_status',title:'状态',width:100,align:'center',formatter:function(value,rowData,rowIndex){
	        	  var statusText='';
	        	  if(value=='3')
	        		  statusText = '已完成';
	        	  return statusText;
	        }},
	        {field:'score',title:'评价分数',width:100,align:'center'},
	        {field:'score_str',title:'评价结果',width:100,align:'center',formatter:function(value,rowData,rowIndex){
	        	 var statusText='';
	        	  if(rowData.score=='5')
	        		  statusText = '非常满意';
	        	  else if(rowData.score=='4')
	        		  statusText = '满意';
	        	  else if(rowData.score=='3')
	        		  statusText = '一般';
	        	  else if(rowData.score=='2')
	        		  statusText = '不满意';
	        	  else if(rowData.score=='1')
	        		  statusText = '非常不满意';
	        	  else if(rowData.score=='0')
	        		  statusText = '未评价';
	        	  return statusText;
	        }},
	        {field:'custom_asses_desc',title:'评价描述',width:200,align:'left'}
	    ]] ,
	    pagination:true,
		pageList:[10,20,30]	,
		frozenColumns:[[
			      		{field:'ck',checkbox:true} 
					]],
		onLoadSuccess:function(data){
			var div01 = $("td[field='custom_asses_desc'] div");  
            div01.css({  
	            "width":200,  
	            "white-space":"nowrap",  
	            "text-overflow":"ellipsis",  
	            "-o-text-overflow":"ellipsis",  
	            "overflow":"hidden"  
           	});
            $('#tt_appraisal_info').datagrid('doCellTip',{cls:{'background-color':'#FFFEE6'},delay:1000});
		}
	}); 
});

//客户服务按钮
function openChatWindows(){
	var rows = $('#tt_appraisal_info').datagrid("getSelections");
	if (rows.length < 1) {			
		$.messager.alert('警告','您没有选择任何记录，无法进行操作!');
		return;
	}
	for(var i=0;i<rows.length;i++){
		//加载对话Tab
		addTabPane(rows[i].open_id,
				   rows[i].nickname,
				   '${pageContext.request.contextPath}/weixin!openHistoryWindow?taskID=' + rows[i].id + '&flag=1'
				);
	}
}

//打开底层对话框
function openChatWindow(itemid) {
	$('#openChatDiv').window({    		
        modal:false,
        cache:false,
        width:825,
        height:565,
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
</script>  
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding:15px;text-align:left;">  
			<a href="javascript:void(0);" class="easyui-linkbutton" onclick="openChatWindows();">聊天记录</a> 
</div>
<table id="tt_appraisal_info"></table>
<div id="openChatDiv" class="easyui-window" closed="true" title="聊天记录" collapsible="false" minimizable="false" maximizable="true">
      <div id="chat-container" class="easyui-tabs" data-options="fit:true,border:false" ></div>
</div>
</body>
</html>