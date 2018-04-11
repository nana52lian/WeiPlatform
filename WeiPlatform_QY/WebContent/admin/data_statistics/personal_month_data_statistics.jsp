<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人月数据统计</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>
<script src="${pageContext.request.contextPath}/js/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/js/modules/exporting.js"></script>
<script src="${pageContext.request.contextPath}/js/modules/data.js"></script>
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
		padding:10px;
	}
	.conditionDiv {
		margin-bottom: 10px;
	}
	.mytext {
		width:100px;
	}
</style>
<script type="text/javascript">
$(function () {
	var date = new Date();
	var year = date.getFullYear();
	var month = parseInt(date.getMonth())+1;
	var day = date.getDate();
	if(month!="11"||month!="12"){
		month = "0" + month;
    }
	if(day<"10"){
		day = "0" + day;
	}
	var dateStr = year+"-"+month+"-"+day;
	var date = $("#searchDate");
	date.datebox("setValue",dateStr);
	
	searchData();
	
});


function searchData(){
	if($('#searchDate').datebox('getValue') == ""){
		alert('<s:text name="DATA_STATISTICS_STR10"></s:text>！');
		return;
	}
	$.post("<%=request.getContextPath()%>/weixin!getPersonalMonthDataStatistics",
        	{        		
				req_params : $('#searchDate').datebox('getValue')
        	},
        	function(data){
        		
        		getColumn(data.data.dateList,data.data.countList,data.data.sumCount);
        	},
        	"json"
        );
}
//柱状图
function getColumn(dateList,countList,sumCount){
	$('#column_container').highcharts({
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: '<s:text name="main_str11"></s:text>'
	        },
	        subtitle: {
                text: '<s:text name="DATA_STATISTICS_STR11"></s:text>（' + sumCount + '）<s:text name="DATA_STATISTICS_STR12"></s:text>'
            },
	        xAxis: {
	        	categories: dateList
	        },
	        yAxis: {
	        	min: 0,
	            allowDecimals: false,
	            title: {
	                text: ''
	            }
	        },
	        tooltip: {
	            pointFormat: '<s:text name="DATA_STATISTICS_STR3"></s:text>: <b>{point.y}</b>'
	        },
	        credits: {
	            enabled: false
	        },
	        series: [{
	            name: '<s:text name="DATA_STATISTICS_STR13"></s:text>',
	            data: countList
	        }]
	    });
}
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<div class="topDiv">
		<form id="conditions">
			<div class="conditionDiv">
				<label for="searchDate"><s:text name="DATA_STATISTICS_STR6"></s:text>：</label>
				<input type="text" id="searchDate" name="searchDate" class="easyui-datebox" >
	     	</div>		
		</form>
		<div style="padding-right:10px;text-align:right;">  
			<%-- <a id="btnClear" href="#" class="easyui-linkbutton" onclick="clearConditions();"><s:text name="CLEAR"></s:text></a> --%>
			<a id="btnQuery" href="javascript:void(0);" class="easyui-linkbutton" onclick="searchData();"><s:text name="SEARCH"></s:text></a>
		</div>
	</div>
	<div id="column_container" style="width:900px; height: 300px; float: left;"></div>
</div>
</body>
</html>