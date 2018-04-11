<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>聊天</title>
<style type="text/css">  
.ifile {
	position:absolute;
    cursor: pointer;
    direction: rtl;
    font-size: 30px;
    height: 35px;
    opacity: 0;
    filter: alpha(opacity=0); 
    width: 25px;
    z-index:100000;
} 
.itext {
	position:absolute;
    cursor: pointer;
    direction: rtl;
    font-size: 12px;
    height: 30px;
    line-height:34px;
    width: 60px;
    z-index:100000;
    margin-left:310px;
    text-align:center;
    text-decoration: none;
    color:#996633;
    hover:#996633;
    visited:#996633;
}
.commonButton {
	-webkit-border-radius: 3px;
	border:1px solid #D8D196;
	background-color:#FFFDDF;
	padding:3px 6px;
	color:#875f0e;
	font-size: 13px;
	font-family:微软雅黑;
	font-weight:bold;
}
.dangerButton {
	-webkit-border-radius: 3px;
	border:1px solid #FF8A8A;
	background-color:#FEBBC4;
	padding:3px 6px;
	color:#990a14;
	font-size: 13px;
	font-family:微软雅黑;
	font-weight:bold;
}
.chat_log_div a:hover{ border:1px solid #D4D4D4; }
</style> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ueditor/ueditor.config.js"></script>   
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ueditor/ueditor.all.js"></script>
<script src="${pageContext.request.contextPath}/js/chat_helper.js"></script>
<script src="${pageContext.request.contextPath}/js/1bit/swfobject.js"></script>
<script src="${pageContext.request.contextPath}/js/1bit/1bit.js"></script>
<script type="text/javascript">

	var listMessage = new Array();
	var taskID = '<s:property value="list_message[0].task_queue_id" />';
	var openID = '<s:property value="list_message[0].open_id"/>';
	var flagVal = '<s:property value="flag"/>';

	// 显示框
	var show_message_box;
	
	$(function () {
		//alert('<%=session.getAttribute("lang")%>');
		if(flagVal==1){
			document.getElementById('btnDiv').style.display = "none";
			
		}
		if(flagVal==2){
			document.getElementById('btnDiv').style.display = "none";
			document.getElementById('closeDiv').style.display = "";
			
		}
		if (flagVal==0){
			$('#keyAccount').attr({"readonly":false});
			$('#rid').attr({"readonly":false});
		}
		initShowMessageBox();
		//取得微信用户信息
		
		$('#requestType').tree({
			url:'${pageContext.request.contextPath}/weixin!getRequestType',
			onLoadSuccess:function(){
				$('#requestType').tree("collapseAll");
				document.getElementById('keyAccount').focus();
			},
			onClick: function(node){
				$("#typetxt").val(node.text);
				$("#typeidtxt").val(node.id);
				$('#typetxtDiv').window('close');
			}
		});
		
		
	});
	
	//取得微信用户信息
	function getWeixinUserInfo(){
		var params = {
				task_queue_id	: taskID
			};
		$.post("${pageContext.request.contextPath}/weixin!getUserAdvisoryInfo",
		    	{
					req_params : JSON.stringify(params)
		    	},
		    	function(data){	
		    		document.getElementById('ridPrefix').innerText=data.ridPrefix;
		    		if(data.size == 1){
		    			document.getElementById('keyAccount').value=data.key_account;
			    		document.getElementById('name').value=data.name;
		    			if(data.sex==1){
		    				$('input:radio[name=sex]:nth(0)').attr('checked',true); 
		    				/* document.getElementById('sex').innerText = '<s:text name="chat_window_str34"></s:text>'; */
		    			} else if (data.sex==2){
		    				$('input:radio[name=sex]:nth(1)').attr('checked',true); 
		    				/* document.getElementById('sex').innerText = '<s:text name="chat_window_str35"></s:text>'; */
		    			}
		    			/* document.getElementById('age').value=data.age;
			    		document.getElementById('grade').value=data.grade;
			    		document.getElementById('department').value=data.department;
			    		document.getElementById('area').value=data.area; */
			    		document.getElementById('cellphone').value=data.cellphone;
			    		document.getElementById('email').value=data.email;
			    		document.getElementById('usertype').value=data.usertype;
			    		/* document.getElementById('activestatus').innerText=data.activestatus; */
			    		document.getElementById('rid').value=data.rid;
			    		//$('#selectorType').combobox("setValue",data.type );
			    		document.getElementById('typetxt').value=data.type;
			    		if(data.customScore == 1){
			    			$(".selectorScore").val("1");
			    			$(".selectorScore").find("option[text='1']").attr("selected",true);
			    		} else if(data.customScore == 2){
			    			$(".selectorScore").val("2");
			    			$(".selectorScore").find("option[text='2']").attr("selected",true);
			    		} else if(data.customScore == 3){
			    			$(".selectorScore").val("3");
			    			$(".selectorScore").find("option[text='3']").attr("selected",true);
			    		} else if(data.customScore == 4){
			    			$(".selectorScore").val("4");
			    			$(".selectorScore").find("option[text='4']").attr("selected",true);
			    		} else if(data.customScore == 5){
			    			$(".selectorScore").val("5");
			    			$(".selectorScore").find("option[text='5']").attr("selected",true);
			    		} else {
			    			$(".selectorScore").val("0");
			    			$(".selectorScore").find("option[text='<s:text name="chat_window_str1"></s:text>']").attr("selected",true);
			    		}
		    		} else if(data.size == 2){
		    			/* if(data.sex==1){
		    				$('input:radio[name=sex]:nth(0)').attr('checked',true);
		    			} else if (data.sex==2){
		    				$('input:radio[name=sex]:nth(1)').attr('checked',true);
		    			}
		    			document.getElementById('age').value=data.age; */
		    			//$('#selectorType').combobox("setValue",data.type );
			    		document.getElementById('typetxt').value=data.type;
			    		if(data.customScore == 1){
			    			$(".selectorScore").val("1");
			    			$(".selectorScore").find("option[text='1']").attr("selected",true);
			    		} else if(data.customScore == 2){
			    			$(".selectorScore").val("2");
			    			$(".selectorScore").find("option[text='2']").attr("selected",true);
			    		} else if(data.customScore == 3){
			    			$(".selectorScore").val("3");
			    			$(".selectorScore").find("option[text='3']").attr("selected",true);
			    		} else if(data.customScore == 4){
			    			$(".selectorScore").val("4");
			    			$(".selectorScore").find("option[text='4']").attr("selected",true);
			    		} else if(data.customScore == 5){
			    			$(".selectorScore").val("5");
			    			$(".selectorScore").find("option[text='5']").attr("selected",true);
			    		} else {
			    			$(".selectorScore").val("0");
			    			$(".selectorScore").find("option[text='<s:text name="chat_window_str1"></s:text>']").attr("selected",true);
			    		}
		    		} 
		    		//请求企业号平台，根据KID取个人信息
		    		getUserInfoForUserID();
		    	},
		    	"json"
	    ); 
	}
	
	function getUserInfoForUserID(){
    	var params = {
				user_id	: openID
			};
    	$.post("${pageContext.request.contextPath}/weixin!getUserInfoForUserID",
		    	{
					req_params : JSON.stringify(params)
		    	},
		    	function(data){	
		    		document.getElementById('keyAccount').value=data.kid;
		    		document.getElementById('name').value=data.name;
	    			if(data.gender=='M'){
	    				 $('input:radio[name=sex]:nth(0)').attr('checked',true); 
	    			} else if (data.sex==2){
	    				 $('input:radio[name=sex]:nth(1)').attr('checked',true); 
	    			}
		    		document.getElementById('cellphone').value=data.mobile;
		    		document.getElementById('email').value=data.email;
		    	},
		    	"json"
	    ); 
    }
	
	//未读消息
	function  getMessageData(){
		var params = {
				taskID	: taskID,
				open_id	: openID
			};
		$.post("${pageContext.request.contextPath}/weixin!getUnReadMessage",
		    	{
					req_params : JSON.stringify(params)
		    	},
		    	function(data){	
		    		for(var i=0;i<data.resultWeixinList.length;i++){
		    			var list = data.resultWeixinList[i];
		    			appendMessage(list.nick_name,list.service_id,list.content,list.create_user,list.create_date);
		    		}
		    	},
		    	"json"
	    ); 
	}
	
	// 初始化显示框
	function initShowMessageBox() {
		var editorOption = {
			toolbars:[['']],
           	contextMenu:[],
           	initialFrameHeight:416,
           	initialFrameWidth:380,
           	zIndex:900,
           	autoHeightEnabled:false,
           	onready:function () {
           		showHistory();
           	}
	    };
		show_message_box = new UE.ui.Editor(editorOption);
		show_message_box.render('show_message');
	}
		
	// 将聊天的历史记录加入到聊天框
	function showHistory() {
		for (var i=0; i<listMessage.length; i++) {
			var messageMap = listMessage[i];
			var content = messageMap["message"];
			if(content.search(".mp3")!=-1){
				content = '<span><s:text name="chat_window_str2"></s:text>'
					+ '<embed type="application/x-shockwave-flash" src="${pageContext.request.contextPath}/js/1bit/1bit.swf" width="10" height="10" bgcolor="#FFFFFF" quality="high" flashvars="foreColor=#000000&amp;analytics=false&amp;filename=${pageContext.request.contextPath}'+content+'">'
					+ '</span>';
	
			}
			appendMessage(messageMap["nick_name"], 
					        messageMap["service_id"],
					        content,
							messageMap["create_user"],
							messageMap["create_date"]);
		}		
	}
	
	// 将发送的消息加到主屏上显示
	function appendMessage(nick_name, service_id, msg, create_user, date) {
		var color = 'blue';
		// 自己发的消息显示绿色
		if (service_id == create_user) {
			color = "#008040";
			from_name = "我";
		} else if(create_user == "system"){
			color = "#008040";
			from_name = "System info";
		} else {
			// 如果是对方说话就显示对方名字
			from_name = nick_name;
		}
		msg = "<div style='font-size:12px;color:" + color + ";'>" + from_name + '&nbsp;&nbsp;' + date + "</div>"
		    + "<div style='font-size:12px;padding-bottom:5px;'>" + msg + "</div>";
		
		/** 向输出框输出文字信息开始 */
		show_message_box.enable();
		show_message_box.execCommand("insertHtml", msg, true);
	    show_message_box.disable(); 
	    /** 向输出框输出文字信息结束 */
	}

	// 将历史消息先保存到数组，等iframe加载完成后在放到对话框里
	function initHistoryMessage(index, nick_name, service_id, message, create_user, create_date) {		
		var messageMap = {};		
		messageMap["nick_name"] = nick_name;
		messageMap["service_id"] = service_id;
		messageMap["message"] = message;
		messageMap["create_user"] = create_user;
		messageMap["create_date"] = create_date;
		//listMessage[index] = messageMap; 
		listMessage.push(messageMap);
	}
	
	//关闭
	function complete(){
		var params = {
				task_queue_id	: taskID,
				task_type       : 'completed'
			};
		$.post("${pageContext.request.contextPath}/weixin!setStatus",
		    	{
					req_params : JSON.stringify(params)
		    	},
		    	function(data){	
		    		//刷新父页面
	    			parent.$('#tt_uncomplete_advisory').datagrid("reload");
	    		   	var tabsCon = window.parent.$('#chat-container');
		    		var tabsLength = tabsCon.tabs('tabs').length;
	    			var currentTab = tabsCon.tabs('getSelected');
	    			var index = tabsCon.tabs('getTabIndex',currentTab);
	    			
	    			if(tabsLength==1)
	    				parent.$('#openChatDiv').window('close');
	    			
	    			tabsCon.tabs('close',index);
		    	},
		    	"json"
	    );
	}
	
	//关闭窗口
	function close(){
		//刷新父页面
	   	var tabsCon = window.parent.$('#chat-container');
		var tabsLength = tabsCon.tabs('tabs').length;
		var currentTab = tabsCon.tabs('getSelected');
		var index = tabsCon.tabs('getTabIndex',currentTab);
		
		if(tabsLength==1)
			parent.$('#openChatDiv').window('close');
		
		tabsCon.tabs('close',index);
	}
	
	//发送评价连接
	function exit(){
		 var close_type = $('input[name="closetype"]:checked').val();	
			//正常关闭
			if(close_type == 1){
				
				var params = {
						task_queue_id	: taskID,
						open_id	        : openID,
						rid             : 0
					};
				$.post("${pageContext.request.contextPath}/weixin!getAppraisalUrl",
				    	{ 
					req_params : JSON.stringify(params)
					},
				    	function(data){	
				    		if(data.result == true){
				    		   	//alert('<s:text name="chat_window_str43"></s:text>');
				    		   	$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str43"></s:text>');
				    		   	complete();
				    		} 
				    	},
				    	"json"
			    );
			} else {
				//非正常关闭
				if(document.getElementById('abnormal_content').value == ""){
					//alert('<s:text name="chat_window_str19"></s:text>');
					$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str19"></s:text>');
					document.getElementById('abnormal_content').focus();
			    	return;   
				}  
				//改变状态
				complete();
				//添加非正常关闭原因
				var params = {
						task_queue_id	: taskID,
						cause           : document.getElementById('abnormal_content').value
					};
				$.post("${pageContext.request.contextPath}/weixin!SetCause",
				    	{ 
					req_params : JSON.stringify(params)
					},
				    	function(data){	
				    		if(data.result){
				    			
				    		
				    		} 
				    	},
				    	"json"
			    );
			}
	}
	
	function exitDiv(){
		$('#openCloseWindowsDiv').window({    	
			href:'admin/weixin/close_window.jsp', 
            modal:true,
            cache:false,
            width:300,
            height:250,
            left:180,
            top:100,
            onLoad:function(){
            	document.getElementsByName('closetype')[0].checked=true;//默认选中正常 
            	document.getElementById('abnormal_content').disabled =true;
            }
        });   		
		$('#openCloseWindowsDiv').window('open');
	}
	
	//正常关闭
	function normal(){
		document.getElementById('abnormal_content').disabled =true;
	}
	
	//非正常关闭
	function abnormal(){
		document.getElementById('abnormal_content').disabled =false;
	}
	
	//保存用户信息
	function saveUserInfo(){
		if(document.getElementById('keyAccount').value == ""){
			//alert('<s:text name="chat_window_str4"></s:text>');
			$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str4"></s:text>');
			document.getElementById('keyAccount').focus();
	    	return;   
		} 
		
		if(document.getElementById('rid').value == ""){
			//alert(<s:text name="chat_window_str12"></s:text>);
			$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str12"></s:text>');
			document.getElementById('rid').focus();
	    	return;   
		}  
		if($("#typetxt").val() == ""){
			//alert('<s:text name="chat_window_str13"></s:text>');
			$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str13"></s:text>');
	    	return;   
		}  
		if(document.getElementById('customScore').value == ""){
			//alert('<s:text name="chat_window_str14"></s:text>');
			$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str14"></s:text>');
			document.getElementById('customScore').focus();
	    	return;   
		}  
		var params = {
				task_queue_id	: taskID,
				open_id		    : openID, 
				keyAccount	    : document.getElementById('keyAccount').value,
				name		    : document.getElementById('name').value,
				sex			    : $('input[name="sex"]:checked').val(),
				usertype		: document.getElementById('usertype').value,
				cellphone		: document.getElementById('cellphone').value,
				email		    : document.getElementById('email').value,
				rid				: 0,
				type			: $("#typetxt").val(),
				customScore	    : $('#customScore').val()
			};
		$.post("${pageContext.request.contextPath}/weixin!saveUserInfo",
		    	{
					req_params : JSON.stringify(params)
		    	},
		    	function(data){	
		    		if(data.result>0){
		    			//alert('<s:text name="chat_window_str15"></s:text>');
		    			$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str15"></s:text>');
		    		};
		    	},
		    	"json"
	    );    
	}
	function openRequestTypeWindow(){
		$('#typetxtDiv').window({  
	           modal:true,
	           width:200,
	           height:'auto',
	           left:200,
	           top:200,
	           zIndex:999999999999
	    });
		
		$('#typetxtDiv').window('open');
	}
	
	
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
    <s:iterator value="list_message" status="index">
			<script>
			initHistoryMessage(<s:property value="#index.index" />, '<s:property value="nick_name" />',  '<s:property value="service_id" />', 
					'<s:property value="content.replaceAll('\r\n','<br/>').replaceAll('\r','</br>').replaceAll('\n','<br/>')" escape="false" />', '<s:property value="create_user" />','<s:property value="create_date" />');
			</script>
	</s:iterator>
	<div style="float:left;width:100%;height:100%;border:solid 0px red;overflow: hidden;">
	        <div style="float:left;">
				<div>
					<textarea name="show_message" id="show_message"></textarea>				
				</div>
	                <div id="btnDiv" style="padding-top: 5px;padding-bottom:5px; padding-left: 15px;font-weight: bold;text-align:center;">
				    	<input type="button" class="dangerButton" value='<s:text name="chat_window_str42"></s:text>' onclick="exitDiv()" style="cursor: pointer;"/>
				    </div>
				    <div id="closeDiv" style="display:none; padding-top: 5px;padding-bottom:5px; padding-left: 15px;font-weight: bold;text-align:center;">
				    	
				    	<input type="button" class="dangerButton" value='<s:text name="chat_window_str42"></s:text>' onclick="close()" style="cursor: pointer;"/>
				    </div>
				    <div id="btnSaveDiv" style="display:none; padding-top: 5px;padding-bottom:5px; padding-left: 15px;font-weight: bold;text-align:center;" >
				    	
				    	<input type="button" class="commonButton" value='<s:text name="chat_window_str39"></s:text>' onclick="saveUserInfo()" style="cursor: pointer;"/>
				    </div>
				    <div id="openCloseWindowsDiv" class="easyui-window" closed="true" title='<s:text name="chat_window_str42"></s:text>' collapsible="false" minimizable="false" maximizable="false">
			        </div>
			</div>
	</div>
	<div style="clear:both;"></div>
</body>
</html>