<%@ page contentType="image/jpeg" import="javax.imageio.*" %>

<jsp:useBean id="image" scope="session" class="com.yidatec.weixin.util.VerificationImage"/>

<%

response.setHeader("Buffer", "true");
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
//Response.ExpiresAbsolute = System.DateTime.Now.AddSeconds(-1);

// 输出图象到页面
ImageIO.write(image.creatImage(), "JPEG", response.getOutputStream());

//将认证码存入SESSION
session.setAttribute("ver_rand", image.sRand);

out.clear();
out = pageContext.pushBody();

%> 