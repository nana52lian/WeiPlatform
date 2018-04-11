<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String id = request.getParameter("id") == null ? "" : request.getParameter("id").toString();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>	
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/admin/article_manage/js/ueditor/style.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jQselect.js"></script> 
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/admin/article_manage/js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/admin/article_manage/js/ueditor/ueditor.all.js"></script> --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ueditor/ueditor.all.js"></script>
<style type="text/css">
#edui1
{
    margin: 0 auto;
    clear: both;
    display: block;
    overflow: auto;
    background-color: #fff;
    box-shadow: 0 0 10px #333;
}
</style>
<script type="text/javascript">

	var editor_a;
	var article_id = '<%=id%>';

	$(function () {		
	    loadeditor();
	    loadSections();
	    initArticle();
	});
	
	function loadeditor() {		
	    var editorOption = {
	        initialFrameHeight: 560,
	        initialFrameWidth: 650,
	        zIndex:900
	    };
	    editor_a = new UE.ui.Editor(editorOption);
	    editor_a.render('myEditor');
	}
	
	function loadSections() {
		$('#section').combobox({  
			url:'<%=request.getContextPath()%>/articleMgr!loadSections',  
			valueField:'id',  
	   		textField:'name',
	   		onSelect:function(record) {
	   			loadCategories(record.id);
	   			$('#category').combobox({  
    				onLoadSuccess:function() {
    					$('#category').combobox("setValue", "");
    				}
    			});
	   		}
		}); 
	}
	
	function loadCategories(id) {
		$('#category').combobox({  
			url:'<%=request.getContextPath()%>/articleMgr!loadCategoriesBySection?section=' + id,  
			valueField:'id',  
	   		textField:'name'
		}); 
	}

	function initArticle() {
		if (article_id) {
			var params = '{"id":"' + article_id + '"}';
			$.post("<%=request.getContextPath()%>/articleMgr!loadArticleById",
	        	{
	        		req_params : params
	        	},
	        	function(data){
	        		if (data.resCode) {
	        			$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
	        		} else {
	        			$("#action_no").val("2");
	        			$("#id").val(data.id);
	        			editor_a.ready(function(){	        			    
	        				editor_a.setContent(data.content);
	        			});       			
	        			$('#section').combobox("setValue", data.section_id);
	        			// 加载子类
	        			loadCategories(data.section_id);
	        			$('#category').combobox({  
	        				onLoadSuccess:function() {
	        					$('#category').combobox("setValue", data.catagory_id);
	        				}
	        			}); 
	        			$("#title").val(data.title);
	        			$("#writer").val(data.writer);
	        			$('#status').combobox("setValue", data.status);
	        		}
	        	},
	        	"json"
	        );
		}
	}
	
	function saveArticle() {
		if(!$('#article_params').form('validate')) return;
		$("#content").text(editor_a.getContent());
		if($("#content").text().length == 0){
			$.messager.alert('<s:text name="WARNING"></s:text>','<s:text name="article_str10"></s:text>!');
			return;
		}		
		var params = JSON.stringify(serializeObject($("#article_params")));		
		$.post("<%=request.getContextPath()%>/articleMgr!articleAction",
        	{
				req_params : params
        	},
        	function(data){
        		$.messager.alert('<s:text name="INFO"></s:text>', data.resDesc);
        		if (data.resCode == 200) {
        			editor_a.setContent("");
        			$("#content").text("");
        			$("#title").val("");
        		}
        	},
        	"json"
        ); 
	}

</script>
    
</head>
<body>
<s:include value="../langCommon.jsp"></s:include>
<div id="content_body" style="width:98%; margin:0 auto">
<form id="article_params">
	<input type="hidden" id="action_no" name="action_no" value="1"/>
	<input type="hidden" id="id" name="id" />
    <div id="content_left" >
        <div>
        	<textarea id="myEditor"></textarea>
        </div>
    </div>
    <div id="content_right" style="height:690px; width:300px;margin-left:10px;border-right:1px solid #ced2dd">
        <h2><s:text name="article_str1"></s:text></h2>
        <div>
            <table cellpadding="5" cellspacing="0" border="0">
                <tr>
                    <th><s:text name="article_str2"></s:text></th>
                    <td>
                    	<input id="section" name="section" class="easyui-combobox" panelHeight="auto" required="true"/>
                    </td>
                </tr>
                <tr>
                    <th><s:text name="article_str3"></s:text></th>
                    <td>
                    	<input id="category" name="category" class="easyui-combobox" panelHeight="auto" required="true"/>
                    </td>
                </tr>
                <tr>
                    <th><s:text name="article_str4"></s:text></th>
                    <td>
                    	<input type="text" id="title" name="title" class="easyui-validatebox" required="true"/>
                    </td>
                </tr>
                <tr>
                    <th><s:text name="article_str5"></s:text></th>
                    <td>
						<input type="text" id="writer" name="writer" class="easyui-validatebox" required="true"/>
                    </td>
                </tr>
                <tr>
                    <th><s:text name="article_str6"></s:text></th>
                    <td>
                    	<select id="status" class="easyui-combobox" name="status" panelHeight="auto">
						    <option value="0"><s:text name="article_str7"></s:text></option>
						    <option value="1"><s:text name="article_str8"></s:text></option>						    
						</select>
                    </td>
                </tr>
                <tr>
                    <th colspan="2">
                    	<div id="content_left_control" style="background-color: #f8f8f8;width:65px;border:0;">
				            <a href="#" class="icon_save" onclick="saveArticle()" style="margin:0;"><s:text name="article_str9"></s:text></a>
				        </div>
                    </th>                    
                </tr>
                
            </table>
        </div>
    </div>
    <div style="display:none;">
  		<textarea name="content" id="content"></textarea>
  	</div>
    <div class="clear"></div>
</form>
</div>
</body>
</html>