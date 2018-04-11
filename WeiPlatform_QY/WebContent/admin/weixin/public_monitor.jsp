<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>监控</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/js/modules/exporting.js"></script>
<script src="${pageContext.request.contextPath}/js/modules/data.js"></script>

<style type="text/css">
#currentStatus {
	border:1px solid grey;
}
.odd { 
	border-top:0px solid red;
	background-color: #ffc;
	padding:5px;
} 
.even { 
	border:0px solid red;
	background-color: #cef;
	padding:5px;
} 

.odd span {
	color:#2F7ED8;
	font-weight:bold;
}

.even span {
	color:#2F7ED8;
	font-weight:bold;
}
</style>

<script type="text/javascript">
$(function () {
	refreshPage();
	setInterval(refreshPage, 30*1000);
});

function refreshPage(){
	$.post("${pageContext.request.contextPath}/weixin!getTodayProcessingCount",function(data){	
	    		//用户请求数以及活跃用户数
	    		getUser(data.requestCount,data.userActiveCount);
	    		//横的柱状图
	    		getBar(data.unProcessCount);
	    		//曲线图
	    		getLine(data.taskCount1,data.taskCount2,data.taskCount3,data.taskCount4,data.taskCount5,data.taskCount6,data.taskCount7,data.taskCount8,data.taskCount9,data.taskCount10);
	    		//圆型图
	    		getPie(data.requestTypeList);
	    		//柱状图
	    		getColumn(data.nameList,data.waitList,data.processList);
	    		//状态刷新
	    		refreshCurrentStatus(data.requestInfoList);
	    	},
	    	"json"
    );  
}

function refreshCurrentStatus(requests) {
	$("#currentStatus").html("");
	$.each(requests, function(i,val){
		 if(i%2==0){
			  if(val.taskStatus==0)
		      	  $("#currentStatus").append("<div class='odd'>用户<span>"+val.userName+"</span>发起请求&nbsp;&nbsp;"+val.timeing+"</div>");
		      else if(val.taskStatus==1)
		    	  $("#currentStatus").append("<div class='odd'>用户<span>"+val.userName+"</span>的请求已经分配给坐席<span>"+val.serverName+"</span>&nbsp;&nbsp;"+val.timeing+"</div>");
		      else if(val.taskStatus==2)
		    	  $("#currentStatus").append("<div class='odd'>坐席<span>"+val.serverName+"</span>正在处理<span>"+val.userName+"</span>的请求&nbsp;&nbsp;"+val.timeing+"</div>");
		      else if(val.taskStatus==3)
		    	  $("#currentStatus").append("<div class='odd'>坐席<span>"+val.serverName+"</span>关闭<span>"+val.userName+"</span>的请求&nbsp;&nbsp;"+val.timeing+"</div>");
		  }
		  else{
			  if(val.taskStatus==0)
		      	  $("#currentStatus").append("<div class='even'>用户<span>"+val.userName+"</span>发起请求&nbsp;&nbsp;"+val.timeing+"</div>");
		      else if(val.taskStatus==1)
		    	  $("#currentStatus").append("<div class='even'>用户<span>"+val.userName+"</span>的请求已经分配给坐席<span>"+val.serverName+"</span>&nbsp;&nbsp;"+val.timeing+"</div>");
		      else if(val.taskStatus==2)
		    	  $("#currentStatus").append("<div class='even'>坐席<span>"+val.serverName+"</span>正在处理<span>"+val.userName+"</span>的请求&nbsp;&nbsp;"+val.timeing+"</div>");
		      else if(val.taskStatus==3)
		    	  $("#currentStatus").append("<div class='even'>坐席<span>"+val.serverName+"</span>关闭<span>"+val.userName+"</span>的请求&nbsp;&nbsp;"+val.timeing+"</div>");
		  }
	  });   
}  

//用户请求数以及活跃用户数
function getUser(requestCount,userActiveCount){
	document.getElementById('requestCount').innerText=requestCount;
	document.getElementById('userActiveCount').innerText=userActiveCount;
}

