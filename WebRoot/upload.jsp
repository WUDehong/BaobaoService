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
   <form action="upload" method="post" enctype="multipart/form-data">
   		Book id:<input type="text" name="id" value="1"><br>
   		Book name:<input type="text" name="name" value="1"><br>
   		Book author:<input type="text" name="author" value="1"><br>
   		Book price:<input type="text" name="price" value="1"><br>
   		Book description:<input type="text" name="description" value="wawawa"><br>
   		Book image<input type="file" name="imageFile"><br>
   		Book source<input type="file" name="sourceFile"><br>
   		<input type="submit" name="submit" value="上传">
   </form>
  </body>
</html>
