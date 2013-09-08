<jsp:useBean id="helper" class="core.helpers.Bootstrap3Helper" />
<% helper.init(request,response); %>
 <div class="container">
    <%=helper.flashText()%> 
    <form action="/login/verify" method="post">
        <%=helper.formGroupText("email", "Email :")%>
        <%=helper.formGroupPassword("password", "Password:")%>
        <%=helper.csrfField()%>
        <%=helper.submit("Login")%>
    </form>
</div> <!-- /container -->
