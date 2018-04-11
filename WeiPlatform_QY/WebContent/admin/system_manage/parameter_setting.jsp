<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>参数设定</title>	
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
		width:100px;
	}
</style> 
<script>
	$(function() {
		$('#tt').datagrid({ 
			width:'auto',
			height:'auto',
			idField:'id',
			nowrap: false,
			striped: true,
			collapsible:false,
			rownumbers:true,
			autoSizeColumn:true, 
	        iconCls:'icon-save',
		    url:'${pageContext.request.contextPath}/adminResourceMgr!loadParams',
		    columns:[[  
		        {field:'id',hidden:true},
		        {field:'param_value_hidden',hidden:true},
		        {field:'param_description',title:'<s:text name="PARAMETER_SETTING_STR1"></s:text>',width:350,align:'left'},  
		        {field:'param_value',title:'<s:text name="PARAMETER_SETTING_STR2"></s:text>',editor:'text', align:'left'}, 
		        {field:'action',title:'<s:text name="PARAMETER_SETTING_STR3"></s:text>',width:100,align:'center',  
	                formatter:function(value,row,index){
	                	if (row.editing){  
	                        var save = '<a href="#" onclick="saverow('+index+')"><s:text name="PARAMETER_SETTING_STR4"></s:text></a> ';  
	                        var cancel = '<a href="#" onclick="cancelrow('+index+')"><s:text name="PARAMETER_SETTING_STR5"></s:text></a>';  
	                        return save+cancel;  
	                    } else {  
	                        var edit = '<a href="#" onclick="editrow('+index+')"><s:text name="PARAMETER_SETTING_STR6"></s:text></a> ';  
	                        //var del = '<a href="#" onclick="deleterow('+index+')">删除</a> ';
	                        return edit; 
	                    }  
	                }
	            }  
		    ]] ,
			onBeforeEdit:function(index,row){  
	            row.editing = true;  
	            updateActions();  
	        },  
	        onAfterEdit:function(index,row){  
	            row.editing = false;  
	            updateActions();
	            saveResult(row.id,row.param_name,row.param_value,index);
	        },  
	        onCancelEdit:function(index,row){  
	            row.editing = false;  
	            updateActions();
	        },
	        onLoadSuccess:function() {
	        	var div01=$("td[field='param_value'] div");  
	            div01.css({  
		            "width":150,  
		            "white-space":"nowrap",  
		            "text-overflow":"ellipsis",  
		            "-o-text-overflow":"ellipsis",  
		            "overflow":"hidden"  
            	});
	        }
		});  
	});	
	function saveResult(id,name,value,index){
		if(name=="redundancy_num"){
			if(value<1 || value>4){
				parent.$.messager.alert("<s:text name='PARAMETER_SETTING_STR7'></s:text>","<s:text name='PARAMETER_SETTING_STR8'></s:text>");
				editrow(index);
				return;
			}				
		}
		
		if(name=="workday_time" || name=="restday_time"){
			var re = /^(\d|1\d|2[0-3]):([0-5]\d)\/(\d|1\d|2[0-3]):([0-5]\d)$/;
			if(!re.test(value)){
				parent.$.messager.alert("<s:text name='PARAMETER_SETTING_STR7'></s:text>","<s:text name='PARAMETER_SETTING_STR9'></s:text>！");
				editrow(index);
				return;
			}
		}
		
		if(name=="temp_working_time"){
			var re = /^([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))\/([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))\s(\d|1\d|2[0-3]):([0-5]\d)\/(\d|1\d|2[0-3]):([0-5]\d)$/;
			if(value!="" &&!re.test(value)){
				parent.$.messager.alert("<s:text name='PARAMETER_SETTING_STR7'></s:text>","<s:text name='PARAMETER_SETTING_STR9'></s:text>！");
				editrow(index);
				return;
			}
		}
			
		$.post("${pageContext.request.contextPath}/adminResourceMgr!updateParams",{id:id,paramDatas:name,message:value},function(data){
			parent.$.messager.alert("<s:text name='PARAMETER_SETTING_STR7'></s:text>",data);
		});
	}
	function updateActions(){  
	    var rowcount = $('#tt').datagrid('getRows').length;  
	    for(var i=0; i<rowcount; i++){  
	        $('#tt').datagrid('updateRow',{  
	            index:i,  
	            row:{action:''}  
	        });  
	    }  
	}  
	function editrow(index){  
	    $('#tt').datagrid('beginEdit', index);  
	}  
	function saverow(index){  
	    $('#tt').datagrid('endEdit', index);  
	}  
	function cancelrow(index){  
	    $('#tt').datagrid('cancelEdit', index);  
	}  
	//保存页面数据
	var paramTempIndex=0;
	function save(){
		var a=$('#tt').datagrid('getData'); 
		alert(a);
		var paramRows = $("#tt").datagrid("getRows");
		for(var key in paramRows){
			var paramRow = paramRows[key];
			var type = paramRow.type;
			var param_name = paramRow.param_name;
			var param_description = paramRow.param_description;
			var param_value = paramRow.param_value;
			
			var hiddenParamDiv = $("#hiddenParamDiv");
			
			var typeEle = $("<input name='param["+paramTempIndex+"].type' value='"+type+"'>");
			var param_nameEle = $("<input name='param["+paramTempIndex+"].param_name' value='"+param_name+"'>");
			var param_descriptionEle = $("<input name='param["+paramTempIndex+"].param_description' value='"+param_description+"'>");
			var param_valueEle = $("<input name='param["+paramTempIndex+"].param_value' value='"+param_value+"'>");
			
			var itemDiv = $("<div id='paramItemDiv"+paramTempIndex+"'>");
			
			itemDiv.append(typeEle);
			itemDiv.append(param_nameEle);
			itemDiv.append(param_descriptionEle);
			itemDiv.append(param_valueEle);
			
			hiddenParamDiv.append(itemDiv);
			
			paramTempIndex++;
		}
		$('#commitForm').ajaxSubmit({success:function(){
			location.reload(true);
		}});
			
	}
	function upload(rowIndex,rowId,value){
		$("#id").val(rowId);
		$("#paramValue").val(value);
    	$('#uploadDiv').window({  
            modal:true,
            width:400,
            height:200,
            left:200,
            top:200
        });
    	$('#uploadDiv').window('open');
	}
	/*关闭弹出窗口*/
	function cancleWindow(id){
		$('#'+id).window('close');
	}
	function uploadLogo(){
		$('#uploadForm').ajaxSubmit({success:uploadResult,dataType:'json'});
	}
	function uploadResult(responseText, statusText){
		//var path = responseText.filename;
		$('#uploadDiv').window('close');
		$('#tt').datagrid("reload");
	}
