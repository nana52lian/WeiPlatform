<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关注信息同步</title>	
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script src="${pageContext.request.contextPath}/js/common.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"></script>

<script>

	$(function() {
		$('#p').progressbar({
		    value: 0
		});
	});
	
	// 获取一条用户信息的时间  
	var delay_time = 300;	
	// 同步信息
	function beginSync() {		
		$.messager.confirm('<s:text name="WARNING"></s:text>','<s:text name="SYNC_SUBSCRIBE_STR3"></s:text>',
			function(r) {
				if(r) {					
					$.post("${pageContext.request.contextPath}/weixin!loadSubscribeCount",
					    	{},
					    	function(data){	
					    		if(data.count == "0"){
					    			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="SYNC_SUBSCRIBE_STR4"></s:text>!');
					    		} else {
					    			// 数量 * 获取一条记录的时间 / 60秒（单位是毫秒）
					    			var tmp = Math.ceil(data.count * delay_time / 60000);
					    			$.messager.confirm("<s:text name="WARNING"></s:text>", data.count + "<s:text name="SYNC_SUBSCRIBE_STR5"></s:text>"+ tmp + "<s:text name="SYNC_SUBSCRIBE_STR10"></s:text>",
					    				function(r) {
					    					if(r) {
					    						// 禁用按钮
					    						$('#sync_btn').linkbutton({
					    							disabled: true
					    						});
					    						$('#msg').html('<s:text name="SYNC_SUBSCRIBE_STR6"></s:text>！');
					    						$('#p').progressbar('setValue', 0);					    						
					    						delay_time = parseInt(data.count / 100 * delay_time);
					    						setTimeout(function() {
					    							setProgressbar();
					    						}, delay_time);
					    						// 请求同步
					    						$.post("${pageContext.request.contextPath}/weixin!syncSubscribe",
					    								{},
					    								function(data) {
					    									if (data.resCode == 200) {					    										
					    										$('#msg').html('<s:text name="SYNC_SUBSCRIBE_STR7"></s:text>!');
					    										$('#sync_btn').linkbutton({
					    											disabled: false
					    										});
					    									} else {
					    										$('#msg').html('<s:text name="SYNC_SUBSCRIBE_STR8"></s:text>!');
					    									}
					    								},
					    								"json"
					    						);
					    					}
					    				}
					    			);
					    		}
					    	},
					    	"json"
				    );
				}
			}
		);
	}
	
	// 设置Progressbar
	function setProgressbar() {
		var value = $('#p').progressbar('getValue');
		if (value < 100) {
			$('#p').progressbar('setValue', ++value);
			setTimeout(function() {
				setProgressbar();
			}, delay_time);
		} else {
			$('#msg').html('<s:text name="SYNC_SUBSCRIBE_STR9"></s:text>...');			
		}
	}
	
	
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div style="padding: 10px;">
	<div class="topDiv" style="color:#76797d;">
		<s:text name="SYNC_SUBSCRIBE_STR1"></s:text>
	</div>	
	<div class="conditionDiv">
		<a id="sync_btn" href="javascript:void(0);" class="easyui-linkbutton" onclick="beginSync();"><s:text name="SYNC_SUBSCRIBE_STR2"></s:text></a>		
	</div>	
	<div id="p" style="width:400px;"></div>
	<div id="msg" style="width:400px;text-align:center;color:#76797d;"></div>
</div>	
</body>
</html>