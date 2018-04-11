<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>咨询师评价统计情况</title>
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
	//取得咨询师评价统计情况
	getAppraisalCountInfo();
});

//取得咨询师评价统计情况
function getAppraisalCountInfo(){
	$('#tt_appraisal_count_info').datagrid({ 
		title:'<s:text name="APPRAISAL_COUNT_STR1"></s:text>',
		width:'auto',
		height:'auto',
		idField:'id',
		url:'${pageContext.request.contextPath}/adviserMgr!getAppraisalCountInfo',
		nowrap: false,
		striped: true,
		collapsible:false,
		rownumbers:true,
	    columns:[[  
	        {field:'id',hidden:true},
	        {field:'name',title:'<s:text name="SUBSCRIBER_VIEW_STR9"></s:text>',width:90,align:'center'}, 
	        {field:'create_date',title:'<s:text name="USER_ROLE_VIEW_STR20"></s:text>',width:90,align:'center'}, 
	        {field:'type',title:'<s:text name="APPRAISAL_COUNT_STR2"></s:text>',width:90,align:'center',formatter:function(value,rowData,rowIndex){
	        	 var statusText="";
	        	  if(value=="2"){
	        		  statusText = "<s:text name='ADVISER_VIEW_STR10'></s:text>";
	        	  } else if(value=="3"){
	        		  statusText = "<s:text name='ADVISER_VIEW_STR11'></s:text>";
	        	  }
	        	  return statusText;
	        }},
	        {field:'login_time',title:'<s:text name="APPRAISAL_COUNT_STR3"></s:text>',width:160,align:'center'}, 
	        {field:'completed_count',title:'<s:text name="APPRAISAL_COUNT_STR4"></s:text>',width:170,align:'center'}, 
	        /* {field:'assess_count',title:'评价次数',width:150,align:'center',formatter:function(value,rowData,rowIndex){
	        	 var statusText=0;
	        	  if(value==null){
	        		  statusText = 0;
	        	  } else {
	        		  statusText = value;
	        	  }
	        	  return statusText;
	        }}, */
	        {field:'avg_score',title:'<s:text name="APPRAISAL_COUNT_STR5"></s:text>',width:120,align:'center'},
	        /* {field:'action',title:'详细',width:100,align:'center',  
                formatter:function(value,row,index){
                	var contentHref = "javascript:parent.addTabPane('"+row.id+"','"+row.name+"_评价详情','${pageContext.request.contextPath}/pages/advisory_manage/appraisal_info.jsp?serviceID="+ row.id +"');";
                	return '<a href="#" onclick='+contentHref+' >查看</a> ';  
                }
            }  */ 
	    ]] ,
	    pagination:true,
		pageList:[10,20,30]	
	});
}
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
	<table id="tt_appraisal_count_info" ></table>
</body>
</html>