</script>
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
	<div style="padding: 10px;">
		<form id="commitForm" action="${pageContext.request.contextPath}/adminResourceMgr!addParams" method="post" >
			<div id="hiddenParamDiv" style="display:none;"></div>
			<div>
				<table id="tt"></table>
			</div>
			<%-- 
			<div align="center" style="margin-top:20px;">
				<a href="#" class="easyui-linkbutton" onclick="save();">保存</a>
			</div>
			--%>
		</form>
	</div>
	
	<div id="uploadDiv" class="easyui-window" closed="true" title="" style="padding:20px">
		<form id="uploadForm" action="${pageContext.request.contextPath}/adminPlatformUserMgr!uploadLogo" method="post" enctype="multipart/form-data">
   			<input type="file" name="uFile">
   			<input type="hidden" id="id" name="id" >
   			<input type="hidden" id="paramValue" name="paramValue" >
   		</form>
   		<div style="padding:5px;text-align:center;">  
           	<a href="javascript:void(0);" id="updateButton" class="easyui-linkbutton" icon="icon-ok" onclick="uploadLogo('uploadDiv');"><s:text name="SAVE"></s:text></a>  
           	<a href="javascript:void(0);" id="cancleButton" class="easyui-linkbutton" icon="icon-cancel" onclick="cancleWindow('uploadDiv');"><s:text name="CANCEL"></s:text></a>  
       	</div>
	</div>
</body>
</html>