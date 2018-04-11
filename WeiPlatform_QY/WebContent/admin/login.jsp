<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信服务平台</title>
<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<link type="text/css" href="${pageContext.request.contextPath}/css/HT_login.css" rel="stylesheet" />
<link type="text/css" href="${pageContext.request.contextPath}/css/msgTips.css" rel="stylesheet" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/msgTips.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript">

    if (window != top)
        top.location.href = location.href;

    function keyDown(evt) { evt = evt ? evt : (window.event ? window.event : null); if (evt.keyCode == 13 || evt.charCode == 13) { $("#btnLogin").click(); } }

    function showyear() {
        var dateTime = new Date();
        $("#yy").html(dateTime.getFullYear());
    }

    //设置登录cookie
    function setLoginCookie() {
        setCookie("login_name", $("#username").val(), 30);
    }

    //页面加载
    $(document).ready(function () {
        initPage();
    });

    function initPage() {
    	// 从cookie取用户名
        $("#username").val(getCookie("login_name"));
        $("#username").focus();  
        
        //页面取session
        var langtype = '<%=session.getAttribute("lang")%>';
        //如果是en
        if(langtype == "en_US"){
        	$(".selector").val("en_US");
        }
        //如果是cn
        if(langtype == "zh_CN"){
        	$(".selector").val("zh_CN");
        }
    }
    
    function login(){
    	var uName = $("#username").val();
    	if (!uName) {
    		$.messager.alert("<s:text name="WARNING"></s:text>","<s:text name="login_str1"></s:text>");
    		return false;
    	}
    	var uPassword = $("#password").val();
    	if (!uPassword) {
    		$.messager.alert("<s:text name="WARNING"></s:text>","<s:text name="login_str2"></s:text>");
    		return false;
    	}
    	var ver_code = document.getElementById("verCode").value;
    	if (!ver_code) {
    		$.messager.alert("<s:text name="WARNING"></s:text>","<s:text name="login_str3"></s:text>");
    		return false;
    	}
    	document.getElementById("login_form").submit();
    }

    //登录验证
  /*   function loginCheck() {            
        if ($("#username").val() == "") {
            showMsg("请输入登录账号", "error");
            return false;
        }
        if ($("#password").val() == "") {
            showMsg("请输入登录密码", "error");
            return false;
        }
        setLoginCookie();
        return true;
    } */

    //调用提示信息的方法--txtMsg：提示内容，to：信息显示时间，s：滑动速度，t：提示类型，src：跳转路径
   /*  function showMsg(txtMsg, t) {
        $("#login_top").msgTips({
            src: "",
            msg: txtMsg,			//显示的消息
            type: t			//提示类型（1、success 2、error 3、warning）
        });
    } */

    //页面加载后执行
    function getFocus() {
        $("#username").focus();
    }

    /* //用户名文本框获得焦点时的样式和内容
    function userNameFocus(o) {
        if (o.value == "请输入登录账号") {
            o.className = "login_txt_focus";
            setCaret(o, 0);
        } else {
            o.className = "login_txt_focus_input";
        }
    }

    //用户名文本框失去焦点时的样式和内容
    function userNameBlur(o) {
        if (o.value == "请输入登录账号" || o.value == "") {
            o.value = "请输入登录账号";
            o.className = "login_txt";
        } else {
            o.className = "login_txt_input";
        }
    }

    //鼠标经过用户名文本框时的样式和内容
    function userNameOver(o) {
        if (o.value == "请输入登录账号") {
            o.className = "login_txt_over";
        } else {
            o.className = "login_txt_over_input";
        }
    }

    //鼠标离开用户名文本框时的样式和内容
    function userNameOut(o) {
        if (document.activeElement.id == o.id)
            return;
        if (o.value == "请输入登录账号" || o.value == "") {
            o.value = "请输入登录账号";
            o.className = "login_txt";
        } else {
            o.className = "login_txt_input";
        }
    }

    //用户名文本框按下按键时
    function userNameKeyDown(o) {
        o.className = "login_txt_focus_input";
        if (o.value == "请输入登录账号") {
            o.value = "";
        }
    }

    //用户名文本框按下并抬起按键时
    function userNameKeyUp(o) {
        if (o.value == "") {
            o.value = "请输入登录账号";
            o.className = "login_txt_focus";
            setCaret(o, 0);
        }
    }

    //密码文本框获得焦点时的样式
    function passwordFocus(o) {
        o.className = "login_txt_focus";
        if (o.id == "txtPwd")
            setCaret(o, 0);
    }

    //密码文本框失去焦点时的样式
    function passwordBlur(o) {
        o.className = "login_txt";
    }

    //鼠标经过密码文本框时的样式
    function passwordOver(o) {
        o.className = "login_txt_over";
    }

    //鼠标离开密码文本框时的样式
    function passwordOut(o) {
        if (document.activeElement.id == o.id)
            return;
        o.className = "login_txt";
    }

    //密码文本框按下按键时
    function passwordKeyDown(o) {
        o.style.display = "none";
        var pw = document.getElementById("password");
        pw.style.display = "";
        pw.className = "login_txt_focus_input";
        pw.focus();
    }

    //密码文本框抬起按键时
    function passwordKeyUp(o) {
        if (o.value == "") {
            o.style.display = "none";
            var pw2 = document.getElementById("txtPwd");
            pw2.style.display = "";
            pw2.className = "login_txt_focus_input";
            pw2.focus();
            setCaret(pw2, 0);
        }
    }

    //设置光标的位置
    function setCaret(o, pos) {
        if (o.setSelectionRange) {//非IE
            o.setSelectionRange(0, 0);
            o.focus();
        } else if (o.createTextRange) {//IE
            var r = o.createTextRange();
            r.collapse(true);
            r.moveStart("character", pos);
            r.select();
        } else {
            return;
        }
    } */

    //阻止浏览器的默认行为 
    function stopDefault(e) {
        if (e && e.preventDefault)//W3C标准
            e.preventDefault();
        else//IE
            window.event.returnValue = false;
    }
    
    function changeVerCode() {	
    	var imgUrl = '${pageContext.request.contextPath}/admin/image.jsp?r=' + Math.random();
    	document.getElementById("ver_code_img").src = imgUrl;		
    }
    
    function  subCheck(){
    	if(event.keyCode==13){
    		login();
    	}
    }

    //改变语言
    function syslang(){
		document.form.action='lang.action?local='+document.getElementById("productlang").value;
		document.form.submit();
    }
