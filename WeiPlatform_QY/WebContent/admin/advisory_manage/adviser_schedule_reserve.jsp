<%@ page language="java" import="com.yidatec.weixin.common.GlobalDef" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<%@ taglib uri="/struts-tags" prefix="s"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的预约咨询管理</title>	
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>
 
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
		width:160px;
	}
	
	.border_table {
		border:solid #add9c0; 
		border-width:1px 0px 0px 1px;
	}
	
	.border_table tbody tr td {
		border:solid #add9c0; 
		border-width:0px 1px 1px 0px;		
	}
	
</style>
<script>

	$(function() {
		searchScheduleReserve();
	});
	
	function searchScheduleReserve() {
		$('#tt_schedule_reserve').datagrid({ 
			title:'我的预约咨询',
			width:'auto',
			height:'auto',
			idField:'id',
			url:'${pageContext.request.contextPath}/adviserMgr!queryScheduleReserve',
			queryParams : {				
				req_params : JSON.stringify(serializeObject($("#conditions")))
			},
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			singleSelect:true,
		    columns:[[  
		        {field:'id',hidden:true},
		        {field:'advisoryAccount',title:'账户',width:90,align:'center'}, 
		        {field:'create_date',title:'预约发起时间',width:150,align:'center'},
		        {field:'beginTime',title:'咨询开始时间',width:150,align:'center'},
		        {field:'status',title:'状态',width:100,align:'center',formatter:function(value,rowData,rowIndex){
		        	  var statusText='';
		        	  if(value=='1')
		        		  statusText = '预约待确认';
		        	  else if(value=='2')
		        		  statusText = '预约已确认';
		        	  else if(value=='3')
		        		  statusText = '咨询待反馈';
		        	  else if(value=='4')
		        		  statusText = '咨询完毕';
		        	  else if(value=='0')
		        		  statusText = '取消预约';
		        	  return statusText;
		        }},
		        {field:'qq',title:'QQ',width:90,align:'center'},
		        {field:'mobile_phone',title:'电话号码',width:90,align:'center'},
		        {field:'format_feedback',title:'咨询反馈',width:80,align:'center',
	                formatter:function(value,rowData,rowIndex){
	                	var str = "<a href='javascript:void(0);' onclick='getFeedback(\""+ rowData.id +"\")'>"+value+"</a>";
	                    return str;	
	                }
		        }
		    ]] ,
		    pagination:true,
			pageList:[10,20,30],
			frozenColumns:[[
	      		{field:'ck',checkbox:true} 
			]]			
		});  
	}
	
	//查看咨询反馈
	function getFeedback(feedback_id) {
		document.getElementById("feedback_id").value = feedback_id;
		$.post("<%=request.getContextPath()%>/adviserMgr!getScheduleReserveInfo",
        	{        		
				params : feedback_id
        	},
        	function(data){
        		document.getElementById("feedback").value = data.feedback;
        	},
        	"json"
        );
		$('#feedbackDiv').window({
            modal:true,
            cache:false,
            left:150,
            top:150
        });
   		$('#feedbackDiv').window('open');
	}
	
	function feedbackDivCancle(){
		$('#tt_schedule_reserve').datagrid("clearSelections");
		$('#feedbackDiv').window('close');
	}
	
	//预约咨询确认
	function confirmReserve() {
		var rows = $('#tt_schedule_reserve').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('警告','您没有选择任何记录，无法进行操作!');
			return;
		}
		if (rows[0].status != 1) {			
			$.messager.alert('警告','您选择的记录，无法进行确认预约操作!');
			return;
		}
		$.messager.confirm("警告","您确定进行确认预约操作 ?",
				function(r){
					if(r){
						var id = rows[0].id;
						var params = '{"id":"' + id + '", "status":"2"}';
						$.post("<%=request.getContextPath()%>/adviserMgr!scheduleReserveAction",
				        	{        		
				        		req_params : params
				        	},
				        	function(data){
				        		$.messager.alert('信息', data.resDesc);
				        		$('#tt_schedule_reserve').datagrid("clearSelections");
				        		$('#tt_schedule_reserve').datagrid("reload");
				        	},
				        	"json"
				        );
					}
				}
			);
		
	}
	
	//预约咨询完成
	function completeReserve() {
		var rows = $('#tt_schedule_reserve').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('警告','您没有选择任何记录，无法进行操作!');
			return;
		}
		if (rows[0].status != 2) {			
			$.messager.alert('警告','您选择的记录，无法进行完成操作!');
			return;
		}
		//获取系统时间
		var myDate = new Date();
	    var yy = myDate.getYear();
	    if(yy<1900) yy = yy+1900;
	    var MM = myDate.getMonth()+1;
	    if(MM<10) MM = '0' + MM;
	    var dd = myDate.getDate();
	    if(dd<10) dd = '0' + dd;
	    var hh = myDate.getHours();
	    if(hh<10) hh = '0' + hh;
	    var mm = myDate.getMinutes();
	    if(mm<10) mm = '0' + mm;
	    var ss = myDate.getSeconds();
	    if(ss<10) ss = '0' + ss;
	    var nowDate = yy + "-" + MM + "-" + dd + " " + hh + ":" + mm + ":" + ss;
	    //系统时间与咨询开始时间进行比较，系统时间小于咨询开始时间，禁止进行完成咨询操作
        var beginTime = rows[0].beginTime;
        var sysTime = nowDate;
        var beginTimes = beginTime.substring(0, 10).split('-');
        var sysTimes = sysTime.substring(0, 10).split('-');

        beginTime = beginTimes[1] + '-' + beginTimes[2] + '-' + beginTimes[0] + ' ' + beginTime.substring(10, 19);
        sysTime = sysTimes[1] + '-' + sysTimes[2] + '-' + sysTimes[0] + ' ' + sysTime.substring(10, 19);
        var a = (Date.parse(sysTime) - Date.parse(beginTime)) / 3600 / 1000;
        if (a < 0) {
        	//不可以操作
        	$.messager.alert('警告','现在无法进行完成操作，请耐心等待!');
            return;
        } 
		$.messager.confirm("警告","您确定进行完成预约操作 ?",
				function(r){
					if(r){
						var id = rows[0].id;
						var advisoryAccount = rows[0].advisoryAccount;
						var params = '{"id":"' + id + '", "account":"' + advisoryAccount + '"}';
						$.post("<%=request.getContextPath()%>/adviserMgr!completeReserve",
				        	{        		
				        		req_params : params
				        	},
				        	function(data){
				        		$.messager.alert('信息', data.resDesc);
				        		$('#tt_schedule_reserve').datagrid("clearSelections");
				        		$('#tt_schedule_reserve').datagrid("reload");
				        	},
				        	"json"
				        );
					}
				}
			);
		
	}
	
	//取消预约咨询
	function cancelReserve() {
		var rows = $('#tt_schedule_reserve').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('警告','您没有选择任何记录，无法进行操作!');
			return;
		}
		if (rows[0].status != 1) {			
			$.messager.alert('警告','您选择的记录，无法进行取消操作!');
			return;
		}
		$.messager.confirm("警告","您确定进行取消预约操作 ?",
				function(r){
					if(r){
						var id = rows[0].id;
						var params = '{"id":"' + id + '", "status":"0"}';
						$.post("<%=request.getContextPath()%>/adviserMgr!scheduleReserveAction",
				        	{        		
				        		req_params : params
				        	},
				        	function(data){
				        		$.messager.alert('信息', data.resDesc);
				        		$('#tt_schedule_reserve').datagrid("clearSelections");
				        		$('#tt_schedule_reserve').datagrid("reload");
				        	},
				        	"json"
				        );
					}
				}
			);
		
	}
	
	//修改预约开始咨询
	function editTime() {
		var rows = $('#tt_schedule_reserve').datagrid("getSelections");
		if (rows.length < 1) {			
			$.messager.alert('警告','您没有选择任何记录，无法进行操作!');
			return;
		}
		if (rows[0].status != 1 && rows[0].status != 2) {			
			$.messager.alert('警告','您选择的记录，无法更改开始时间，请重新选择!');
			return;
		}
		var account = rows[0].advisoryAccount;
		$('#radioId').val(rows[0].id);
		$.post("<%=request.getContextPath()%>/adviserMgr!getScheduleReserveTimes",
        	{        		
				params : account
        	},
        	function(adviserEntity){
        		loadTimes(adviserEntity);
        	},
        	"json"
        );
		$('#editTimeDiv').window({
            modal:true,
            cache:false,
            left:50,
            top:50
        });
   		$('#editTimeDiv').window('open');
	}
	
	//加载空闲时间
	function loadTimes(adviserEntity){
		var times1 = adviserEntity.todayFreeTimes;
		var radios1="";
		var radios2="";
		var radios3="";
		for (var i=0;i<times1.length;i++)
		{
			radios1 += "<input type='radio' id=times1_'"+times1[i]+"' name=freeTimeRadio value=a"+times1[i]+" />"+ times1[i] +"点" + "&nbsp;&nbsp;";
		}
		time1.innerHTML=radios1;
		
		var times2 = adviserEntity.freeTimes1;
		for (var j=0;j<times2.length;j++)
		{
			radios2 += "<input type='radio' id=times2_'"+times2[j]+"' name=freeTimeRadio value=b"+times2[j]+" />"+ times2[j] +"点" + "&nbsp;&nbsp;";
		}
		time2.innerHTML=radios2;
		
		var times3 = adviserEntity.todayFreeTimes;
		for (var k=0;k<times3.length;k++)
		{
			radios3 += "<input type='radio' id=times3_'"+times3[k]+"' name=freeTimeRadio value=c"+times3[k]+" />"+ times3[k] +"点" + "&nbsp;&nbsp;";
		}
		time3.innerHTML=radios3;
		
	}
	
	function updateTimeConfirm(){
		var id = $('#radioId').val();
		var times = $("input[name='freeTimeRadio']:checked").val();
		$.messager.confirm("警告","您确定修改预约时间 ?",
				function(r){
					if(r){
						var params = '{"id":"' + id + '", "freeTimeRadio": "' + times +'"}';
						$.post("<%=request.getContextPath()%>/adviserMgr!updateReserveTime",
				        	{        		
				        		req_params : params
				        	},
				        	function(data){
				        		$.messager.alert('信息', data.resDesc);
				        		$('#tt_schedule_reserve').datagrid("clearSelections");
				        		$('#tt_schedule_reserve').datagrid("reload");
				        		$('#editTimeDiv').window('close');
				        	},
				        	"json"
				        );
					}
				}
			);
	}
	
	function updateTimeCancle(){
		$('#tt_schedule_reserve').datagrid("clearSelections");
		$('#editTimeDiv').window('close');
	}
	
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<input type="hidden" id="feedback_id" name="feedback_id" />
	<div style="padding:15px;text-align:left;">  
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="confirmReserve();">确认预约</a>  
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="completeReserve();">完成预约</a>  
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="cancelReserve();">取消预约</a>
		<a href="javascript:void(0);" class="easyui-linkbutton" onclick="editTime();">修改时间</a>
	</div>
	<table id="tt_schedule_reserve"></table>
	
	<div id="feedbackDiv" class="easyui-window" closed="true" title="咨询反馈" style="width:430px;height:215px;">  
        <div style="padding: 10px;">
            <div style="width:auto; height:auto;">  
            	<label>咨询反馈</label>
            	<textarea rows="5" cols="50" id="feedback" name="feedback"></textarea>
                <div style="padding:5px;text-align:center;">  
                    <a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="feedbackDivCancle();">返回</a>  
                </div>  
            </div>
        </div>
    </div>
    <div id="editTimeDiv" class="easyui-window" closed="true" title="修改时间" style="width:600px;height:200px;">  
        <div style="padding: 10px;">
	        <form name="form" action="advisory!orderAdvisory" method="post" onsubmit="return check();">
	        	<input type="hidden" id="radioId" name="radioId" />
				<table width="100%" border="1" cellpadding="3" cellspacing="0" class="InfoShowPanel">
					<tbody id="tbody" >
					  	<tr>
					  		<td colspan="2" align="center">预约时间（以1小时为单位）</td> 
					  	</tr>
					  	<tr>
					  		<td width="77px;"  align="center" >今天</td>
					  		<td id="time1">
					  		</td>
					  	</tr>
					  	<tr>
					  		<td align="center">明天</td>
					  		<td id="time2"></td>
					  	</tr>
					  	<tr>
					  		<td align="center">后天</td>
					  		<td id="time3"></td>
					  	</tr>
					</tbody>
				</table>
				<div style="padding:5px;text-align:center;">  
                    <a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="updateTimeConfirm();">确定</a>  
                    <a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="updateTimeCancle();">返回</a> 
                </div>
			</form>
        </div>
    </div>
</div>
</body>
</html>