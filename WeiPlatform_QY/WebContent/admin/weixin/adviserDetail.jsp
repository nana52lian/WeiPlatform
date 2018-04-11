<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>咨询师详细信息</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/weixin.css">
<script type="text/javascript">
	function check(){
		if(!document.getElementById("sureButton").checked){
			alert("请确认同意咨询说明书内容"); 
			return false;
		}
		var types = document.getElementsByName("adviseType");
		var flag1=false;
		for(var i in types){
			var radio = types[i];
			if(radio.checked){
				flag1 = true;
				if(document.getElementById(radio.id+"Txt").value==""){
					alert("请输入对应的联系方式");
					return false;
				}
			}
		}
		if(!flag1){
			alert("请选择咨询方式");
			return false;
		}
		var times = document.getElementsByName("freeTimeRadio");
		var flag2 = false;
		for(var i in times){
			var radio = times[i];
			if(radio.checked){
				flag2 = true;
				break;
			}
		}
		if(!flag2){
			alert("请选择咨询时间");
			return false;
		}
		return true;
	}
</script>
</head>
<body>
<table width="100%" border="1" cellpadding="3" cellspacing="0" class="InfoShowPanel">
	<tr>
		<th width="30%">昵称</th>
		<td ><s:property value='adviserEntity.nick_name' /></td>
	</tr>
	<tr>
		<th >性别</th>
		<td >
			<s:if test="adviserEntity.sex==1">男</s:if>
			<s:else>女</s:else>
		</td>
	</tr>
	<tr>
		<th>年龄</th>
		<td><s:property value='adviserEntity.age' /></td>
	</tr>
	<tr>
		<th>地区</th>
		<td><s:property value='adviserEntity.provinceName' /> <s:property value='adviserEntity.cityName' /> <s:property value='adviserEntity.regionName' /></td>
	</tr>
	<tr>
		<th>证书编号</th>
		<td><s:property value='adviserEntity.certificate_number' /></td>
	</tr>
	<tr>
		<th>级别</th>
		<td><s:property value='adviserEntity.level' /></td>
	</tr>
	<tr>
		<th>可咨询年龄段</th>
		<td><s:property value='adviserEntity.age_group' /></td>
	</tr>
	<tr>
		<th>咨询范围</th>
		<td><s:property value='adviserEntity.advisory_scope' /></td>
	</tr>
	<tr>
		<th>擅长方法</th>
		<td><s:property value='adviserEntity.good_way' /></td>
	</tr>
	<tr>
		<th>自述</th>
		<td><s:property value='adviserEntity.description' /></td>
	</tr>
	<tr>
		<th>加入时间</th>
		<td><s:property value='adviserEntity.create_date' /></td>
	</tr>
</table> 
<table width="100%" border="1" cellpadding="3" cellspacing="0" class="InfoShowPanel">
	<tr>
		<th width="30%">当前咨询收费方式和单价</th>
		<td><s:property value='adviserEntity.price_plan' /></td>
	</tr>
</table>
<table width="100%" border="1" cellpadding="3" cellspacing="0" class="InfoShowPanel">
	<tr>
		<th width="30%">备注</th>
		<td><s:property value='adviserEntity.remark' /></td>
	</tr>
</table>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="InfoShowPanel">
	<tr>
		<td><img alt="" src="<s:property value='adviserEntity.assessImgUrl' />"></td>
	</tr>
</table>
<form name="form" action="advisory!orderAdvisory" method="post" onsubmit="return check();">
<s:hidden name="fromUserName" property="fromUserName"></s:hidden>
<input type="hidden" name="account" value="<s:property value='adviserEntity.account' />" >
<input type="hidden" name="advisoryName" value="<s:property value='adviserEntity.name' />" >
<input type="hidden" name="advisoryPhone" value="<s:property value='adviserEntity.phone' />" >
<table width="100%" border="1" cellpadding="3" cellspacing="0" class="InfoShowPanel">
	<tr>
		<th width="30%">预约方式</th> 
		<td>
			<input type="radio" id="qqRadio" name="adviseType" value="1"><label for="female">qq</label> &nbsp;&nbsp;<input type="text" name="qqNo" id="qqRadioTxt" size="15"><br />
			<input type="radio" id="phoneRadio" name="adviseType" value="2"><label for="phoneRadio">电话</label> <input type="text" name="phone" id="phoneRadioTxt" size="15"><br />
			<input type="radio" id="faceTofaceRadio" name="adviseType" value="3"><label for="faceTofaceRadio">面谈</label> <input type="text" name="faceToface" id="faceTofaceRadioTxt" size="15">
		</td>
	<tr>
		<th>预约时间（以1小时为单位）</th> 
		<td>
			<table border="1" cellpadding="3" cellspacing="0" class="InfoShowPanel">
				<tr><th>今天</th><th>明天</th><th>后天</th></tr>
				<tr> 
					<td width="20%" valign="top">
						<s:iterator value='adviserEntity.todayFreeTimes' id='freeTime' status='st'>
							<input type="radio" name="freeTimeRadio" id="freeTimeA<s:property value='#st.index' />" value='a<s:property value='freeTime'/>'> <label for="freeTimeA<s:property value='#st.index' />"><s:property value='freeTime'/>点</label>
							<s:if test="#st.index!=#st.count"><br /></s:if> 
						</s:iterator>
					</td>
					<td width="20%" valign="top">
						<s:iterator value='adviserEntity.freeTimes1' id='freeTime' status='st'>
							<input type="radio" name="freeTimeRadio" id="freeTimeB<s:property value='#st.index' />" value='b<s:property value='freeTime'/>'> <label for="freeTimeB<s:property value='#st.index' />"><s:property value='freeTime'/>点</label>
							<s:if test="#st.index!=#st.count"><br /></s:if> 
						</s:iterator>
					</td>
					<td width="20%" valign="top">
						<s:iterator value='adviserEntity.freeTimes2' id='freeTime' status='st'>
							<input type="radio" name="freeTimeRadio" id="freeTimeC<s:property value='#st.index' />" value='c<s:property value='freeTime'/>'> <label for="freeTimeC<s:property value='#st.index' />"><s:property value='freeTime'/>点</label>
							<s:if test="#st.index!=#st.count"><br /></s:if> 
						</s:iterator>
					</td>
				</tr>
			</table>
			
		</td>
	</tr>
</table>
<br />
<label><input type="checkbox" id="sureButton" > 我已经了解并认可<a href='<%=request.getContextPath()%>/pages/weixin/advisory_direction.jsp'>咨询说明书</a>内容</label>
<div style="text-align:-webkit-center"><input type="submit" value="预约"></div>
</form>
<div style="color:red;font-size:12px;margin-top:10px;">微信添加朋友，查找微信公众账号：心灵四叶草  就可以关注我们</div>
</body>
</html>