</script>
</head> 
<body onload="javascript:showyear();getFocus();" style="background:url(${pageContext.request.contextPath}/images/login_bg_new.jpg) no-repeat fixed top;">
	<div id="login_top">
		<a href="http://www.astrazeneca.com.cn/" target="_blank"><img src='${pageContext.request.contextPath}/images/RGB.png' alt="阿斯利康" width="300" height="197"/></a>
	</div>
	<div style="display: none;">
		<form name="form" method="post">
		</form>
	</div>
	<%-- <input id="sessionID" name="sessionID" value="<%=session.getAttribute("lang")%>"/> --%>
  	<div id="login">  	
  		<form id="login_form" name="login_form" action="${pageContext.request.contextPath}/j_spring_security_check" method="post" onkeydown="javascript:keyDown(event)">
	        <p style="font-family:'微软雅黑';font-size:25px;margin-bottom:20px;"><s:text name="login_title"></s:text></p>
	        <p>
	          	<input type="text" style="background: url(${pageContext.request.contextPath}/images/name.png) no-repeat 5px center;padding:2px 2px 2px 25px;"  id="username" name="j_username" class="login_txt" placeholder="<s:text name="user_name"></s:text>" />
	        </p>
	        <p>
	          	<input style="background: url(${pageContext.request.contextPath}/images/password.png) no-repeat 5px center;padding:2px 2px 2px 25px;" type="password" id="password" name="j_password" class="login_txt" placeholder="<s:text name="user_password"></s:text>" />                
	        </p>
	        <p>
	        	<input style="background: url(${pageContext.request.contextPath}/images/code.png) no-repeat 5px center;padding:2px 2px 2px 25px;" type="text" id="verCode" name="verCode" onkeydown="subCheck();" class="login_txt_code"  placeholder="<s:text name="verify_code"></s:text>" />
                <img onClick="JavaScript:changeVerCode();" id="ver_code_img" alt="ver_code" style="vertical-align:middle;cursor:pointer; width:90px;" src="${pageContext.request.contextPath}/admin/image.jsp" />
	        </p>
	        <p>
	          	<input type="button" value="<s:text name="login"></s:text>" id="btnLogin" href="javascript:void(0)"  onclick="login();" class="login_btn" />
	        </p>
	        <p>
	            <select id="productlang"  name="productlang" style="width:120px;"  class="selector" onchange="syslang()">
				    <option value="en_US">English</option>
				    <option value="zh_CN">中文版</option>
				</select>
			</p>
	        <p>
	          	<font color="red">${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}</font>
	        </p>
    	</form>
	</div>
  	<div id="login_bottom">
		<b>微信服务平台&nbsp;</b>&copy;&nbsp;<span id="yy"></span>&nbsp;<a href='#' target="_blank"><i>YIDATEC</i></a>&nbsp;Inc. All rights reserved.
	</div>
</body>
</html>