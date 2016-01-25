<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'upload.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
   <form action="uploadAPK" method="post" enctype="multipart/form-data">
   		Version code:<input type="text" name="versionCode" value="1"><br>
   		Version name:<input type="text" name="versionName" value="版本1.0"><br>
   		Version description:<input type="text" name="versionDescription" value="wawawa"><br>
   		<input type="file" name="file"><br>
   		<input type="submit" name="submit" value="上传">
   </form>
  </body>
</html>
