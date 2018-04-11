<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="secx" uri="/extaglib" %>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="main_str1"></s:text></title>
<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" type="image/ico" href="${pageContext.request.contextPath}/favicon.ico" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<%-- <script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script> --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/syUtil.js"></script>

<style type="text/css"> 
	body,html {
		font-family:微软雅黑;
		font-size:13px;
		font-weight:normal;
		font-style:normal;
		text-decoration:none;
		width:100%;
		height:100%;
	}
	.header{
		height:70px; 
		background:#F4F3F3;
		overflow:hidden;
		
	}
	.bottom_bar{
		position: absolute;
		right: 30px;
		top: 30px;
		height: 16px;
		text-align: right;
		color:white;
		word-wrap: break-word;
		color:#000
	}
	.panel-tool{
		line-height:28px;
	}
	.accordion .accordion-header {
	    background-attachment: scroll;
	    background-clip: border-box;
	    background-color: #FAFAFA;
	    background-image: none;
	    background-origin: padding-box;
	    background-position: 0 0;
	    background-repeat: repeat;
	    background-size: auto auto;
	    height:20px;
	    line-height:20px;
	}
	.accordion .accordion-header-selected {
	    -moz-border-bottom-colors: none;
	    -moz-border-image: none;
	    -moz-border-left-colors: none;
	    -moz-border-right-colors: none;
	    -moz-border-top-colors: none;
	    background-attachment: scroll;
	    background-clip: border-box;
	    background-color: transparent;
	    background-image: url("${pageContext.request.contextPath}/images/nav.png");
	    background-origin: padding-box;
	    background-position: center bottom;
	    background-repeat: no-repeat;
	    background-size: auto auto;
	    border-bottom-color: -moz-use-text-color;
	    border-bottom-style: none;
	    border-bottom-width: 0;
	    border-left-color-ltr-source: physical;
	    border-left-color-rtl-source: physical;
	    border-left-color-value: -moz-use-text-color;
	    border-left-style-ltr-source: physical;
	    border-left-style-rtl-source: physical;
	    border-left-style-value: none;
	    border-left-width-ltr-source: physical;
	    border-left-width-rtl-source: physical;
	    border-left-width-value: 0;
	    border-right-color-ltr-source: physical;
	    border-right-color-rtl-source: physical;
	    border-right-color-value: -moz-use-text-color;
	    border-right-style-ltr-source: physical;
	    border-right-style-rtl-source: physical;
	    border-right-style-value: none;
	    border-right-width-ltr-source: physical;
	    border-right-width-rtl-source: physical;
	    border-right-width-value: 0;
	    border-top-color: -moz-use-text-color;
	    border-top-style: none;
	    border-top-width: 0;
	    height: 35px;
	}
	.pitem {
	    list-style-type: none;
	    margin-bottom: 0;
	    margin-left: 0;
	    margin-right: 0;
	    margin-top: 0;
	    padding-bottom: 10px;
	    padding-left: 20px;
	    padding-right: 0;
	    padding-top: 2px;
	}
	.pitem li {
	    font-size: 12px;
	    line-height: 20px;
	}
	.pitem li a { color:#0c7af5}

	
</style> 
<script>
function addTabPane(itemid, title, url,icon){  
    var tt = $('#tab-container');  
    if (tt.tabs('exists', title)){//如果tab已经存在,则选中并刷新该tab          
        tt.tabs('select', title);  
        refreshTab({tabTitle:title,url:url});  
    } else {  
    	var content;
        if (url){  
            content = "<iframe scrolling='auto' frameborder='0'  src=\'"+url+"\' style='width:100%;height:99%;'></iframe>";
        } else {  
            content = 'main_unrealized';  
        }  
        tt.tabs('add',{
        	id:itemid,
            title:title,  
            closable:true,  
            content:content,  
            iconCls:icon
        });  
    };  
} 

function changeTabPane(itemid, title, url,icon){  
    var tt = $('#tab-container');  
    if (tt.tabs('exists', title)){//如果tab已经存在,则选中并刷新该tab          
        tt.tabs('select', title);  
    } else {  
        var content;
        if (url){  
            content = "<iframe scrolling='auto' frameborder='0'  src=\'"+url+"\' style='width:100%;height:99%;'></iframe>";
        } else {  
            content = 'main_unrealized';  
        }  
        tt.tabs('add',{  
            title:title,  
            closable:true,  
            content:content,  
            iconCls:icon
        });  
    };  
} 

/**     
 * 刷新tab 
 * @cfg  
 *example: {tabTitle:'tabTitle',url:'refreshUrl'} 
 *如果tabTitle为空，则默认刷新当前选中的tab 
 *如果url为空，则默认以原来的url进行reload 
 */  
function refreshTab(cfg){  
    var refresh_tab = cfg.tabTitle?$('#tab-container').tabs('getTab',cfg.tabTitle):$('#tab-container').tabs('getSelected');  
    if(refresh_tab && refresh_tab.find('iframe').length > 0){  
	    var _refresh_ifram = refresh_tab.find('iframe')[0];  
	    var refresh_url = cfg.url?cfg.url:_refresh_ifram.src;  
	    //_refresh_ifram.src = refresh_url;  
	    _refresh_ifram.contentWindow.location.href=refresh_url;  
    };
}  
function logout(){
	//var currentUserId = $("#hiddenId").text();
	//alert(currentUserId);
	//$.post("adminPlatformUserMgr!reAllot",function(data){
		//if(data.resCode==200){
			//location.href="${pageContext.request.contextPath}/j_spring_security_logout";
		//}
		//else {
			//$.messager.alert("警告","重新分配任务失败，不能退出！");
		//}
	//},"json");
	location.href="${pageContext.request.contextPath}/j_spring_security_logout";
}
</script>
</head>
<body class="easyui-layout" onload="document.getElementById('main').click()">
	<s:include value="langCommon.jsp"></s:include>
	<div data-options="region:'north',border:false,split:true" class="header">
		<div class="logogo" style="float:left;padding:15px 0 0 15px;"><img src="${pageContext.request.contextPath}/images/logo_az.gif" style="border:0" /></div>
		<div class="bottom_bar">
			<s:text name="main_str2"></s:text><sec:authorize access="isAuthenticated()">
					<sec:authentication property="principal.userName"/>
					<span id="hiddenId" style="display:none"><sec:authentication property="principal.id"/></span>
			      </sec:authorize>
			      <a style="color:#000;text-decoration:none;" href="javascript:void(0)" onclick="logout()">【<s:text name="main_str3"></s:text>】</a>
		</div>
	</div>
	<div data-options="region:'west',split:true,title:' '," style="width:260px;">
		<div class="easyui-accordion"  data-options="border:false">		
		    <secx:authorizex resKey="my_advisory">
			<div title="<s:text name="main_str4"></s:text>" >
			    <ul class="pitem">
			    	<secx:authorizex resKey="uncomplete_advisory">
						<li><a href="javascript:addTabPane('C1','<s:text name="main_str5"></s:text>', '${pageContext.request.contextPath}/admin/weixin/uncomplete_advisory.jsp');"><s:text name="main_str5"></s:text></a></li>
					</secx:authorizex>
					<secx:authorizex resKey="complete_advisory">
						<li><a href="javascript:addTabPane('C2','<s:text name="main_str6"></s:text>', '${pageContext.request.contextPath}/admin/weixin/complete_advisory.jsp');"><s:text name="main_str6"></s:text></a></li>
					</secx:authorizex>
					<secx:authorizex resKey="changePass">
					    <li><a href="javascript:addTabPane('C3','<s:text name="main_str7"></s:text>', '${pageContext.request.contextPath}/admin/system_manage/change_password.jsp');"><s:text name="main_str7"></s:text></a></li>
					</secx:authorizex>
			    </ul>
			</div>
			</secx:authorizex>
			
			<secx:authorizex resKey="data_statistics">
			<div title="<s:text name="main_str8"></s:text>" >
			    <ul class="pitem">
			        <secx:authorizex resKey="daily_data_statistics">
					     <li><a href="javascript:addTabPane('W1','<s:text name="main_str9"></s:text>', '${pageContext.request.contextPath}/admin/data_statistics/daily_data_statistics.jsp');" id="main"><s:text name="main_str9"></s:text></a></li>
					</secx:authorizex>
					<!--  
					<li><a href="javascript:addTabPane('W2','月数据统计', '${pageContext.request.contextPath}/admin/data_statistics/monthly_data_statistics.jsp');">月数据统计</a></li>					
					-->
					<secx:authorizex resKey="yearly_data_statistics">
					     <li><a href="javascript:addTabPane('W3','<s:text name="main_str10"></s:text>', '${pageContext.request.contextPath}/admin/data_statistics/yearly_data_statistics.jsp');"><s:text name="main_str10"></s:text></a></li>
					</secx:authorizex>
					<secx:authorizex resKey="personal_month_data_statistics">
					     <li><a href="javascript:addTabPane('W4','<s:text name="main_str11"></s:text>', '${pageContext.request.contextPath}/admin/data_statistics/personal_month_data_statistics.jsp');"><s:text name="main_str11"></s:text></a></li>
			        </secx:authorizex>
			    </ul>
			</div>
			</secx:authorizex>
			
			<secx:authorizex resKey="advisory_management">
			<div title="<s:text name="main_str12"></s:text>" >
			    <ul class="pitem">
			    	<secx:authorizex resKey="consultant_management">
						<li><a href="javascript:addTabPane('A1','<s:text name="main_str13"></s:text>', '${pageContext.request.contextPath}/admin/advisory_manage/adviser_view.jsp');"><s:text name="main_str13"></s:text></a></li>
					</secx:authorizex>
					<%-- <secx:authorizex resKey="adviser_schedule">
						<li style="display:none;"><a href="javascript:addTabPane('A2','我的咨询时间管理', '${pageContext.request.contextPath}/admin/advisory_manage/adviser_schedule.jsp');">我的咨询时间管理</a></li>
					</secx:authorizex>
					<secx:authorizex resKey="adviser_schedule_reserve">
						<li style="display:none;"><a href="javascript:addTabPane('A3','我的预约咨询管理', '${pageContext.request.contextPath}/admin/advisory_manage/adviser_schedule_reserve.jsp');">我的预约咨询管理</a></li>
					</secx:authorizex> --%>
					<secx:authorizex resKey="adviser_appraisal_count">
						<li><a href="javascript:addTabPane('A4','<s:text name="main_str14"></s:text>', '${pageContext.request.contextPath}/admin/advisory_manage/appraisal_count_info.jsp');"><s:text name="main_str14"></s:text></a></li>
					</secx:authorizex>
					<%-- <secx:authorizex resKey="adviser_online">
						<li><a href="javascript:addTabPane('A5','在线座席员', '${pageContext.request.contextPath}/admin/advisory_manage/adviser_online.jsp');">在线座席员</a></li>
					</secx:authorizex>
					<secx:authorizex resKey="task_management">
					    <li><a href="javascript:addTabPane('A6','咨询信息管理', '${pageContext.request.contextPath}/admin/advisory_manage/task_management.jsp');">咨询信息管理</a></li>
					</secx:authorizex> --%>
					<secx:authorizex resKey="group_management">
						<li><a href="javascript:addTabPane('A7','<s:text name="main_str15"></s:text>', '${pageContext.request.contextPath}/admin/advisory_manage/group_view.jsp');"><s:text name="main_str15"></s:text></a></li>
					</secx:authorizex>
			    </ul>
			</div>
			</secx:authorizex>	
			
			<secx:authorizex resKey="weixin_users_management">		
			<div title="<s:text name="main_str16"></s:text>" >
			    <ul class="pitem">
			        <secx:authorizex resKey="subscribers">
					     <li><a href="javascript:addTabPane('D1','<s:text name="main_str17"></s:text>', 'weixin/subscriber_view.jsp');"><s:text name="main_str17"></s:text></a></li>
					</secx:authorizex>
					<secx:authorizex resKey="subscribers_synchronous">
					     <li><a href="javascript:addTabPane('D2','<s:text name="main_str18"></s:text>', 'weixin/sync_subscribe.jsp');"><s:text name="main_str18"></s:text></a></li>	
					</secx:authorizex>
			    </ul>
			</div>
			</secx:authorizex>
			
			<secx:authorizex resKey="requisition_management">
			<div title="<s:text name="main_str19"></s:text>" >
			    <ul class="pitem">
					<secx:authorizex resKey="request_management">
					     <li><a href="javascript:addTabPane('D3','<s:text name="main_str19"></s:text>', 'weixin/requisition_manage.jsp');"><s:text name="main_str19"></s:text></a></li>
					</secx:authorizex>
			    </ul>
			</div>
			</secx:authorizex>
			
			<secx:authorizex resKey="cms_mgr">
			<div title="<s:text name="main_str31"></s:text>" >
			    <ul class="pitem">
			        <secx:authorizex resKey="category_mgr">
			    	     <li><a href="javascript:addTabPane('F1','<s:text name="main_str32"></s:text>', '${pageContext.request.contextPath}/admin/article_manage/sections_mgr.jsp');"><s:text name="main_str32"></s:text></a></li>
					</secx:authorizex>
					<secx:authorizex resKey="article_mgr">
					     <li><a href="javascript:addTabPane('F2','<s:text name="main_str33"></s:text>', '${pageContext.request.contextPath}/admin/article_manage/articles_mgr.jsp');"><s:text name="main_str33"></s:text></a></li>					
			        </secx:authorizex>
			    </ul>
			</div>
			</secx:authorizex>	
			
			<secx:authorizex resKey="system_management">
			<div title="<s:text name="main_str20"></s:text>" >
			    <ul class="pitem">
			    	<secx:authorizex resKey="account_management">
					     <li><a href="javascript:addTabPane('X1','<s:text name="main_str21"></s:text>', '${pageContext.request.contextPath}/admin/system_manage/platform_users_view.jsp');"><s:text name="main_str21"></s:text></a></li>
					</secx:authorizex>
					<secx:authorizex resKey="permission_role_management">
					     <li><a href="javascript:addTabPane('X2','<s:text name="main_str22"></s:text>', '${pageContext.request.contextPath}/admin/system_manage/user_role_view.jsp');"><s:text name="main_str22"></s:text></a></li>					
					</secx:authorizex>
					<secx:authorizex resKey="permissions_resources_management">
					     <li><a href="javascript:addTabPane('X3','<s:text name="main_str23"></s:text>', '${pageContext.request.contextPath}/admin/system_manage/resource_mgr.jsp');"><s:text name="main_str23"></s:text></a></li>
					</secx:authorizex> 
			    </ul>
			</div>
			</secx:authorizex>
			
			<secx:authorizex resKey="parameter_setting_management">
			<div title="<s:text name="main_str24"></s:text>" >
			    <ul class="pitem">
					<secx:authorizex resKey="params_management">
					     <li><a href="javascript:addTabPane('B5','<s:text name="main_str25"></s:text>', '${pageContext.request.contextPath}/admin/system_manage/parameter_setting.jsp');"><s:text name="main_str25"></s:text></a></li>						
			        </secx:authorizex>
			        <secx:authorizex resKey="request_type_management">
						<li><a href="javascript:addTabPane('B6','<s:text name="main_str26"></s:text>', '${pageContext.request.contextPath}/admin/system_manage/request_type.jsp');"><s:text name="main_str26"></s:text></a></li>						
			    	</secx:authorizex>
			    </ul>
			</div>
			</secx:authorizex>
			
			<secx:authorizex resKey="sys_monitor">
			<div title="<s:text name="main_str27"></s:text>" >
			    <ul class="pitem">
			    	<secx:authorizex resKey="leaderMonitor">
						<li><a href="javascript:addTabPane('E1','<s:text name="main_str28"></s:text>', '${pageContext.request.contextPath}/admin/weixin/leader_monitor.jsp');"><s:text name="main_str28"></s:text></a></li>
					</secx:authorizex>
					<secx:authorizex resKey="transfering_tasks">
						<li><a href="javascript:addTabPane('E3','<s:text name="main_str29"></s:text>', '${pageContext.request.contextPath}/admin/weixin/transfering_tasks.jsp');"><s:text name="main_str29"></s:text></a></li>
					</secx:authorizex>
					<secx:authorizex resKey="publicMonitor">
						<li><a href="javascript:addTabPane('E2','<s:text name="main_str30"></s:text>', '${pageContext.request.contextPath}/admin/weixin/public_monitor.jsp');"><s:text name="main_str30"></s:text></a></li>
					</secx:authorizex>
			    </ul>
			</div>
			</secx:authorizex>
			
		</div>
	</div>
	<div data-options="region:'center'" style="overflow:hidden;" >
		<div id="tab-container" class="easyui-tabs" data-options="fit:true,border:false" >
			<!--<div title="首页" data-options="closable:true"> 
				
			</div>
		--></div>
	</div>
	<div data-options="region:'south'" style="height:50px;text-align:center;">
		<p><b><s:text name="main_str1"></s:text>&nbsp;</b>&copy;&nbsp;<span id="yy">2013</span>&nbsp;<a href='http://www.yidatec.com' target="_blank"><i>YIDATEC</i></a>&nbsp;Inc. All rights reserved.</p>
	</div>
</body>
</html>

