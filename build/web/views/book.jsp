<%@page import="java.util.*,
	app.entities.Book"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World! artist</h1>
	
<%	 
    List<Book> books = (List<Book>)request.getAttribute("books");
    if(books!=null )
    {    
        String className="odd";
        for(Iterator it = books.iterator();it.hasNext();)
        {
	    Book b = (Book)it.next();  
	    %>
	    <p><%= b.getTitle() %></p>
	    <%
	}
    }
%>
    </body>
</html>
