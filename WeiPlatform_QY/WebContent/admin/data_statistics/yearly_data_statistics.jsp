<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>年数据</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
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
		width:100px;
	}
	#pieContainer {
		width:350px;
		float:left;
	}
	#barContainer {
		float:left;
	}
	#yearlyTotalCount {
		clear:both;
		text-align:center;
		font-size:14px;
		color:#274B6D;
		font-weight:bold;
	}
</style>
<script type="text/javascript">
	$(function(){
		var date = new Date();
		var year = date.getFullYear();
		var month = parseInt(date.getMonth())+1;
		var day = date.getDate();
		var dateStr = year+"-"+month+"-"+day;
		var toDate = $("#toDate");
		toDate.datebox("setValue",dateStr);
		var fromDate = $("#fromDate");
		fromDate.datebox("setValue",year+"-01-01");
		
		$('#server').combobox({ 
			url : "${pageContext.request.contextPath}/statistics!getServers", 
			valueField : 'id', 
			textField : 'name',
			width : 120,
			height: 'auto',
			onLoadSuccess:function(){
				$('#server').combobox("select","-1");
			}
	 	}); 
		
		searchData();
		
	})
	
	function searchData(){
		var params = {
			id	    : $("#server").combobox("getValue"),
			fromDt	: $("#fromDate").datebox("getValue"),
			toDt	: $("#toDate").datebox("getValue")
		};
		$.post("${pageContext.request.contextPath}/statistics!searchYearlyData",{param:JSON.stringify(params)},function(data){
			loadCharts(data.pieData,data.barData,data.categories);
			$("#yearlyTotalCount").text('<s:text name="DATA_STATISTICS_STR8"></s:text>：'+data.yearlyTotalCount);
		},"json");
	}
	
	function loadCharts(pieData,barData,categories){
		$('#pieContainer').highcharts({
	        chart: {
	            plotBackgroundColor: null,
	            plotBorderWidth: null,
	            plotShadow: false
	        },
	        title: {
	            text: '<s:text name="DATA_STATISTICS_STR1"></s:text>'
	        },
	        tooltip: {
	    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	        },
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: true,
	                    color: '#000000',
	                    connectorColor: '#000000',
	                    format: '<b>{point.name}</b>: {point.y}'
	                },
	       		 	showInLegend: true
	            }
	        },credits: {
                text: '',
                fontSize: '0'
        	},
	        series: [{
	            type: 'pie',
	            name: '<s:text name="MONITOR_STR13"></s:text>',
	            data: pieData
	        }]
	    });
		
	    $('#barContainer').highcharts({
	        chart: {
	            type: 'column',
	            margin: [ 50, 50, 100, 80]
	        },
	        title: {
	            text: '<s:text name="DATA_STATISTICS_STR9"></s:text>'
	        },
	        xAxis: {
	            categories: categories,
	            labels: {
	                rotation: 0,
	                align: 'right',
	                style: {
	                    fontSize: '13px',
	                    fontFamily: 'Verdana, sans-serif'
	                }
	            }
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '<s:text name="DATA_STATISTICS_STR3"></s:text>'
	            }, 
                allowDecimals:false 
	        },
	        legend: {
	            enabled: false
	        },
	        tooltip: {
	            pointFormat: '<s:text name="DATA_STATISTICS_STR3"></s:text>: <b>{point.y}</b>',
	        },credits: {
                text: '',
                fontSize: '0'
        	},
	        series: [{
	            name: 'Population',
	            data: barData,
	            dataLabels: {
	                enabled: true,
	                rotation: -90,
	                color: '#FFFFFF',
	                align: 'right',
	                x: 4,
	                y: 10,
	                style: {
	                    fontSize: '13px',
	                    fontFamily: 'Verdana, sans-serif',
	                    textShadow: '0 0 3px black'
	                }
	            }
	        }]
	    });	
	}	

	function exportTaskForMonth(){
		 //if($("#server").combobox("getValue") == "" || $("#server").combobox("getValue") == "-1"){
		//	   $.messager.alert('<s:text name="INFO"></s:text>','<s:text name="DATA_STATISTICS_STR14"></s:text>');
		//	   return;
		//   }
		   if($("#fromDate").datebox("getValue") == ""){
			   $.messager.alert('<s:text name="INFO"></s:text>','<s:text name="DATA_STATISTICS_STR16"></s:text>');
			   return;
		   }
		   if($("#toDate").datebox("getValue") == ""){
			   $.messager.alert('<s:text name="INFO"></s:text>','<s:text name="DATA_STATISTICS_STR17"></s:text>');
			   return;
		   } 
		   document.exportForm.submit();
	}
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<form id="exportForm" name="exportForm" action="<%=request.getContextPath()%>/statistics!exportTaskForMonth" method="post" >
<div style="padding: 10px;">
	<div class="topDiv">
		<div class="conditionDiv">
			<label><s:text name="MONITOR_STR8"></s:text>：</label>
			<input class="easyui-combobox" id="server" name="server">
            &nbsp;&nbsp;
	     	<label><s:text name="DATA_STATISTICS_STR6"></s:text>：</label> 
			<input id="fromDate" name="fromDate" class="easyui-datebox" />
			&nbsp;&nbsp;
			<label><s:text name="DATA_STATISTICS_STR7"></s:text>：</label> 
			<input id="toDate" name="toDate" class="easyui-datebox" />
			&nbsp;&nbsp;
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchData();"><s:text name="SEARCH"></s:text></a>
			&nbsp;&nbsp;
     	    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="exportTaskForMonth();"><s:text name="EXPORT_STR"></s:text></a> 
     	</div>		
	</div>	
	<div id="pieContainer" ></div>	
	<div id="barContainer" ></div>
	<div id="yearlyTotalCount"></div>
</div>	
</form>
</body>
</html>