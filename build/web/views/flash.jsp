<%-- 
    Document   : flash
    Created on : Jul 4, 2013, 8:30:26 PM
    Author     : aldo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
	<%= request.getAttribute("message")  %>
    </body>
</html>
