<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<![if !IE]>
<audio id="snd" src='' autoplay="autoplay">
</audio>
<![endif]>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>聊天</title>
<meta http-equiv=”Access-Control-Allow-Origin” content=”*”>
<style type="text/css">  

.edui-editor-bottombar { display: none!important}

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
.chat_log_div a:hover{ border:1px solid #D4D4D4; }

#buttonDiv {
	text-align:center;
}

#divBaseInfo input[type="text"] {
	border:1px solid #ACACB2;
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
</style> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>
<script src="${pageContext.request.contextPath}/js/ueditor/ueditor.config.js"></script>   
<script src="${pageContext.request.contextPath}/js/ueditor/ueditor.all.js"></script>
<script src="${pageContext.request.contextPath}/js/chat_helper.js"></script>
<script src="${pageContext.request.contextPath}/js/1bit/swfobject.js"></script>
<script src="${pageContext.request.contextPath}/js/1bit/1bit.js"></script>

<script type="text/javascript">

	var listMessage = new Array();
	var taskID = '<s:property value="list_message[0].task_queue_id" />';
	var openID = '<s:property value="list_message[0].open_id"/>';
	// 输入框
	var input_message_box;
	// 显示框
	var show_message_box;
	// 聊天记录
	var history_message_box;
	
	$(function () {
		//alert('<%=session.getAttribute("lang")%>');
		
		initInputMessageBox();
		initShowMessageBox();
		
		oneBit = new OneBit('${pageContext.request.contextPath}/js/1bit/1bit.swf');
		oneBit.ready(function() {
			oneBit.specify('color', '#000000');
			oneBit.specify('background', '#FFFFFF');
			oneBit.specify('playerSize', '10');
			oneBit.specify('position', 'after');
			oneBit.specify('analytics', false);
			oneBit.apply('a');
		});
		
		//initHistoryMessageBox();		
		//document.getElementsByName('sex')[0].checked=true;//默认选中男 
		//获取请求类型
		getRequestType();
		//定时刷新聊天数据 10秒
		setInterval("getMessageData()",5000);
		
		//取得微信用户咨询信息
		//getUserAdvisoryInfo();
		//取得微信用户咨询次数
		//getUserAdvisoryCount();
		
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
	
	//取得微信用户咨询信息
	function getUserAdvisoryInfo(){
		var params = {
				task_queue_id : taskID,
				open_id	: openID
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
		    				/* document.getElementById('sex').value = '<s:text name="chat_window_str34"></s:text>'; */
		    			} else if (data.sex==2){
		    				 $('input:radio[name=sex]:nth(1)').attr('checked',true); 
		    				/* document.getElementById('sex').value = '<s:text name="chat_window_str35"></s:text>'; */
		    			}
		    			/* document.getElementById('age').value=data.age;
			    		document.getElementById('grade').value=data.grade;
			    		document.getElementById('department').value=data.department;
			    		document.getElementById('area').value=data.area; */
			    		document.getElementById('cellphone').value=data.cellphone;
			    		document.getElementById('email').value=data.email;
			    		document.getElementById('usertype').value=data.usertype;
			    		/* document.getElementById('activestatus').value=data.activestatus; */
			    		document.getElementById('rid').innerText=data.rid;
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
	
	//取得微信用户咨询次数
	function getUserAdvisoryCount(){
		var params = {
				taskID	: taskID,
				open_id	: openID
			};
		$.post("${pageContext.request.contextPath}/weixin!getUserAdvisoryCount",
		    	{
					req_params : JSON.stringify(params)
		    	},
		    	function(data){	
		    		//document.getElementById('statistics').innerText=data.count;
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
		    			var content = list.content;
						if(content.search(".mp3")!=-1){
				content = 
								'<span><s:text name="chat_window_str2"></s:text>'
								+'<embed id="tttt" type="application/x-shockwave-flash" src="${pageContext.request.contextPath}/js/1bit/1bit.swf"' 
								+'width="10" height="10" bgcolor="#FFFFFF" quality="high" flashvars="foreColor=#000000&amp;analytics=false&amp;'
								+'filename=${pageContext.request.contextPath}/voice_files/'+content+'">'
								+ '</span>';
						}
		    			appendMessage(list.nick_name,list.service_id,content,list.create_user,list.create_date);
		    			playSound('${pageContext.request.contextPath}/images/msg.wav');
		    		    
		    		   	var tabsCon = window.parent.$('#chat-container');
		    		   	
		    		   
		    			currentTab = tabsCon.tabs('getTab',openID);
		    			
		    			/* 
		    			var  a = tabsCon.tabs('getTabIndex',currentTab)
		    			alert(a); */
		    			
		    			if(currentTab==null){
		    				currentTab = tabsCon.tabs('getTab',"<sapn style='color:red'>"+openID+"</span>");
		    			} 
		    			//alert(currentTab);
		    		    tabsCon.tabs('update', {
		    				tab: currentTab,
		    				options: {
		    					title: "<span style='color:red'>"+openID+"</span>"
		    				}
		    			});  
		    		}
		    		if(data.resultWeixinList.length>0){
		    			
		    			input_message_box.focus(true);
		    			
		    			/* console.log("新消息");
		            		title_default = $("title").text() ;
		            		console.log(title_default);
		                    show = "【新消息】"+title_default ;
		                    $("title").text(show);  */
		    		}
		    	},
		    	"json"
	    ); 
	}
	
	//响提示音
	function playSound(src){ 
		var _s = document.getElementById('snd'); 
		if(src!='' && typeof src!=undefined){ 
			_s.src = src; 
		}
	}
	
	// 初始化输入框
	function initInputMessageBox() {
		var editorOption = {
			toolbars:[['insertimage']],
           	contextMenu:[],
           	initialFrameHeight:100,
           	initialFrameWidth:380,
           	zIndex:900,
           	onready:function () {
           		input_message_box.focus();
           		UE.dom.domUtils.on(input_message_box.document.body,"keydown",function(e){
           			//if(e.ctrlKey && e.keyCode == 13){           				
           			if(e.keyCode == 13){
           				sendMsg();
           				return false;
           			}
           		});           		
           	}
	    };
		input_message_box = new UE.ui.Editor(editorOption);
		input_message_box.render('input_message');
	}
	
	// 初始化显示框
	function initShowMessageBox() {
		var editorOption = {
			toolbars:[['']],
           	contextMenu:[],
           	initialFrameHeight:305,
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
		
/* 	// 初始化历史消息
	function initHistoryMessageBox() {
		var editorOption = {
			toolbars:[['']],
           	contextMenu:[],
           	initialFrameHeight:443,
           	initialFrameWidth:380,
           	autoHeightEnabled:false,
           	onready:function () {
           		
           	}
	    };
		history_message_box = new UE.ui.Editor(editorOption);
		history_message_box.render('history_message');
	} */
		
	String.prototype.replaceAll = function (AFindText,ARepText){
		raRegExp = new RegExp(AFindText,"g");
		return this.replace(raRegExp,ARepText);
	};
	
	//发送消息
	function sendMsg(){
		var msgOld = input_message_box.getContent();
		document.getElementById('p1').innerHTML = msgOld;
		var msgOldReplace = document.getElementById('p1').innerText;
		if (msgOld == "") return;
		
		var params = {
				task_queue_id	: taskID,
				open_id		    : openID, 
				content			: msgOld,
				contentReplace	: msgOldReplace
			};
		$.post("${pageContext.request.contextPath}/weixin!sendServiceMessage",
		    	{
					req_params : JSON.stringify(params)
		    	},
		    	function(data){	
		    		if(data.resultCode == "1"){
		    			// clear input message
		    		    input_message_box.execCommand("clearDoc");	 
		    		    // add showMessageBox list
		    		    appendMessage("我","0",msgOld,"0",getLongDate());
		    		    
		    		   	/* var tabsCon = window.parent.$('#chat-container');
		    		   	console.log(tabsCon);
		    			currentTab = tabsCon.tabs('getTab',openID);
		    		   tabsCon.tabs('update', {
		    				tab: currentTab,
		    				options: {
		    					title: "<sapn style='color:red'>"+openID+"</span>"
		    				}
		    			}); 
		    			console.log(currentTab.attr("class"));
		    			
		    			$(".tabs-title").css("color","red");  */
		    			
		    			//$currentTab.attr("class").bind("onclick");
		    			//console.log(currentTab);
		    			//console.log(currentTab.attr("s"));
		    			//$(".tabs-title tabs-closable").attr("style","color:red"); 
		    		    
		    		} else if (data.resultCode == "2"){
		    			//alert('<s:text name="chat_window_str3"></s:text>');
		    			$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str3"></s:text>');
		    		} 
		    	},
		    	"json"
	    ); 
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
	
	//保存用户信息
	function saveUserInfo(){
		if(document.getElementById('keyAccount').value == ""){
			//alert('<s:text name="chat_window_str4"></s:text>');
			$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str4"></s:text>');
			document.getElementById('keyAccount').focus();
	    	return;   
		} 
		/* if(document.getElementById('name').value == ""){
			alert('<s:text name="chat_window_str5"></s:text>');
			document.getElementById('name').focus();
	    	return;   
		} 
        if(document.getElementById('age').value == ""){
    	   alert('<s:text name="chat_window_str6"></s:text>');
    	   document.getElementById('age').focus();
    	   return;
        } 
        if(document.getElementById('grade').value == ""){
     	   alert('<s:text name="chat_window_str7"></s:text>');
     	   document.getElementById('grade').focus();
     	   return;
         }
		if(document.getElementById('department').value == ""){
			alert('<s:text name="chat_window_str8"></s:text>');
			document.getElementById('department').focus();
	    	return;   
		}    
		if(document.getElementById('area').value == ""){
			alert('<s:text name="chat_window_str9"></s:text>');
			document.getElementById('area').focus();
	    	return;   
		}  
		if(document.getElementById('cellphone').value == ""){
			alert('<s:text name="chat_window_str10"></s:text>');
			document.getElementById('cellphone').focus();
	    	return;   
		}  
		if(document.getElementById('email').value == ""){
			alert('<s:text name="chat_window_str11"></s:text>');
			document.getElementById('email').focus();
	    	return;   
		}   */
		if(document.getElementById('rid').value == ""){
			//alert('<s:text name="chat_window_str12"></s:text>');
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
				/* name		    : document.getElementById('name').value,
				sex			    : $('input[name="sex"]:checked').val(),
				age			    : document.getElementById('age').value,
				grade			: document.getElementById('grade').value,
				department		: document.getElementById('department').value,
				area			: document.getElementById('area').value,
				cellphone		: document.getElementById('cellphone').value,
				email			: document.getElementById('email').value, */
				name		    : document.getElementById('name').value,
				sex			    : $('input[name="sex"]:checked').val(),
				usertype		: document.getElementById('usertype').value,
				cellphone		: document.getElementById('cellphone').value,
				email		    : document.getElementById('email').value,
			/* 	activestatus    : document.getElementById('activestatus').value, */
				rid				: document.getElementById('ridPrefix').innerText + document.getElementById('rid').value,
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
	
	//正常关闭
	function normal(){
		document.getElementById('abnormal_content').disabled =true;
	}
	
	//非正常关闭
	function abnormal(){
		document.getElementById('abnormal_content').disabled =false;
	}
	
	//挂起
	function wait(){
		var params = {
				task_queue_id	: taskID,
				task_type            : 'wait'
			};
		$.post("${pageContext.request.contextPath}/weixin!setStatus",
		    	{
					req_params : JSON.stringify(params)
		    	},
		    	function(data){	
		    		if(data.result>0){
		    			//alert('<s:text name="chat_window_str16"></s:text>');
		    			$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str16"></s:text>');
		    		};
		    	},
		    	"json"
	    );   
	}
	
	//升级
	function stop(){
		if(document.getElementById('rid').value == ""){
			alert('<s:text name="chat_window_str12"></s:text>');
			document.getElementById('rid').focus();
	    	return;   
		}  
		var params = {
				task_queue_id	: taskID,
				open_id	        : openID,
				rid             : document.getElementById('ridPrefix').innerText + document.getElementById('rid').value
			};
		$.post("${pageContext.request.contextPath}/weixin!sendMsgForStop",
		    	{ 
			req_params : JSON.stringify(params)
			},
		    	function(data){	
		    		if(data.result){
		    		   	//alert('<s:text name="chat_window_str17"></s:text>');
		    		   	$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str17"></s:text>');
		    		   	//改变状态
		    		   	close();
		    		} 
		    	},
		    	"json"
	    );
	}
	
	function close(){
		var params = {
				task_queue_id	: taskID,
				task_type       : 'stop'
			};
		$.post("${pageContext.request.contextPath}/weixin!setStatus",
		    	{
					req_params : JSON.stringify(params)
		    	},
		    	function(data){	
		    		if(data.result>0){
		    			//刷新父页面
		    			parent.$('#tt_uncomplete_advisory').datagrid("reload");
		    		   	var tabsCon = window.parent.$('#chat-container');
			    		var tabsLength = tabsCon.tabs('tabs').length;
		    			var currentTab = tabsCon.tabs('getSelected');
		    			var index = tabsCon.tabs('getTabIndex',currentTab);
		    			if(tabsLength==1)
		    				parent.$('#openChatDiv').window('close');
		    			tabsCon.tabs('close',index);
		    		} else {
		    			//alert('<s:text name="chat_window_str18"></s:text>');
		    			$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str18"></s:text>');
		    		}
		    	},
		    	"json"
	    );
	}
	
	//任务更改关闭状态关闭
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
	
	//发送评价连接并关闭
	function exit(){
	    var close_type = $('input[name="closetype"]:checked').val();	
		//正常关闭
		if(close_type == 1){
			/*
			if(document.getElementById('rid').value == ""){
				//alert('<s:text name="chat_window_str12"></s:text>');
				$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str12"></s:text>');
				document.getElementById('rid').focus();
		    	return;   
			} 
*/			
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
			    		if(data.result){
			    		   	//alert('<s:text name="chat_window_str43"></s:text>');
			    		   	$.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str43"></s:text>');
			    		   	//改变状态
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

	// 上传图片、文件 async: false,
	function upload(obj, formId){
	 	var filePath = $(obj).val();
	 	var jpgFlag = filePath.substring(filePath.length - 3);
	 	if(jpgFlag != "jpg" && jpgFlag != "JPG"){
           // alert('<s:text name="chat_window_str20"></s:text>');
            $.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str20"></s:text>');
	 		return;
	 	}
	 	if(filePath != "") {	
			$("#taskId").val(taskID);
			$("#openId").val(openID);
	 		$('#'+formId).ajaxSubmit({success:function(responseData, statusText){
	 			if(responseData.res == "200"){
	 				console.log(responseData);
		 			appendMessage("我","0","<img src=\"" + responseData.imgUrl + "\" width=\"330px\" />","0",getLongDate());
	 			} else if(responseData.res == "128"){
	 				//alert('<s:text name="chat_window_str21"></s:text>');
	 				 $.messager.alert('<s:text name="INFO"></s:text>','<s:text name="chat_window_str21"></s:text>');
	 			}
	 		},dataType:'json'});
	 	}
	}
	
	function result(data){
		alert(data.responseData);
	}
	
	function getDepartment(businessQuarter){
		$('#selectorType').combobox({  
			url:'<%=request.getContextPath()%>/examCommonQuery?command=1',  
	   		 valueField:'value',  
	   		 textField:'text' , 
	   		 onSelect:function(data){
	   		 	//getCycle();
	         },
	         onSuccess:function(data){
	   		 	//$('#'+businessQuarter).combobox("setValue",$('#i_branch_no_hide').val()+' ' );
	         }
		}); 
	}
	
	//获取请求类型
	function getRequestType(){
		
		$('#requestType').tree({
			url:'${pageContext.request.contextPath}/weixin!getRequestType'			
		});
		
	}
	
	function openRequestTypeWindow(){
		$('#typetxtDiv').window({  
	           modal:true,
	           width:200,
	           height:'auto',
	           left:200,
	           top:200
	    });
		
		$('#typetxtDiv').window('open');
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
	
	//标准提示语
	function inputFormatMessage(param){
		input_message_box.execCommand("insertHtml", param, true);
		input_message_box.focus();	   
	}
	
	//查找KDATA数据
/* 	function sreachKData(){
		
		if(document.getElementById('keyAccount').value == ''){
			document.getElementById('name').innerText='';
			document.getElementById('sex').innerText = '';
			document.getElementById('cellphone').innerText='';
			document.getElementById('email').innerText='';
			document.getElementById('usertype').innerText='';
			document.getElementById('activestatus').innerText='';
			return;
		}
		
		var params = {
				kid : document.getElementById('keyAccount').value
			};
		$.post("${pageContext.request.contextPath}/weixin!sreachKData",
		    	{ 
			req_params : JSON.stringify(params)
			},
		    	function(data){	
		    		if(data!=null){
		    			document.getElementById('name').innerText=data.name;
		    			if(data.sex=='M'){
		    				document.getElementById('sex').innerText = '<s:text name="chat_window_str34"></s:text>';
		    			} else if (data.sex=='F'){
		    				document.getElementById('sex').innerText = '<s:text name="chat_window_str35"></s:text>';
		    			}
			    		document.getElementById('cellphone').innerText=data.cellphone;
			    		document.getElementById('email').innerText=data.email;
			    		document.getElementById('usertype').innerText=data.usertype;
			    		document.getElementById('activestatus').innerText=data.activestatus;
		    		}else {
		    			document.getElementById('name').innerText='';
	    				document.getElementById('sex').innerText = '';
	    				document.getElementById('cellphone').innerText='';
	    				document.getElementById('email').innerText='';
	    				document.getElementById('usertype').innerText='';
	    				document.getElementById('activestatus').innerText='';
	    			}
		    	},
		    	"json"
	    );
	} */
</script>
</head>
<body>
<!-- <div id="chat-container" class="easyui-tabs" ></div> -->
    <s:iterator value="list_message" status="index">
       <s:if test="create_user != '' && create_user != null"> 
			<script>
			initHistoryMessage(<s:property value="#index.index" />, '<s:property value="nick_name" />',  '<s:property value="service_id" />', 
					'<s:property value="content.replaceAll('\r\n','<br/>').replaceAll('\r','</br>').replaceAll('\n','<br/>').replaceAll(\"'\",'&#039')" escape="false" />', '<s:property value="create_user" />','<s:property value="create_date" />');
			</script>
		</s:if>
	</s:iterator>
	<div style="float:left;width:100%;height:100%;border:solid 0px red;overflow: hidden;">
	        <div style="float:left;">
				<div>
					<textarea name="show_message" id="show_message"></textarea>				
				</div>
				<div> 	
				    <form id="uploadForm1" action="uploadFile!uploadMediaFile" method="post" enctype="multipart/form-data">
				   		<input type="file" name="media" class="ifile" style="margin-left:8px;" title="发送图片" onchange="upload(this,'uploadForm1');">
				   		<input name="type" value="image" type="hidden">
				   		<input id="taskId" name="taskId" value="" type="hidden">
				   		<input id="openId" name="openId" value="" type="hidden">
				   	</form>
			    	<textarea name="input_message" id="input_message" ></textarea>
			    	<div style="width:100%;text-align:right;padding-top:5px;"> 
			    		<%-- <div style="float: left;padding-left: 5px;padding-top: 5px">[<s:text name="chat_window_str22"></s:text>]&nbsp;</div> --%>
			    		<div style="float: left;text-align:center; font-size: 11px;width:88px; height:26px; color: #875f0e; background: url(${pageContext.request.contextPath}/images/bt0.png) no-repeat;font-weight: bold;cursor: pointer;line-height:26px;" onclick="sendMsg()"><s:text name="chat_window_str23"></s:text></div>
			    	    
			    	</div>
				</div> 
	        </div>
	        <div id="divBaseInfo" style="float:left;color:#495a4a;font-size: 12px;font-family:微软雅黑;width:305px;margin-left:5px"> 	
				<div id="buttonDiv" style="text-align:center;">
			    		<input type="button" class="commonButton" value='<s:text name="chat_window_str40"></s:text>' onclick="wait()" style="cursor: pointer;"/>
			    		<input type="button" class="dangerButton" value='<s:text name="chat_window_str42"></s:text>' onclick="exitDiv()" style="cursor: pointer;"/>
			    </div>
			    <div id="openCloseWindowsDiv" class="easyui-window" closed="true" title='<s:text name="chat_window_str43"></s:text>' collapsible="false" minimizable="false" maximizable="false">
			    </div>
    </div> 
    <div id="p1" style="border: solid 1px #1144EB; min-height: 50px; display: none;"></div>
	<div style="clear:both;"></div>
</body>
</html>