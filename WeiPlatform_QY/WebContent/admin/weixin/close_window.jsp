<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关闭</title>
</head>
<body>
<table>
<tr><th>类型：</th><td>
<input type="radio" name="closetype" value="1" onclick="normal();" />正常
<input type="radio" name="closetype" value="2" onclick="abnormal()"/>非正常</td></tr>
<tr><th>原因：</th><td><textarea id="abnormal_content" name="abnormal_content" rows="8" cols="30" ></textarea></td></tr>
</table>
<table>
 <tr>
<td style="padding-left: 100px;"><input type="button" class="commonButton" value="确定" onclick="exit()" style="cursor: pointer;"/></td>
<td><input type="button" class="commonButton" value="取消" onclick="closeWindow('openCloseWindowsDiv');" style="cursor: pointer;"/></td>
</tr>
</table>
</body>
</html>