//横的柱状图
function getBar(unProcessCount){
	
	$('#bar_container').highcharts({
		colors: ['#8bbc21'], 
		
        chart: {
            type: 'bar'
        },
        title: {
            text: ''
        },
        xAxis: {
            categories: ['<s:text name="MONITOR_STR3"></s:text>'],
            title: {
                text: null
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: '',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            },
            max:100, 
            allowDecimals:false 
        },
        tooltip: {
            valueSuffix: ''
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 100,
            floating: true,
            borderWidth: 1,
            backgroundColor: '#FFFFFF',
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: [{
            name: '<s:text name="MONITOR_STR3"></s:text>',
            data: [unProcessCount]
        }]
    });
}
//曲线图
function getLine(taskCount1,taskCount2,taskCount3,taskCount4,taskCount5,taskCount6,taskCount7,taskCount8,taskCount9,taskCount10){
	$('#line_container').highcharts({
        title: {
            text: '',
            x: -20 //center
        },
        subtitle: {
            text: '',
            x: -20
        },
        xAxis: {
            categories: ['9', '10', '11', '12', '13', '14', '15', '16', '17', '18']
        },
        yAxis: {
            title: {
                text: '<s:text name="MONITOR_STR4"></s:text>'
            },
            plotLines: [{value: 0,width: 1,color: '#808080'}], 
            allowDecimals:false 
        },
        tooltip: {
            valueSuffix: ''
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        credits: {
            enabled: false
        },
        series: [{
            name: '<s:text name="MONITOR_STR4"></s:text>',
            data: [taskCount1, taskCount2, taskCount3, taskCount4, taskCount5, taskCount6, taskCount7, taskCount8, taskCount9, taskCount10]
        }]
    });
}
//圆型图
function getPie(pieData){
	$('#pie_container').highcharts({
		chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: '<s:text name="MONITOR_STR5"></s:text>'
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
           enabled:false
    	},
        series: [{
            type: 'pie',
            name: '<s:text name="MONITOR_STR13"></s:text>',
            data: pieData
        }]
    });
}
//柱状图
function getColumn(nameList,waitList,processList){
	$('#column_container').highcharts({
		 // colors: ['#058DC7', '#ED561B'],
	        chart: {
	            type: 'column'
	        },
	        title: {
	            text: ''
	        },
	        xAxis: {
	        	categories: nameList
	            //categories: ['客服A','客服B','客服C','客服D','客服E']
	        },
	        yAxis: {
	            allowDecimals: false,
	            title: {
	                text: '<s:text name="MONITOR_STR4"></s:text>'
	            }, 
                allowDecimals:false 
	        },
	        tooltip: {
	            formatter: function() {
	                return '<b>'+ this.series.name +'</b><br/>'+
	                    this.y +' '+ this.x.toLowerCase();
	            }
	        },
	        credits: {
	            enabled: false
	        },
	        series: [{
	            name: '<s:text name="MONITOR_STR6"></s:text>',
	            //data: [3, 2, 5, 1, 2]
	            data: waitList
	        }, {
	            name: '<s:text name="MONITOR_STR7"></s:text>',
	            //data: [4, 0, 11, 1, 4]
	            data:processList
	        }]
	    });
}
</script>
</head>
<body>
<!--  
<div style="text-align:right"><a href="public_monitor_en.jsp" class="easyui-linkbutton" >英文</a></div>
-->
<div>
    <div style="float: left;width:300px;padding-left: 20px;">
        <div style="font-size: 30px;width:300px;padding-top: 10px;font-weight: bold;color: #2f7ed8;"><s:text name="MONITOR_STR1"></s:text>：<label id="requestCount"></label></div>
        <div style="font-size: 30px;width:300px;padding-top: 10px;font-weight: bold;color: #2f7ed8;"><s:text name="MONITOR_STR2"></s:text>：<label id="userActiveCount"></label></div>
    </div>
    <div id="bar_container" style="width:580px; height: 100px; float: left;"></div>
</div>
<!-- <div>
   <div id="line_container" style="width:600px; height: 200px; float: left;"></div>
</div> -->
<div id="line_container" style="width:500px; height: 250px; float: left;"></div>
<div id="pie_container" style="width:400px; height: 250px; float: left;"></div>
<div id="column_container" style="width:900px; height: 300px; float: left;"></div>
<div id="currentStatus" style="clear:both"></div>
</body>
</html>