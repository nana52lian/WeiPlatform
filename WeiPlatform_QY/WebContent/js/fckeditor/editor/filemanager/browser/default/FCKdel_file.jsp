<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="org.apache.struts2.ServletActionContext,java.io.File;" %>

<% 

	String filePath = request.getParameter("filePath"); 
	String ud = request.getParameter("UD");
	
	
	//String localPath = ServletActionContext.getServletContext().getRealPath("");
	//String ttPath = localPath.replaceAll("ttadmin", "TravelTicket");
	//File localImageFile = new File(localPath + "/" + filePath);
	//File ttImageFile = new File(ttPath + "/" + filePath);
	
	//System.out.println(localImageFile);
	//System.out.println(ttImageFile);
	
	String localPath = ServletActionContext.getServletContext().getRealPath("\\");
	System.out.println("=============================");
	System.out.println(localPath);
	System.out.println("=============================");
	String localFileString = localPath.concat(filePath.substring(filePath.indexOf("upload_files")));
	System.out.println("=============================");
	System.out.println(localFileString);
	System.out.println("=============================");
	//System.out.println(localFileString);
	//String ttFileString = localFileString.replaceAll("ttadmin", "TravelTicket");
	//System.out.println(ttFileString);
	File localFile = new File(localFileString);
	//File ttImageFile = new File(ttFileString);
	//System.out.println(localFile);
	//System.out.println(ttImageFile);

	if(localFile.exists())
		localFile.delete();
	//if(ttImageFile.exists())
		//ttImageFile.delete();

%>