<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>月数据</title>	
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
	}
	#barContainer {
	}
	#monthlyTotalCount {
		clear:both;
		text-align:center;
		font-size:14px;
		color:#274B6D;
		font-weight:bold;
	}
</style>
<script type="text/javascript">
$(function () {
	var date = new Date();
	var year = date.getFullYear();
	var month = parseInt(date.getMonth())+1;
	var day = date.getDate();
	var dateStr = year+"-"+month+"-"+day;
	var date = $("#date");
	date.datebox("setValue",dateStr);
	
	searchData();
	
});

function loadCharts(pieData,barData,categories){
	$('#pieContainer').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: '每名坐席月请求单数'
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
                }
            }
        },credits: {
            text: '',
            fontSize: '0'
    	},
        series: [{
            type: 'pie',
            name: '比例',
            data: pieData
        }]
    });
	
    $('#barContainer').highcharts({
        chart: {
            type: 'column',
            margin: [ 50, 50, 100, 80]
        },
        title: {
            text: '月统计'
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
                text: '单数'
            }
        },
        legend: {
            enabled: false
        },
        tooltip: {
            pointFormat: '单数: <b>{point.y}</b>',
        },credits: {
            text: '',
            fontSize: '0'
    	},
        series: [{
            data: barData,
            dataLabels: {
                enabled: true,
                rotation: -90,
                color: '#FFFFFF',
                align: 'right',
                style: {
                    fontSize: '13px',
                    fontFamily: 'Verdana, sans-serif',
                    textShadow: '0 0 3px black'
                }
            }
        }]
    });
}

function searchData(){
	$.post("${pageContext.request.contextPath}/statistics!searchMonthlyData",{param:$("#date").datebox("getValue")},function(data){
		loadCharts(data.pieData,data.barData,data.categories);
		$("#monthlyTotalCount").text("月请求单总数："+data.monthlyTotalCount);
	},"json");
}
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<div class="topDiv">
		<div class="conditionDiv">
	     	<label>日期：</label> 
			<input id="date" name="date" type="text" class="easyui-datebox" />
			&nbsp;&nbsp;
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchData();">查询</a>
     	</div>		
	</div>	
	<div id="pieContainer" ></div>	
	<div id="barContainer" ></div>
	<div id="monthlyTotalCount"></div>	
</div>	
</body>
</html>