<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=9" />
    <title>微信服务平台</title>
    <link type="text/css" href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet" />
    <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
    
    <link type="text/css" href="${pageContext.request.contextPath}/css/msgTips.css" rel="stylesheet" />
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/msgTips.js"></script>    
    
	<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
	
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
            //$("#username").val(getCookie("login_name"));
            //$("#username").focus();            
        }
        
        function login(){
        	var uName = $("#username").val();
        	if (!uName) {
        		$.messager.alert("警告","请输入用户名！");
        		return false;
        	}
        	var uPassword = $("#password").val();
        	if (!uPassword) {
        		$.messager.alert("警告","请输入密码！");
        		return false;
        	}
        	var ver_code = document.getElementById("verCode").value;
        	if (!ver_code) {
        		$.messager.alert("警告","请输入验证码！");
        		return false;
        	}
        	document.getElementById("login_form").submit();
        }

        //登录验证
        function loginCheck() {            
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
        }

        //调用提示信息的方法--txtMsg：提示内容，to：信息显示时间，s：滑动速度，t：提示类型，src：跳转路径
        function showMsg(txtMsg, t) {
            $("#login_top").msgTips({
                src: "",
                msg: txtMsg,			//显示的消息
                type: t			//提示类型（1、success 2、error 3、warning）
            });
        }

        //页面加载后执行
        function getFocus() {
            $("#username").focus();
        }

        //用户名文本框获得焦点时的样式和内容
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
        }

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
    </script>

</head>
<body onload="javascript:showyear();getFocus();">
	<form id="login_form" name="login_form" action="${pageContext.request.contextPath}/j_spring_security_check" method="post" onkeydown="javascript:keyDown(event)">
		<div id="login_top">
			
		</div>
        <div id="login" style="background:url('${pageContext.request.contextPath}/images/logo_panel2.png') no-repeat;">
            <p>
            	<input type="text" id="username" name="j_username" title="请输入登录账号" class="login_txt" value="请输入登录账号" 
            		onclick="stopDefault(event);userNameFocus(this);"
            		onblur="userNameBlur(this);"
            		onmouseover="userNameOver(this);"
            		onkeyup="userNameKeyUp(this);"
            		onkeydown="userNameKeyDown(this);"
            		onmouseout="userNameOut(this);" />
            </p>
            <p>
            	<input type="password" id="password" name="j_password" class="login_txt" style="display:none;"
            		onclick="passwordFocus(this);"
                    onblur="passwordBlur(this);" 
                    onmouseover="passwordOver(this);" 
                    onmouseout="passwordOut(this);" 
                    onkeyup="passwordKeyUp(this);"/>
                <input type="text" id="txtPwd" class="login_txt" value="请输入登录密码" title="请输入登录密码" 
                	onclick="stopDefault(event); passwordFocus(this);"
                    onblur="passwordBlur(this);" 
                    onmouseover="passwordOver(this);"
                    onkeydown="passwordKeyDown(this);" 
                    onmouseout="passwordOut(this);" />
            </p>
            <p>
            	<input type="text" id="verCode" name="verCode" onkeydown="subCheck();" class="login_txt" style="width:95px;float: left;" value="验证码"
            		onmouseover='this.focus()' 
            		onfocus='this.select()' 
                    onblur="if (this.value ==''){this.value='验证码'}" 
                    onclick="if(this.value=='验证码')this.value=''" />
                    
                <span class="check_img">
                	<img onClick="JavaScript:changeVerCode();" id="ver_code_img" alt="ver_code" style="vertical-align:middle;cursor:pointer; width:90px" src="${pageContext.request.contextPath}/admin/image.jsp" />
                </span>
                <div class="clear"></div>
            </p>
            <p>
            	<input id="btnLogin" href="javascript:void(0)"  onclick="login();" class="login_btn" />                
            </p>
        </div>
		<div id="login_bottom">
			<b>微信服务平台&nbsp;</b>&copy;&nbsp;<span id="yy"></span>&nbsp;<a href='#' target="_blank"><i>YIDATEC</i></a>&nbsp;Inc. All rights reserved.
		</div>
    </form>
</body>
</html>