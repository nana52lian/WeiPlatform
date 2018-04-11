<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>预约结果</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/weixin.css">
</head>
<body>
<s:property value="result" />
<s:if test="orderStatus=='成功'">
	<br /><br />
	<s:property value="advisoryName" />的联系方式：<s:property value="advisoryPhone" />
	<br /><br />
	咨询顾问不一定立即发现该预约，请务必使用您的预留方式尽快联系顾问敲定咨询时间。并及时支付咨询费用，取消咨询请联系咨询师。
	详细支付账号请返回微信输入数字3，查看当前预约
</s:if>
<s:elseif test="orderStatus=='失败'">
	<br /><br />
	系统错误，请联系管理员
</s:elseif>
<s:elseif test="orderStatus=='未完成'">
	<br /><br />
	您当前有正在进行中的预约，请返回微信输入数字3，查看当前预约，联系当前预约的咨询顾问，先取消咨询或完成咨询后，然后再进行预约，感谢合作。
</s:elseif>
<s:elseif test="orderStatus=='未反馈'">
	<br /><br />
	您上次的咨询还没有及时评价和反馈，请返回微信输入数字4，给上一次的咨询顾问做评价和反馈。然后再进行预约。管理员代表草草们谢谢您了。
</s:elseif>
</body>
</html>