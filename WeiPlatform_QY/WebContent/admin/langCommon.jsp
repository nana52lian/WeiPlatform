<script type="text/javascript">
var langtype = '<%=session.getAttribute("lang")%>';
if(langtype == "zh_CN"){
	document.write("<script type='text/javascript' src='${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js'><\/script>");
}
else {
	document.write("<script type='text/javascript' src='${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-en.js'><\/script>");
}
</script>