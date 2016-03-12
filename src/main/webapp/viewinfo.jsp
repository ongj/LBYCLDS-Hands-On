<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<html>
<head>
    <title>View Info</title>
</head>
<body>
	<h3>Information Extraction Complete</h3>
	<% 
        String age = (String) request.getAttribute("age");
		String gender = (String) request.getAttribute("gender");
		
		out.println("Age: " + age);
		out.println("Gender: " + gender);
      %>

    
</body>
</html>